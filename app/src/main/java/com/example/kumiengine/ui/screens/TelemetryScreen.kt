package com.example.kumiengine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kumiengine.ui.components.AuditTerminal
import com.example.kumiengine.ui.components.DinamoKeyboard
import com.example.kumiengine.ui.components.TelemetryChart
import com.example.kumiengine.ui.theme.AccentCyan
import com.example.kumiengine.ui.theme.BorderSubtle
import com.example.kumiengine.ui.theme.DarkBackground
import com.example.kumiengine.ui.theme.PrimaryBlue
import com.example.kumiengine.ui.theme.SecondaryEmerald
import com.example.kumiengine.ui.theme.SurfaceDark
import com.example.kumiengine.ui.theme.TextMuted
import com.example.kumiengine.ui.theme.TextPrimary
import com.example.kumiengine.viewmodel.KumiViewModel

@Composable
fun TelemetryScreen(
    viewModel: KumiViewModel,
    modifier: Modifier = Modifier
) {
    val telemetryData by viewModel.telemetry.collectAsState()
    val dinamoKeys by viewModel.dinamoKeys.collectAsState()
    val passcode by viewModel.enteredPasscode.collectAsState()
    val logs by viewModel.auditLogs.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Telemetry Chart
        TelemetryChart(telemetryData = telemetryData)

        // Dinamo Keyboard
        DinamoKeyboard(
            dinamoKeys = dinamoKeys,
            enteredPasscode = passcode,
            onKeyClick = { viewModel.pressDinamoKey(it) },
            onScrambleClick = { viewModel.scrambleKeyboard() },
            onClearClick = { viewModel.clearPasscode() }
        )

        // Metrics Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "INTEGRIDAD",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ℵ₁ CANTOR",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = SecondaryEmerald
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "PROTOCOLO",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ALEPH-1",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = PrimaryBlue
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "CUÁNTICO",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "RESISTENTE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = AccentCyan
                    )
                }
            }
        }

        // Audit Terminal
        AuditTerminal(logs = logs)
    }
}
