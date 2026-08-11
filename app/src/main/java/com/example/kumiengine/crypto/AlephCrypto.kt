package com.example.kumiengine.crypto

import java.security.MessageDigest
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

data class AlephConfig(
    val q: Long = 4294967291L, // 2^32 - 5 (large prime)
    val dimension: Int = 2,
    val noiseStdDev: Double = 3.2,
    val quadrants: Int = 4
)

data class CiphertextResult(
    val ciphertext: LongArray,
    val metadata: EncryptionMetadata
)

data class EncryptionMetadata(
    val quadrant: Int,
    val messagePoint: Pair<Long, Long>,
    val errorVector: Pair<Long, Long>,
    val noiseMagnitude: Double
)

data class DecryptionResult(
    val recoveredHash: ByteArray,
    val metadata: DecryptionMetadata
)

data class DecryptionMetadata(
    val recoveredPoint: Pair<Long, Long>,
    val quadrant: Int
)

class AlephCryptography(val config: AlephConfig = AlephConfig()) {
    
    var generatorG: Array<LongArray>? = null // 4x2 matrix
    var privateKeySK: Array<LongArray>? = null // 2x2 matrix
    var publicKeyPK: Array<LongArray>? = null // 4x2 matrix
    var currentPhase: Int = 0

    /**
     * Phase A: Key Generation (Soldadura de Clave)
     * Generates generator matrix G and projects short basis to 4 quadrants
     */
    fun generateKeys(seed: ByteArray? = null): Pair<Array<LongArray>, Array<LongArray>> {
        val rand = if (seed != null) {
            val seedInt = seed.take(4).fold(0) { acc, byte -> (acc shl 8) or (byte.toInt() and 0xFF) }
            Random(seedInt)
        } else {
            Random.Default
        }

        // Generator matrix G (4x2)
        val gMatrix = Array(4) { LongArray(2) }
        for (i in 0 until 4) {
            for (j in 0 until 2) {
                gMatrix[i][j] = abs(rand.nextLong()) % config.q
            }
        }
        this.generatorG = gMatrix
        this.publicKeyPK = gMatrix

        // Private Key SK: 2x2 short basis matrix
        val skRaw = Array(2) { LongArray(2) }
        for (i in 0 until 2) {
            for (j in 0 until 2) {
                skRaw[i][j] = (abs(rand.nextLong()) % 1000L) + 1L
            }
        }
        // Ensure determinant is non-zero
        val det = skRaw[0][0] * skRaw[1][1] - skRaw[0][1] * skRaw[1][0]
        if (det == 0L) {
            skRaw[0][0] += 1
        }
        this.privateKeySK = skRaw

        return Pair(this.publicKeyPK!!, this.privateKeySK!!)
    }

    /**
     * Determine Cartesian quadrant phase for point (x, y)
     */
    fun quadrantPhase(point: Pair<Long, Long>): Int {
        val (x, y) = point
        return when {
            x >= 0 && y >= 0 -> 0 // Quadrant I
            x < 0 && y >= 0 -> 1  // Quadrant II
            x < 0 && y < 0 -> 2   // Quadrant III
            else -> 3            // Quadrant IV
        }
    }

    /**
     * Rotate phase between quadrants: T(x,y) -> (-y, x) per 90-degree phase shift
     */
    fun rotatePhase(point: Pair<Long, Long>, targetPhase: Int): Pair<Long, Long> {
        val currPhase = quadrantPhase(point)
        val rotationsNeeded = (targetPhase - currPhase + 4) % 4
        
        var rotated = point
        for (i in 0 until rotationsNeeded) {
            // 90 deg rotation: (x, y) -> (-y, x)
            rotated = Pair(-rotated.second, rotated.first)
        }
        this.currentPhase = targetPhase
        return rotated
    }

    /**
     * Phase B: Encryption (Transmisión Transónica)
     * C = (M · G) + e (mod q)
     */
    fun encrypt(message: ByteArray, targetQuadrant: Int? = null): CiphertextResult {
        val g = generatorG ?: throw IllegalStateException("Keys not generated yet")

        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(message)

        var mx = 0L
        for (i in 0 until 16) {
            mx = (mx shl 8) or (hash[i].toLong() and 0xFF)
        }
        mx = abs(mx) % config.q

        var my = 0L
        for (i in 16 until 32) {
            my = (my shl 8) or (hash[i].toLong() and 0xFF)
        }
        my = abs(my) % config.q

        var messagePoint = Pair(mx, my)

        if (targetQuadrant != null) {
            messagePoint = rotatePhase(messagePoint, targetQuadrant)
        }

        // Gaussian noise vector e
        val javaRand = java.util.Random()
        val ex = (javaRand.nextGaussian() * config.noiseStdDev).toLong()
        val ey = (javaRand.nextGaussian() * config.noiseStdDev).toLong()
        val errorVector = Pair(ex, ey)

        // Projection C = (M · G) + e (mod q)
        val ct = LongArray(4)
        for (i in 0 until 4) {
            val proj = messagePoint.first * g[i][0] + messagePoint.second * g[i][1] + ex
            ct[i] = (proj % config.q + config.q) % config.q
        }

        val noiseMag = sqrt((ex * ex + ey * ey).toDouble())

        val metadata = EncryptionMetadata(
            quadrant = currentPhase,
            messagePoint = messagePoint,
            errorVector = errorVector,
            noiseMagnitude = noiseMag
        )

        return CiphertextResult(ct, metadata)
    }

    /**
     * Phase C: Decryption
     */
    fun decrypt(ciphertext: LongArray): DecryptionResult {
        val sk = privateKeySK ?: throw IllegalStateException("Private key missing")

        // Simple lattice reduction with short basis
        val det = sk[0][0] * sk[1][1] - sk[0][1] * sk[1][0]
        val c0 = ciphertext[0]
        val c1 = ciphertext[1]

        val recX = ((c0 * sk[1][1] - c1 * sk[0][1]) / (if (det != 0L) det else 1L)) % config.q
        val recY = ((-c0 * sk[1][0] + c1 * sk[0][0]) / (if (det != 0L) det else 1L)) % config.q

        val recoveredPoint = Pair(abs(recX), abs(recY))
        val quad = quadrantPhase(recoveredPoint)

        val digest = MessageDigest.getInstance("SHA-256")
        val recHash = digest.digest(recoveredPoint.toString().toByteArray())

        val metadata = DecryptionMetadata(
            recoveredPoint = recoveredPoint,
            quadrant = quad
        )

        return DecryptionResult(recHash, metadata)
    }
}
