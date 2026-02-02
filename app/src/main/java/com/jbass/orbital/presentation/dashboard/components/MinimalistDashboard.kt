package com.jbass.orbital.presentation.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.presentation.components.WeatherCard
import com.jbass.orbital.presentation.components.mockTemperatureData
import com.jbass.orbital.presentation.dashboard.DashboardUiState
import com.jbass.orbital.ui.theme.OrbitalTheme


@Composable
fun MinimalistDashboard(
    state: DashboardUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(70.dp))

        // 1. The Weather Hero
        WeatherCard(state.weather)

        Spacer(modifier = Modifier.height(40.dp))

        // 2. The "Live Status" Section (Hero Section)
        Text(
            text = "HOME STATUS",
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.6f)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LiveStatusSection(state.devices)
    }
}


@Preview
@Composable
fun previewMinimum(){
    OrbitalTheme() {
        DashboardBackground() {
            MinimalistDashboard(
                state = DashboardUiState(
                    categories =
                        listOf(DeviceCategory.SECURITY, DeviceCategory.LIGHTING),
                    selectedCategory = null,
                    weather = mockTemperatureData
                ),)
        }

    }
}