package com.jbass.orbital.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
@Composable
fun ThermostatScreen() {
    val minAngle = -130f
    val maxAngle = 135f

    val minTemp = 10f
    val maxTemp = 40f
    val step = 0.5f

    var angle by remember { mutableStateOf(minAngle) }
    var lastSnappedTemp by remember { mutableStateOf(minTemp) }

    val haptics = LocalHapticFeedback.current

    val temperature = mapAngleToValue(
        angle,
        minAngle,
        maxAngle,
        minTemp,
        maxTemp
    )

    Box(
        contentAlignment = Alignment.Center
    ) {
        ThermostatKnob(
            angle = angle,
            minAngle = minAngle,
            maxAngle = maxAngle,
            onAngleChange = { rawAngle ->

                // 1️⃣ Angle → temperature
                val temp = mapAngleToValue(
                    rawAngle,
                    minAngle,
                    maxAngle,
                    minTemp,
                    maxTemp
                )

                // 2️⃣ Snap temperature
                val snappedTemp = snapValue(temp, step)

                // 3️⃣ Haptic tick only when snap changes
                if (snappedTemp != lastSnappedTemp) {
                    haptics.performHapticFeedback(
                        HapticFeedbackType.TextHandleMove
                    )
                    lastSnappedTemp = snappedTemp
                }

                // 4️⃣ Snap back to angle
                angle = mapValueToAngle(
                    snappedTemp,
                    minTemp,
                    maxTemp,
                    minAngle,
                    maxAngle
                )
            },
            modifier = Modifier.size(300.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${lastSnappedTemp.roundToInt()}°C",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${lastSnappedTemp.roundToInt()}°C",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun ThermostatView() {
    MaterialTheme {
        ThermostatScreen()
    }
}
