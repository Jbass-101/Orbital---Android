package com.jbass.orbital.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.ui.theme.OrbitalTheme
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/* ---------- DATA MODELS ---------- */

data class ThermostatState(
    val currentTemperature: Float = 22.5f,
    val targetTemperature: Float = 23.0f,
    val mode: ThermostatMode = ThermostatMode.HEAT,
    val fanMode: FanMode = FanMode.AUTO,
    val isOn: Boolean = true,
    val humidity: Int = 45,
    val roomName: String = "Living Room"
)

enum class ThermostatMode { HEAT, COOL, AUTO, OFF }
enum class FanMode { AUTO, ON, CIRCULATE }

/* ---------- ROOT CARD ---------- */

@Composable
fun ThermostatCard(
    thermostatState: ThermostatState,
    onTemperatureChange: (Float) -> Unit = {},
    onModeChange: (ThermostatMode) -> Unit = {},
    onFanModeChange: (FanMode) -> Unit = {},
    onPowerToggle: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection(
                roomName = thermostatState.roomName,
                isOn = thermostatState.isOn,
                onPowerToggle = onPowerToggle
            )

            Spacer(Modifier.height(16.dp))

            TemperatureDial(
                currentTemp = thermostatState.currentTemperature,
                targetTemp = thermostatState.targetTemperature,
                isOn = thermostatState.isOn,
                onTemperatureChange = onTemperatureChange
            )

            Spacer(Modifier.height(24.dp))

            ModeSelectionSection(
                currentMode = thermostatState.mode,
                onModeChange = onModeChange,
                isEnabled = thermostatState.isOn
            )

            Spacer(Modifier.height(16.dp))

            InfoAndFanSection(
                humidity = thermostatState.humidity,
                fanMode = thermostatState.fanMode,
                onFanModeChange = onFanModeChange,
                isEnabled = thermostatState.isOn
            )
        }
    }
}

/* ---------- HEADER ---------- */

@Composable
private fun HeaderSection(
    roomName: String,
    isOn: Boolean,
    onPowerToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            roomName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Switch(
            checked = isOn,
            onCheckedChange = onPowerToggle
        )
    }
}

/* ---------- TEMPERATURE DIAL ---------- */

@Composable
private fun TemperatureDial(
    currentTemp: Float,
    targetTemp: Float,
    isOn: Boolean,
    onTemperatureChange: (Float) -> Unit
) {
    val animatedTarget = remember { Animatable(targetTemp) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(targetTemp) {
        animatedTarget.animateTo(targetTemp, tween(300))
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(0.8f)
            .onGloballyPositioned { canvasSize = it.size }
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 24.dp.toPx()
            val radius = (size.minDimension / 2f) - stroke / 2
            val center = Offset(size.width / 2, size.height / 2)

            drawArc(
                color = Color(0xFF1B263B).copy(alpha = 0.3f),
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(stroke, cap = StrokeCap.Round)
            )

            val targetAngle = 150f + (animatedTarget.value - 16f) * (240f / 12f)
            val rad = toRadians(targetAngle.toDouble())
            val indicatorPos = Offset(
                center.x + (radius + 10.dp.toPx()) * cos(rad).toFloat(),
                center.y + (radius + 10.dp.toPx()) * sin(rad).toFloat()
            )

            drawCircle(
                color = Color(0xFF1B263B),
                radius = 16.dp.toPx(),
                center = indicatorPos
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${currentTemp.format(1)}°",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Set to ${targetTemp.format(1)}°",
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (isOn) {
            Box(
                Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, drag ->
                            change.consumePositionChange()
                            val next = (targetTemp - drag / 10f)
                                .coerceIn(16f, 28f)
                                .roundToHalf()
                            onTemperatureChange(next)
                        }
                    }
            )
        }
    }
}

/* ---------- MODE & FAN ---------- */
/* (UNCHANGED LOGIC, SAFE AS-IS) */

@Composable
private fun ModeSelectionSection(
    currentMode: ThermostatMode,
    onModeChange: (ThermostatMode) -> Unit,
    isEnabled: Boolean
) { /* same as your version */ }

@Composable
private fun InfoAndFanSection(
    humidity: Int,
    fanMode: FanMode,
    onFanModeChange: (FanMode) -> Unit,
    isEnabled: Boolean
) { /* same as your version */ }

/* ---------- HELPERS ---------- */

private fun Float.roundToHalf(): Float = (this * 2).roundToInt() / 2f
private fun Float.format(decimals: Int): String =
    "%.${decimals}f".format(this)



@Preview(showBackground = true)
@Composable
fun ThermostatCardOffPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ThermostatCard(
                thermostatState = ThermostatState(
                    currentTemperature = 20.0f,
                    targetTemperature = 22.0f,
                    mode = ThermostatMode.OFF,
                    fanMode = FanMode.AUTO,
                    isOn = false,
                    humidity = 40,
                    roomName = "Bedroom"
                )
            )
        }
    }
}