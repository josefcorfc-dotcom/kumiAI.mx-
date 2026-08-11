package com.example.kumiengine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kumiengine.ui.components.CartesianPlaneView
import com.example.kumiengine.ui.theme.AccentCyan
import com.example.kumiengine.ui.theme.BorderSubtle
import com.example.kumiengine.ui.theme.DarkBackground
import com.example.kumiengine.ui.theme.PrimaryBlue
import com.example.kumiengine.ui.theme.SecondaryEmerald
import com.example.kumiengine.ui.theme.SurfaceDark
import com.example.kumiengine.ui.theme.TextMuted
import com.example.kumiengine.ui.theme.TextPrimary
import com.example.kumiengine.ui.theme.TextSecondary
import com.example.kumiengine.viewmodel.KumiViewModel

@Composable
fun CryptoConsoleScreen(
    viewModel: KumiViewModel,
    modifier: Modifier = Modifier
) {
    val inputMsg by viewModel.inputMessage.collectAsState()
    val targetQuadrant by viewModel.targetQuadrant.collectAsState()
    val ciphertextRes by viewModel.lastCiphertext.collectAsState()
    val decryptionRes by viewModel.lastDecryption.collectAsState()
    val simPoint by viewModel.simulatedPoint.collectAsState()
    val simPhase by viewModel.simulatedPhase.collectAsState()

    val scrollState = rememberScrollState()
    val quadNames = listOf("Cuadrante I (+x,+y)", "Cuadrante II (-x,+y)", "Cuadrante III (-x,-y)", "Cuadrante IV (+x,-y)")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Message Encryption Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encrypt",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "CIFRADO LATTICE (ALEPH-1)",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                OutlinedTextField(
                    value = inputMsg,
                    onValueChange = { viewModel.setInputMessage(it) },
                    label = { Text("Mensaje a Cifrar", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("message_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.3f)
                    )
                )

                // Quadrant selector
                Text(
                    text = "Cuadrante Objetivo para Proyección:",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (i in 0..3) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .background(
                                    if (targetQuadrant == i) PrimaryBlue else Color.Black,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { viewModel.setTargetQuadrant(i) }
                                .testTag("quadrant_btn_$i"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Q${i + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                        }
                    }
                }

                Button(
                    onClick = { viewModel.encryptCurrentMessage() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("encrypt_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(
                        text = "EJECUTAR CIFRADO LATTICE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Ciphertext Result Inspector
        ciphertextRes?.let { ct ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "CIPHERTEXT GENERADO (4-VECTOR):",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = ct.ciphertext.joinToString(", "),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = AccentCyan
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Cuadrante: Q${ct.metadata.quadrant + 1}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextSecondary
                        )
                        Text(
                            text = "Ruido Gaussiano: ${String.format("%.2f", ct.metadata.noiseMagnitude)}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = SecondaryEmerald
                        )
                    }

                    Button(
                        onClick = { viewModel.decryptCurrentCiphertext() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("decrypt_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryEmerald)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LockOpen, contentDescription = "Decrypt")
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "DESENCRIPTAR CON BASE CORTA",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Decryption Result
        decryptionRes?.let { dec ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SecondaryEmerald, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "✓ DESENCRIPTACIÓN EXITOSA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = SecondaryEmerald
                    )
                    Text(
                        text = "Punto Cartesiano Recuperado: ${dec.metadata.recoveredPoint}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextPrimary
                    )
                    Text(
                        text = "Integridad Cantoriana: CONFIRMADA",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }
            }
        }

        // Cartesian Quadrant Simulator
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Rotation",
                            tint = AccentCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "SIMULADOR ROTACIÓN DE FASE",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "(${simPoint.first}, ${simPoint.second})",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = AccentCyan
                    )
                }

                CartesianPlaneView(
                    point = simPoint,
                    phase = simPhase,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Text(
                    text = "Rotar Fase a Cuadrante:",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (i in 0..3) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .background(
                                    if (simPhase == i) AccentCyan else Color.Black,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { viewModel.rotateSimulatedPhase(i) }
                                .testTag("sim_phase_btn_$i"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Q${i + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = if (simPhase == i) Color.Black else TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
