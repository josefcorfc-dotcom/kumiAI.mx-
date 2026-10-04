package com.example.kumiengine.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun AuditTerminal(
    logs: List<String>,
    sessionId: String,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "CryptAI", "GenAI", "TestAI"

    val filteredLogs = remember(logs, searchQuery, selectedFilter) {
        logs.filter { log ->
            val matchesFilter = when (selectedFilter) {
                "CryptAI" -> log.contains("CryptAI", ignoreCase = true) || log.contains("Crypt", ignoreCase = true) || log.contains("Entropía", ignoreCase = true)
                "GenAI" -> log.contains("GenAI", ignoreCase = true) || log.contains("Parche", ignoreCase = true) || log.contains("KUMI", ignoreCase = true)
                "TestAI" -> log.contains("TestAI", ignoreCase = true) || log.contains("Sandbox", ignoreCase = true) || log.contains("VIGILANDO", ignoreCase = true)
                else -> true
            }
            val matchesQuery = if (searchQuery.isBlank()) {
                true
            } else {
                log.contains(searchQuery, ignoreCase = true)
            }
            matchesFilter && matchesQuery
        }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    val jsonObject = JSONObject().apply {
                        put("session", sessionId)
                        put("timestamp", System.currentTimeMillis())
                        val logsArray = JSONArray()
                        filteredLogs.forEach { log -> logsArray.put(log) }
                        put("logs", logsArray)
                    }
                    outputStream.write(jsonObject.toString(4).toByteArray())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(filteredLogs.size) {
        if (filteredLogs.isNotEmpty()) {
            listState.animateScrollToItem(filteredLogs.size - 1)
        }
    }

    Column(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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

            IconButton(
                onClick = { exportLauncher.launch("aleph_logs_$sessionId.json") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download Logs",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search and Filter Input Area above the terminal log view
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = "Filtrar por palabra clave o alerta...",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Search",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecondaryEmerald,
                unfocusedBorderColor = BorderSubtle,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = SecondaryEmerald,
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Category Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val categories = listOf("ALL", "CryptAI", "GenAI", "TestAI")
            categories.forEach { cat ->
                val isSelected = selectedFilter == cat
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) SecondaryEmerald.copy(alpha = 0.2f) else SurfaceDark,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) SecondaryEmerald else BorderSubtle,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { selectedFilter = cat }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) SecondaryEmerald else TextMuted
                    )
                }
            }
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
                items(filteredLogs) { log ->
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
