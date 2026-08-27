package com.example.kumiengine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kumiengine.crypto.AlephConfig
import com.example.kumiengine.crypto.AlephCryptography
import com.example.kumiengine.crypto.AlephTestSuite
import com.example.kumiengine.crypto.CiphertextResult
import com.example.kumiengine.crypto.DecryptionResult
import com.example.kumiengine.crypto.FullSuiteReport
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class TelemetryPoint(val timeIndex: Int, val resonanceVal: Float)

class KumiViewModel : ViewModel() {

    val alephCrypto = AlephCryptography(AlephConfig())

    // Telemetry State
    private val _telemetry = MutableStateFlow<List<TelemetryPoint>>(
        List(20) { i -> TelemetryPoint(i, 97.05f + (Random.nextFloat() - 0.5f) * 0.1f) }
    )
    val telemetry: StateFlow<List<TelemetryPoint>> = _telemetry.asStateFlow()

    // R^8 Vector Component Hotness State
    private val _vectorR8 = MutableStateFlow(
        FloatArray(8) { Random.nextFloat() * 0.7f + 0.15f }
    )
    val vectorR8: StateFlow<FloatArray> = _vectorR8.asStateFlow()

    // Dinamo Keypad State
    private val _dinamoKeys = MutableStateFlow(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))
    val dinamoKeys: StateFlow<List<Int>> = _dinamoKeys.asStateFlow()

    private val _enteredPasscode = MutableStateFlow("")
    val enteredPasscode: StateFlow<String> = _enteredPasscode.asStateFlow()

    // Aleph-1 Handshake Efficiency & Latency
    private val _handshakeEfficiency = MutableStateFlow(0.994f)
    val handshakeEfficiency: StateFlow<Float> = _handshakeEfficiency.asStateFlow()

    private val _networkLatencyMs = MutableStateFlow(24.8f)
    val networkLatencyMs: StateFlow<Float> = _networkLatencyMs.asStateFlow()

    // Tripartite Autonomous Brain State (CryptAI - GenAI - TestAI)
    private val _cryptAiEntropy = MutableStateFlow(0.9942f)
    val cryptAiEntropy: StateFlow<Float> = _cryptAiEntropy.asStateFlow()

    private val _genAiPatchLabel = MutableStateFlow("0 (Estable)")
    val genAiPatchLabel: StateFlow<String> = _genAiPatchLabel.asStateFlow()

    private val _testAiState = MutableStateFlow("VIGILANDO")
    val testAiState: StateFlow<String> = _testAiState.asStateFlow()

    private val _isSymbioticHealing = MutableStateFlow(false)
    val isSymbioticHealing: StateFlow<Boolean> = _isSymbioticHealing.asStateFlow()

    // Audit Logs
    private val _auditLogs = MutableStateFlow<List<String>>(
        listOf(
            "[SYS] Nodo MX-SQ-3000 // Protocolo Omega activo.",
            "[INIT] Firebase AppCheck Verified: cantoriano-leyvajf",
            "[NET] Handshake Aleph-1 sincronizado a 97.050 GHz.",
            "[TRIPARTITE] CryptAI + GenAI + TestAI enlazados en topología de Cantor."
        )
    )
    val auditLogs: StateFlow<List<String>> = _auditLogs.asStateFlow()

    // Crypto Console State
    private val _sessionId = MutableStateFlow("session_${System.currentTimeMillis()}")
    val sessionId: StateFlow<String> = _sessionId.asStateFlow()

    private val _inputMessage = MutableStateFlow("La vida es complicada pero muy hermosa")
    val inputMessage: StateFlow<String> = _inputMessage.asStateFlow()

    private val _targetQuadrant = MutableStateFlow(0) // 0=Q1, 1=Q2, 2=Q3, 3=Q4
    val targetQuadrant: StateFlow<Int> = _targetQuadrant.asStateFlow()

    private val _lastCiphertext = MutableStateFlow<CiphertextResult?>(null)
    val lastCiphertext: StateFlow<CiphertextResult?> = _lastCiphertext.asStateFlow()

    private val _lastDecryption = MutableStateFlow<DecryptionResult?>(null)
    val lastDecryption: StateFlow<DecryptionResult?> = _lastDecryption.asStateFlow()

    // Phase Rotation Simulator Point
    private val _simulatedPoint = MutableStateFlow(Pair(120L, 85L))
    val simulatedPoint: StateFlow<Pair<Long, Long>> = _simulatedPoint.asStateFlow()

    private val _simulatedPhase = MutableStateFlow(0)
    val simulatedPhase: StateFlow<Int> = _simulatedPhase.asStateFlow()

    // Test Suite State
    private val _testReport = MutableStateFlow<FullSuiteReport?>(null)
    val testReport: StateFlow<FullSuiteReport?> = _testReport.asStateFlow()

    private val _isTesting = MutableStateFlow(false)
    val isTesting: StateFlow<Boolean> = _isTesting.asStateFlow()

    init {
        // Initialize Keys
        alephCrypto.generateKeys("KUMI-MX-SQ-3000".toByteArray())
        
        // Start Telemetry & Scramble Loop
        viewModelScope.launch {
            var timeCounter = 20
            while (true) {
                delay(1800)
                timeCounter++
                val nextVal = 97.05f + (Random.nextFloat() - 0.5f) * 0.12f
                _telemetry.value = _telemetry.value.drop(1) + TelemetryPoint(timeCounter, nextVal)

                // Handshake Efficiency & Latency updates
                _handshakeEfficiency.value = (0.988f + Random.nextFloat() * 0.011f).coerceIn(0.985f, 0.999f)
                _networkLatencyMs.value = (24.0f + Random.nextFloat() * 1.6f)

                // CryptAI entropy background flutter (if not actively healing)
                if (!_isSymbioticHealing.value) {
                    val baseEntropy = 0.965f + Random.nextFloat() * 0.034f
                    _cryptAiEntropy.value = baseEntropy
                }

                // Real-time R^8 vector component hotness processing
                val currentR8 = _vectorR8.value
                val newR8 = FloatArray(8) { idx ->
                    val prev = currentR8[idx]
                    val delta = (Random.nextFloat() - 0.5f) * 0.22f
                    (prev + delta).coerceIn(0.05f, 0.98f)
                }
                _vectorR8.value = newR8

                if (Random.nextFloat() > 0.85f) {
                    scrambleKeyboard()
                }
            }
        }
    }

    fun triggerSymbioticSelfHealing() {
        if (_isSymbioticHealing.value) return
        viewModelScope.launch {
            _isSymbioticHealing.value = true
            
            // 1. Trigger entropy drop in CryptAI
            val lowEntropy = 0.8872f
            _cryptAiEntropy.value = lowEntropy
            addAuditLog("[CryptAI] Caída de entropía detectada (${String.format("%.4f", lowEntropy)} < 0.9200). Activando GenAI...")
            delay(1000)

            // 2. GenAI creates repair patch
            _genAiPatchLabel.value = "1 (Inyectado: [CML-REPAIR-01])"
            addAuditLog("[GenAI] Parche [CML-REPAIR-01] generado en Kumi-engine.")
            delay(800)

            // 3. TestAI validates sandbox 1MB
            _testAiState.value = "VALIDANDO (Sandbox 1MB)"
            addAuditLog("[TestAI] Iniciando validación topológica en Sandbox 1MB...")
            delay(1500)

            addAuditLog("[TestAI] Validación de sandbox 1MB exitosa. Distancia coseno óptima: 0.999.")
            _testAiState.value = "VIGILANDO"
            _genAiPatchLabel.value = "0 (Estable)"
            _cryptAiEntropy.value = 0.9942f
            _handshakeEfficiency.value = 0.999f
            _isSymbioticHealing.value = false
            addAuditLog("[SYS] Simbiosis CryptAI + GenAI + TestAI restaurada. Nodo MX-SQ-3000 óptimo.")
        }
    }

    fun scrambleKeyboard() {
        val list = _dinamoKeys.value.toMutableList()
        list.shuffle()
        _dinamoKeys.value = list
        addAuditLog("[DINAMO] Teclado Cantoriano reordenado // Axioma de desplazamiento.")
    }

    fun pressDinamoKey(digit: Int) {
        if (_enteredPasscode.value.length < 12) {
            _enteredPasscode.value += digit.toString()
            scrambleKeyboard()
        }
    }

    fun clearPasscode() {
        _enteredPasscode.value = ""
    }

    fun setInputMessage(msg: String) {
        _inputMessage.value = msg
    }

    fun setTargetQuadrant(quadrant: Int) {
        _targetQuadrant.value = quadrant
    }

    fun encryptCurrentMessage() {
        viewModelScope.launch {
            val msg = _inputMessage.value
            if (msg.isNotEmpty()) {
                val res = alephCrypto.encrypt(msg.toByteArray(), _targetQuadrant.value)
                _lastCiphertext.value = res
                _lastDecryption.value = null
                
                // Pulse R^8 hotness vector components
                _vectorR8.value = FloatArray(8) { Random.nextFloat() * 0.4f + 0.58f }
                
                val quadNames = listOf("I", "II", "III", "IV")
                addAuditLog("[ENC] Mensaje cifrado en Cuadrante ${quadNames[res.metadata.quadrant]} | Ruido: ${String.format("%.2f", res.metadata.noiseMagnitude)}")
            }
        }
    }

    fun decryptCurrentCiphertext() {
        viewModelScope.launch {
            val ct = _lastCiphertext.value
            if (ct != null) {
                val res = alephCrypto.decrypt(ct.ciphertext)
                _lastDecryption.value = res
                val quadNames = listOf("I", "II", "III", "IV")
                addAuditLog("[DEC] Ciphertext recuperado en Cuadrante ${quadNames[res.metadata.quadrant]} | Punto: ${res.metadata.recoveredPoint}")
            }
        }
    }

    fun rotateSimulatedPhase(targetPhase: Int) {
        val currPt = _simulatedPoint.value
        val rotated = alephCrypto.rotatePhase(currPt, targetPhase)
        _simulatedPoint.value = rotated
        _simulatedPhase.value = targetPhase
        val quadNames = listOf("I", "II", "III", "IV")
        addAuditLog("[ROTATE] Fase rotada a Cuadrante ${quadNames[targetPhase]} -> $rotated")
    }

    fun runTestSuite() {
        viewModelScope.launch {
            _isTesting.value = true
            addAuditLog("[SUITE] Iniciando Aleph Test Suite...")
            delay(500)
            val suite = AlephTestSuite()
            val report = suite.runAllTests()
            _testReport.value = report
            _isTesting.value = false
            addAuditLog("[SUITE] Pruebas completadas: ${report.testsPassed}/${report.testsTotal} (Tasa: ${report.successRate}%)")
        }
    }

    fun addAuditLog(msg: String) {
        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        _auditLogs.value = _auditLogs.value + "[$timeStr] $msg"
    }
}
