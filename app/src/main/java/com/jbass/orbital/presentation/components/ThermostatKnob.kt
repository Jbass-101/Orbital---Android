package com.jbass.orbital.presentation.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.toOffset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun ThermostatKnob(
    angle: Float,
    onAngleChange: (Float) -> Unit,
    minAngle: Float,
    maxAngle: Float,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val center = size.center.toOffset()
                    val touchAngle = angleFromCenter(center, change.position)

                    val distanceFromCenter =
                        (change.position - center).getDistance()

                    // Ignore touches too close to the center
                    if (distanceFromCenter > size.height * 0.25f) {
                        onAngleChange(
                            touchAngle.coerceIn(minAngle, maxAngle)
                        )
                    }
                }
            }
    ) {
        val center = size.center
        val radius = size.minDimension / 2.3f

        // Background arc
        drawArc(
            color = Color.DarkGray,
            startAngle = minAngle,
            sweepAngle = maxAngle - minAngle,
            useCenter = false,
            style = Stroke(width = 24f, cap = StrokeCap.Round),
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )

        // Active arc
        drawArc(
            color = Color(0xFF4CAF50),
            startAngle = minAngle,
            sweepAngle = angle - minAngle,
            useCenter = false,
            style = Stroke(width = 24f, cap = StrokeCap.Round),
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )

        // Knob position
        val knobPosition = positionOnCircle(center, radius, angle)

        drawCircle(
            color = Color.White,
            radius = 18f,
            center = knobPosition
        )
    }
}

fun angleFromCenter(center: Offset, touch: Offset): Float {
    val dx = touch.x - center.x
    val dy = touch.y - center.y
    return Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
}

fun positionOnCircle(
    center: Offset,
    radius: Float,
    angleDegrees: Float
): Offset {
    val radians = Math.toRadians(angleDegrees.toDouble())
    return Offset(
        x = center.x + cos(radians).toFloat() * radius,
        y = center.y + sin(radians).toFloat() * radius
    )
}

fun mapAngleToValue(
    angle: Float,
    minAngle: Float,
    maxAngle: Float,
    minValue: Float,
    maxValue: Float
): Float {
    val fraction = (angle - minAngle) / (maxAngle - minAngle)
    return minValue + fraction * (maxValue - minValue)
}



fun mapValueToAngle(
    value: Float,
    minValue: Float,
    maxValue: Float,
    minAngle: Float,
    maxAngle: Float
): Float {
    val fraction = (value - minValue) / (maxValue - minValue)
    return minAngle + fraction * (maxAngle - minAngle)
}

fun snapValue(value: Float, step: Float): Float {
    return (value / step).roundToInt() * step
}