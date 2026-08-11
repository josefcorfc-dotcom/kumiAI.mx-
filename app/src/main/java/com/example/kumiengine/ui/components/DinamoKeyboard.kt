package com.example.kumiengine.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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

@Composable
fun DinamoKeyboard(
    dinamoKeys: List<Int>,
    enteredPasscode: String,
    onKeyClick: (Int) -> Unit,
    onScrambleClick: () -> Unit,
    onClearClick: () -> Unit,
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
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lock",
                    tint = SecondaryEmerald,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "TECLADO DINAMO (CANTOR)",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            IconButton(
                onClick = onScrambleClick,
                modifier = Modifier.testTag("scramble_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Scramble",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Display area for entered sequence
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = if (enteredPasscode.isEmpty()) "SECUENCIA DINAMO..." else enteredPasscode,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                color = if (enteredPasscode.isEmpty()) TextMuted else SecondaryEmerald,
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5x2 Keypad Grid
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val rows = dinamoKeys.chunked(5)
            for (row in rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (digit in row) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .background(Color.Black, RoundedCornerShape(8.dp))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { onKeyClick(digit) }
                                .testTag("dinamo_key_$digit"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = digit.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Secuencia transfinitamente reordenada",
                fontSize = 9.sp,
                color = TextMuted,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "LIMPIAR",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .clickable { onClearClick() }
                    .padding(4.dp)
            )
        }
    }
}
