package com.example.kumiengine.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kumiengine.ui.theme.BorderSubtle
import com.example.kumiengine.ui.theme.PrimaryBlue
import com.example.kumiengine.ui.theme.SecondaryEmerald
import com.example.kumiengine.ui.theme.SurfaceDark
import com.example.kumiengine.ui.theme.TextMuted
import com.example.kumiengine.ui.theme.TextPrimary
import com.example.kumiengine.viewmodel.TelemetryPoint

@Composable
fun TelemetryChart(
    telemetryData: List<TelemetryPoint>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(SurfaceDark, RoundedCornerShape(16.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = "Telemetry",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "MONITOR DE RESONANCIA AEA-97.5",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            val currentVal = telemetryData.lastOrNull()?.resonanceVal ?: 97.05f
            Text(
                text = String.format("%.3f%%", currentVal),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = SecondaryEmerald
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (telemetryData.size < 2) return@Canvas

                val width = size.width
                val height = size.height

                val minVal = 96.9f
                val maxVal = 97.2f

                val points = telemetryData.mapIndexed { index, item ->
                    val x = (index.toFloat() / (telemetryData.size - 1)) * width
                    val normalizedY = (item.resonanceVal - minVal) / (maxVal - minVal)
                    val y = height - (normalizedY * height)
                    Offset(x, y)
                }

                // Line path
                val path = Path()
                path.moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    path.lineTo(points[i].x, points[i].y)
                }

                // Fill gradient path
                val fillPath = Path()
                fillPath.addPath(path)
                fillPath.lineTo(points.last().x, height)
                fillPath.lineTo(points.first().x, height)
                fillPath.close()

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(PrimaryBlue.copy(alpha = 0.3f), Color.Transparent)
                    )
                )

                drawPath(
                    path = path,
                    color = PrimaryBlue,
                    style = Stroke(width = 4f)
                )

                // Highlight latest point
                val lastPt = points.last()
                drawCircle(
                    color = SecondaryEmerald,
                    radius = 6f,
                    center = lastPt
                )
            }
        }
    }
}
