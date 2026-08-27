package com.example.kumiengine.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kumiengine.ui.theme.AccentAmber
import com.example.kumiengine.ui.theme.AccentCyan
import com.example.kumiengine.ui.theme.AccentRose
import com.example.kumiengine.ui.theme.BorderSubtle
import com.example.kumiengine.ui.theme.PrimaryBlue
import com.example.kumiengine.ui.theme.SecondaryEmerald
import com.example.kumiengine.ui.theme.SurfaceDark
import com.example.kumiengine.ui.theme.TextMuted
import com.example.kumiengine.ui.theme.TextPrimary
import com.example.kumiengine.ui.theme.TextSecondary

@Composable
fun TripartiteBrainView(
    cryptAiEntropy: Float,
    genAiPatchLabel: String,
    testAiState: String,
    isHealing: Boolean,
    onTriggerSelfHealing: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animEntropy by animateFloatAsState(
        targetValue = cryptAiEntropy,
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "animEntropy"
    )

    val isEntropyLow = animEntropy < 0.92f
    val entropyColor by animateColorAsState(
        targetValue = if (isEntropyLow) AccentRose else SecondaryEmerald,
        animationSpec = tween(400),
        label = "entropyColor"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark, RoundedCornerShape(16.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Hub,
                    contentDescription = "Cerebro Autónomo",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "CEREBRO AUTÓNOMO // NODO MX-SQ-3000",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TextPrimary
                    )
                    Text(
                        text = "ARQUITECTURA SOBERANA: CryptAI + GenAI + TestAI",
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isHealing) AccentAmber.copy(alpha = 0.2f) else SecondaryEmerald.copy(alpha = 0.15f))
                    .border(
                        1.dp,
                        if (isHealing) AccentAmber else SecondaryEmerald,
                        RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isHealing) "SIMBIOSIS ACTIVA" else "HOMEOSTASIS 100%",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = if (isHealing) AccentAmber else SecondaryEmerald
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Three Glass Module Cards
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // 1. CryptAI Card
            ModuleCard(
                accentColor = PrimaryBlue,
                icon = { Icon(Icons.Default.Lock, contentDescription = "CryptAI", tint = PrimaryBlue, modifier = Modifier.size(16.dp)) },
                title = "CryptAI",
                subtitle = "CML Map",
                description = "Mapeo caótico y entropía vectorial activa.",
                valueLabel = "Entropía: ${String.format("%.4f", animEntropy)}",
                valueColor = entropyColor,
                testTag = "card_cryptai"
            )

            // 2. GenAI Card
            ModuleCard(
                accentColor = SecondaryEmerald,
                icon = { Icon(Icons.Default.Bolt, contentDescription = "GenAI", tint = SecondaryEmerald, modifier = Modifier.size(16.dp)) },
                title = "GenAI",
                subtitle = "KUMI Engine",
                description = "Reparación recursiva e inyección de parches.",
                valueLabel = "Parches: $genAiPatchLabel",
                valueColor = if (genAiPatchLabel.startsWith("0")) TextPrimary else AccentAmber,
                testTag = "card_genai"
            )

            // 3. TestAI Card
            ModuleCard(
                accentColor = AccentRose,
                icon = { Icon(Icons.Default.Science, contentDescription = "TestAI", tint = AccentRose, modifier = Modifier.size(16.dp)) },
                title = "TestAI",
                subtitle = "Sandbox 1MB",
                description = "Validación topológica y telemetría GCS.",
                valueLabel = "ESTADO: $testAiState",
                valueColor = if (testAiState == "VIGILANDO") AccentRose else AccentCyan,
                testTag = "card_testai"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Symbiotic Self-Healing Trigger Button
        Button(
            onClick = onTriggerSelfHealing,
            enabled = !isHealing,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("btn_trigger_symbiosis"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                disabledContainerColor = SurfaceDark
            )
        ) {
            if (isHealing) {
                CircularProgressIndicator(
                    color = AccentAmber,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "REPARACIÓN SIMBIÓTICA EN EJECUCIÓN...",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = AccentAmber
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AutoFixHigh,
                    contentDescription = "Auto Fix",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INDUCIR INESTABILIDAD & AUTO-REPARAR",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ModuleCard(
    accentColor: Color,
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    description: String,
    valueLabel: String,
    valueColor: Color,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.5f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag(testTag)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    icon()
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = accentColor
                    )
                }

                Text(
                    text = subtitle,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted,
                    modifier = Modifier
                        .background(Color.Black, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.8f))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = valueLabel,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
            }
        }
    }
}
