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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.kumiengine.ui.theme.PrimaryBlue
import com.example.kumiengine.ui.theme.SecondaryEmerald
import com.example.kumiengine.ui.theme.SurfaceDark
import com.example.kumiengine.ui.theme.TextMuted
import com.example.kumiengine.ui.theme.TextPrimary
import com.example.kumiengine.viewmodel.KumiViewModel

@Composable
fun MainScreen(viewModel: KumiViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .border(1.dp, BorderSubtle)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Memory,
                            contentDescription = "CPU",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = "Kumi AI // NeuroBINAleph-Σ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "MX-SQ-3000",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryEmerald,
                        modifier = Modifier
                            .background(Color.Black, RoundedCornerShape(6.dp))
                            .border(1.dp, SecondaryEmerald, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "NODO MX-SQ-3000 | INTEGRIDAD CANTORIANA: ℵ₁ | RFC: CALF8712186T5",
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                contentColor = TextPrimary,
                modifier = Modifier.border(1.dp, BorderSubtle)
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Telemetría") },
                    label = { Text("Telemetría", fontFamily = FontFamily.Monospace, fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        indicatorColor = SurfaceDark,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_telemetry")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Lock, contentDescription = "Cifrado") },
                    label = { Text("Cifrado", fontFamily = FontFamily.Monospace, fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        indicatorColor = SurfaceDark,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_crypto")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Security, contentDescription = "Test Suite") },
                    label = { Text("Test Suite", fontFamily = FontFamily.Monospace, fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        indicatorColor = SurfaceDark,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_test_suite")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> TelemetryScreen(viewModel = viewModel)
                1 -> CryptoConsoleScreen(viewModel = viewModel)
                2 -> TestSuiteScreen(viewModel = viewModel)
            }
        }
    }
}
