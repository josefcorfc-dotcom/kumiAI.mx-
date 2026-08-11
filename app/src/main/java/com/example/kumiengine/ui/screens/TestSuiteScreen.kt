package com.example.kumiengine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.example.kumiengine.ui.theme.AccentCyan
import com.example.kumiengine.ui.theme.BorderSubtle
import com.example.kumiengine.ui.theme.DarkBackground
import com.example.kumiengine.ui.theme.ErrorRed
import com.example.kumiengine.ui.theme.PrimaryBlue
import com.example.kumiengine.ui.theme.SecondaryEmerald
import com.example.kumiengine.ui.theme.SurfaceDark
import com.example.kumiengine.ui.theme.TextMuted
import com.example.kumiengine.ui.theme.TextPrimary
import com.example.kumiengine.viewmodel.KumiViewModel

@Composable
fun TestSuiteScreen(
    viewModel: KumiViewModel,
    modifier: Modifier = Modifier
) {
    val isTesting by viewModel.isTesting.collectAsState()
    val testReport by viewModel.testReport.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Run Suite Header Card
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
                        imageVector = Icons.Default.Security,
                        contentDescription = "Test Suite",
                        tint = AccentCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "ALEPH-1 TEST SUITE & EVALUATION",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Text(
                    text = "Evaluación rigurosa e imparcial del sistema de retículos. Ejecuta las 7 pruebas de rendimiento, ruido, rotación y entropía.",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted
                )

                Button(
                    onClick = { viewModel.runTestSuite() },
                    enabled = !isTesting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("run_test_suite_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    if (isTesting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "EVALUANDO SISTEMA...",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Run")
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(
                                text = "EJECUTAR EVALUACIÓN COMPLETA",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Test Report Summary
        testReport?.let { report ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (report.successRate == 100.0) SecondaryEmerald else ErrorRed,
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "REPORTE FINAL DE EVALUACIÓN",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "${report.successRate}% ÉXITO",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = if (report.successRate == 100.0) SecondaryEmerald else ErrorRed
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pruebas Pasadas: ${report.testsPassed} / ${report.testsTotal}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                        Text(
                            text = "Nodo: ${report.node}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                    }
                }
            }

            // Individual Test Result Cards
            report.results.forEach { result ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (result.status == "PASS") Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = result.status,
                                    tint = if (result.status == "PASS") SecondaryEmerald else ErrorRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = result.name,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            Text(
                                text = "${result.durationMs}ms",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = AccentCyan
                            )
                        }

                        result.details.forEach { (key, value) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = key,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextMuted
                                )
                                Text(
                                    text = value,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
