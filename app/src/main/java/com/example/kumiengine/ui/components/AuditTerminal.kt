package com.example.kumiengine.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kumiengine.ui.theme.BorderSubtle
import com.example.kumiengine.ui.theme.SecondaryEmerald
import com.example.kumiengine.ui.theme.SurfaceDark
import com.example.kumiengine.ui.theme.TextMuted
import com.example.kumiengine.ui.theme.TextPrimary

@Composable
fun AuditTerminal(
    logs: List<String>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Column(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Terminal,
                contentDescription = "Terminal",
                tint = SecondaryEmerald,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "LOG DE AUDITORÍA // SISTEMA CERRADO",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(logs) { log ->
                    Text(
                        text = log,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = SecondaryEmerald,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}
