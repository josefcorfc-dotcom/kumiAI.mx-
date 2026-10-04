import
    <a
    id="cy-effective-orcid-url"
    class="underline"
     href="https://orcid.org/0009-0007-6963-1205"
     target="orcid.widget"
     rel="me noopener noreferrer"
     style="vertical-align: top">
     <img
        src="https://orcid.org/sites/default/files/images/orcid_16x16.png"
        style="width: 1em; margin-inline-start: 0.5em"
        alt="ORCID iD icon"/>
      https://orcid.org/0009-0007-6963-1205
    </a>package com.example.kumiengine.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kumiengine.ui.theme.AccentCyan
import com.example.kumiengine.ui.theme.BorderSubtle
import com.example.kumiengine.ui.theme.PrimaryBlue
import com.example.kumiengine.ui.theme.SecondaryEmerald
import com.example.kumiengine.ui.theme.SurfaceDark
import com.example.kumiengine.ui.theme.TextMuted
import com.example.kumiengine.ui.theme.TextPrimary
import kotlin.math.atan2

@Composable
fun CartesianPlaneView(
    point: Pair<Long, Long>,
    phase: Int,
    modifier: Modifier = Modifier
) {
    val animX by animateFloatAsState(targetValue = point.first.toFloat(), animationSpec = tween(500), label = "animX")
    val animY by animateFloatAsState(targetValue = point.second.toFloat(), animationSpec = tween(500), label = "animY")

    Box(
        modifier = modifier
            .background(SurfaceDark, RoundedCornerShape(16.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f
            val centerY = height / 2f

            val dashPathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)

            // Draw Quadrant grid
            // Vertical axis (Y-axis)
            drawLine(
                color = BorderSubtle,
                start = Offset(centerX, 0f),
                end = Offset(centerX, height),
                strokeWidth = 2f
            )

            // Horizontal axis (X-axis)
            drawLine(
                color = BorderSubtle,
                start = Offset(0f, centerY),
                end = Offset(width, centerY),
                strokeWidth = 2f
            )

            // Grid lines
            for (i in -3..3) {
                if (i != 0) {
                    val x = centerX + i * (width / 8f)
                    val y = centerY + i * (height / 8f)

                    drawLine(
                        color = Color.White.copy(alpha = 0.05f),
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        pathEffect = dashPathEffect
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.05f),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        pathEffect = dashPathEffect
                    )
                }
            }

            // Map mathematical point (x, y) to Canvas coordinates
            val scale = (width / 300f).coerceAtLeast(1f)
            val canvasX = centerX + animX * scale
            val canvasY = centerY - animY * scale // Invert Y for screen coordinates

            // Draw vector from origin to point
            drawLine(
                color = PrimaryBlue,
                start = Offset(centerX, centerY),
                end = Offset(canvasX, canvasY),
                strokeWidth = 3f
            )

            // Draw Point Outer Glow & Dot
            drawCircle(
                color = SecondaryEmerald.copy(alpha = 0.3f),
                radius = 16f,
                center = Offset(canvasX, canvasY)
            )
            drawCircle(
                color = SecondaryEmerald,
                radius = 8f,
                center = Offset(canvasX, canvasY)
            )
        }

        // Quadrant Labels
        Text(
            text = "CUADRANTE I (+x,+y)",
            color = if (phase == 0) AccentCyan else TextMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (phase == 0) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        Text(
            text = "CUADRANTE II (-x,+y)",
            color = if (phase == 1) AccentCyan else TextMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (phase == 1) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Text(
            text = "CUADRANTE III (-x,-y)",
            color = if (phase == 2) AccentCyan else TextMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (phase == 2) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.align(Alignment.BottomStart)
        )
        Text(
            text = "CUADRANTE IV (+x,-y)",
            color = if (phase == 3) AccentCyan else TextMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (phase == 3) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}
