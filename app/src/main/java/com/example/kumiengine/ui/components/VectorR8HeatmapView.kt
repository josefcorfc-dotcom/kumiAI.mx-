package com.example.kumiengine.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
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
import com.example.kumiengine.ui.theme.BorderSubtle
import com.example.kumiengine.ui.theme.PrimaryBlue
import com.example.kumiengine.ui.theme.SecondaryEmerald
import com.example.kumiengine.ui.theme.SurfaceDark
import com.example.kumiengine.ui.theme.TextMuted
import com.example.kumiengine.ui.theme.TextPrimary
import com.example.kumiengine.ui.theme.TextSecondary

@Composable
fun VectorR8HeatmapView(
    vectorR8: FloatArray,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(SurfaceDark, RoundedCornerShape(16.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = "R8 Hotness",
                    tint = AccentAmber,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "CALORÍMETRO VECTORIAL R⁸ // PROYECTO LATENTE",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Text(
                text = "R⁸ → R⁴ CANÓNICO",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = AccentCyan,
                modifier = Modifier
                    .background(Color.Black, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grid 4x2 or 2x4 for 8 components
        val rows = (0 until 8).chunked(4)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            rows.forEach { rowIndices ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowIndices.forEach { idx ->
                        val value = if (idx < vectorR8.size) vectorR8[idx] else 0f
                        VectorComponentCell(
                            index = idx,
                            value = value,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("r8_component_$idx")
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Projection R8 -> R4 Cantor reduction calculation
        val proj0 = if (vectorR8.size >= 8) (vectorR8[0] + vectorR8[4]) / 2f else 0f
        val proj1 = if (vectorR8.size >= 8) (vectorR8[1] + vectorR8[5]) / 2f else 0f
        val proj2 = if (vectorR8.size >= 8) (vectorR8[2] + vectorR8[6]) / 2f else 0f
        val proj3 = if (vectorR8.size >= 8) (vectorR8[3] + vectorR8[7]) / 2f else 0f

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = "REDUCCIÓN CANTORIANA LATENTE (R⁴ PROYECTADO):",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val formattedProj = listOf(proj0, proj1, proj2, proj3)
                    formattedProj.forEachIndexed { i, p ->
                        Text(
                            text = "P$i: ${String.format("%.3f", p)}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryEmerald
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VectorComponentCell(
    index: Int,
    value: Float,
    modifier: Modifier = Modifier
) {
    val animValue by animateFloatAsState(
        targetValue = value.coerceIn(0f, 1f),
        animationSpec = tween(400),
        label = "animHeatValue"
    )

    val heatColor by animateColorAsState(
        targetValue = when {
            animValue > 0.75f -> AccentAmber
            animValue > 0.45f -> SecondaryEmerald
            else -> AccentCyan
        },
        animationSpec = tween(400),
        label = "animHeatColor"
    )

    Box(
        modifier = modifier
            .height(72.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .border(1.dp, heatColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "R$index",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )

                Text(
                    text = when {
                        animValue > 0.75f -> "HOT"
                        animValue > 0.45f -> "WARM"
                        else -> "COOL"
                    },
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = heatColor
                )
            }

            Text(
                text = String.format("%.3f", animValue),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // Hotness progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = animValue)
                        .height(4.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PrimaryBlue, heatColor)
                            )
                        )
                )
            }
        }
    }
}
