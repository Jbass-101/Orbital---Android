package com.jbass.orbital.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ClimateControlState(
    val location: String = "Upstairs",
    val deviceName: String = "AC control",
    val mode: ClimateMode = ClimateMode.COOL,
    val targetTemperature: Float = 20.0f,
    val humidity: Int = 35,
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val fanSpeed: FanSpeed = FanSpeed.AUTO,
    val scheduleEnabled: Boolean = false,
    val timerEnabled: Boolean = false
)

enum class ClimateMode {
    COOL, HEAT, FAN, AUTO, DRY
}

enum class FanSpeed {
    AUTO, LOW, MEDIUM, HIGH
}

enum class TemperatureUnit {
    CELSIUS, FAHRENHEIT
}

@Composable
fun ClimateControlCard(
    state: ClimateControlState,
    onTemperatureChange: (Float) -> Unit = {},
    onModeChange: (ClimateMode) -> Unit = {},
    onFanChange: (FanSpeed) -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onTimerClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            HeaderSection(
                location = state.location,
                deviceName = state.deviceName
            )

            Spacer(modifier = Modifier.height(32.dp))

            ThermostatScreen()

            // Temperature Control
//            TemperatureControlSection(
//                mode = state.mode,
//                targetTemperature = state.targetTemperature,
//                temperatureUnit = state.temperatureUnit,
//                onTemperatureChange = onTemperatureChange
//            )

            Spacer(modifier = Modifier.height(40.dp))

            // Humidity and Status
            StatusSection(
                humidity = state.humidity,
                fanSpeed = state.fanSpeed
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Control Buttons
            ControlButtonsSection(
                onModeClick = { onModeChange(state.mode) },
                onFanClick = { onFanChange(state.fanSpeed) },
                onScheduleClick = onScheduleClick,
                onTimerClick = onTimerClick,
                scheduleEnabled = state.scheduleEnabled,
                timerEnabled = state.timerEnabled
            )
        }
    }
}

@Composable
private fun HeaderSection(
    location: String,
    deviceName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = location,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Color(0xFFAAAAAA)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = deviceName,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.White
        )
    }
}

@Composable
private fun TemperatureControlSection(
    mode: ClimateMode,
    targetTemperature: Float,
    temperatureUnit: TemperatureUnit,
    onTemperatureChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mode label
        Text(
            text = when (mode) {
                ClimateMode.COOL -> "Cool to"
                ClimateMode.HEAT -> "Heat to"
                ClimateMode.FAN -> "Fan only"
                ClimateMode.AUTO -> "Auto"
                ClimateMode.DRY -> "Dry"
            },
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = when (mode) {
                ClimateMode.COOL -> Color(0xFF4FC3F7)
                ClimateMode.HEAT -> Color(0xFFFF7043)
                ClimateMode.FAN -> Color(0xFF81C784)
                ClimateMode.AUTO -> Color(0xFF9575CD)
                ClimateMode.DRY -> Color(0xFF64B5F6)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Temperature display with circular control
        Box(
            modifier = Modifier
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background circle
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color(0xFF1E1E1E),
                    radius = size.minDimension / 2
                )
            }

            // Temperature ring
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 12.dp.toPx()
                val radius = (size.minDimension / 2) - strokeWidth / 2
                val center = Offset(size.width / 2, size.height / 2)

                // Background ring
                drawCircle(
                    color = Color(0xFF2D2D2D),
                    center = center,
                    radius = radius,
                    style = Stroke(strokeWidth)
                )

                // Active ring based on temperature
                val maxAngle = 300f
                val progress = (targetTemperature - 16f) / (30f - 16f)
                val sweepAngle = maxAngle * progress

                drawArc(
                    color = when (mode) {
                        ClimateMode.COOL -> Color(0xFF00BCD4)
                        ClimateMode.HEAT -> Color(0xFFFF5722)
                        else -> Color(0xFF4CAF50)
                    },
                    startAngle = 130f,
                    sweepAngle = 30f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(strokeWidth, cap = StrokeCap.Round)
                )


                drawArc(
                    color = Color(0xFFFF0000),
                    startAngle = 160f,
                    sweepAngle = 5f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(30f, cap = StrokeCap.Round)
                )

                // Temperature markers
                for (i in 16..30 step 2) {
                    val angle = 120f + (i - 16) * (maxAngle / 14)
                    val markerRadius = radius - strokeWidth / 2

                    val startAngle = Math.toRadians(angle.toDouble())
                    val x1 = center.x + (markerRadius - 8.dp.toPx()) * Math.cos(startAngle).toFloat()
                    val y1 = center.y + (markerRadius - 8.dp.toPx()) * Math.sin(startAngle).toFloat()
                    val x2 = center.x + markerRadius * Math.cos(startAngle).toFloat()
                    val y2 = center.y + markerRadius * Math.sin(startAngle).toFloat()

                    drawLine(
                        color = Color(0xFF555555),
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            // Temperature display
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = targetTemperature.format(1),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Text(
                    text = "°${if (temperatureUnit == TemperatureUnit.CELSIUS) "C" else "F"}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFFAAAAAA)
                )
            }

            // Temperature adjustment buttons
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Increase button
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2A2A2A))
                        .clickable { onTemperatureChange(targetTemperature + 0.5f) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }

                // Decrease button
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2A2A2A))
                        .clickable { onTemperatureChange(targetTemperature - 0.5f) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusSection(
    humidity: Int,
    fanSpeed: FanSpeed,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Humidity Indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularIndicator(
                value = humidity,
                maxValue = 100,
                label = "Humidity",
                unit = "%",
                color = Color(0xFF4FC3F7)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$humidity%",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
        }

        // Fan Speed Indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularIndicator(
                value = when (fanSpeed) {
                    FanSpeed.AUTO -> 25
                    FanSpeed.LOW -> 50
                    FanSpeed.MEDIUM -> 75
                    FanSpeed.HIGH -> 100
                },
                maxValue = 100,
                label = "Fan",
                unit = "",
                color = Color(0xFF81C784)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (fanSpeed) {
                    FanSpeed.AUTO -> "Auto"
                    FanSpeed.LOW -> "Low"
                    FanSpeed.MEDIUM -> "Medium"
                    FanSpeed.HIGH -> "High"
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
        }
    }
}

@Composable
private fun CircularIndicator(
    value: Int,
    maxValue: Int,
    label: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .drawBehind {
                val strokeWidth = 6.dp.toPx()
                val radius = (size.minDimension / 2) - strokeWidth / 2
                val center = Offset(size.width / 2, size.height / 2)

                // Background circle
                drawCircle(
                    color = Color(0xFF2D2D2D),
                    center = center,
                    radius = radius,
                    style = Stroke(strokeWidth)
                )

                // Progress circle
                val sweepAngle = 360f * (value.toFloat() / maxValue)
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(strokeWidth, cap = StrokeCap.Round)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp
                ),
                color = Color(0xFFAAAAAA)
            )

            Text(
                text = "$value$unit",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
        }
    }
}

@Composable
private fun ControlButtonsSection(
    onModeClick: () -> Unit,
    onFanClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onTimerClick: () -> Unit,
    scheduleEnabled: Boolean,
    timerEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ControlButton(
            icon = Icons.Default.ThumbUp,
            label = "Mode",
            onClick = onModeClick,
            isActive = true
        )

        ControlButton(
            icon = Icons.Default.ThumbUp,
            label = "Fan",
            onClick = onFanClick
        )

        ControlButton(
            icon = Icons.Default.ThumbUp,
            label = "Schedule",
            onClick = onScheduleClick,
            isActive = scheduleEnabled
        )

        ControlButton(
            icon = Icons.Default.ThumbUp,
            label = "Timer",
            onClick = onTimerClick,
            isActive = timerEnabled
        )
    }
}

@Composable
private fun ControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    isActive: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    if (isActive) Color(0xFF2A2A2A) else Color(0xFF1A1A1A)
                )
                .border(
                    width = if (isActive) 2.dp else 0.dp,
                    color = if (isActive) Color(0xFF4FC3F7) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = if (isActive) Color(0xFF4FC3F7) else Color(0xFFAAAAAA)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 12.sp
            ),
            color = if (isActive) Color(0xFF4FC3F7) else Color(0xFFAAAAAA)
        )
    }
}

private fun Float.format(decimals: Int): String = "%.${decimals}f".format(this)

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun ClimateControlCardPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ClimateControlCard(
                state = ClimateControlState(
                    location = "Upstairs",
                    deviceName = "AC control",
                    mode = ClimateMode.COOL,
                    targetTemperature = 20.0f,
                    humidity = 35,
                    fanSpeed = FanSpeed.AUTO,
                    scheduleEnabled = false,
                    timerEnabled = false
                ),
                onTemperatureChange = { println("Temperature: $it") },
                onModeChange = { println("Mode: $it") },
                onFanChange = { println("Fan: $it") },
                onScheduleClick = { println("Schedule clicked") },
                onTimerClick = { println("Timer clicked") }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun ClimateControlCardHeatPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ClimateControlCard(
                state = ClimateControlState(
                    location = "Living Room",
                    deviceName = "Heat control",
                    mode = ClimateMode.HEAT,
                    targetTemperature = 24.5f,
                    humidity = 22,
                    fanSpeed = FanSpeed.MEDIUM,
                    scheduleEnabled = true,
                    timerEnabled = true
                )
            )
        }
    }
}