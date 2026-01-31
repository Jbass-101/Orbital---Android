package com.jbass.orbital.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Grain
import androidx.compose.material.icons.rounded.Thunderstorm
import androidx.compose.material.icons.rounded.Umbrella
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.domain.model.weather.CurrentWeather
import com.jbass.orbital.domain.model.weather.WeatherCondition
import com.jbass.orbital.domain.model.weather.WeatherLocation
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun WeatherCard(currentWeather: CurrentWeather?) {

    val weatherVisuals = getWeatherVisuals(currentWeather?.condition)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
            )
    ){
        if(currentWeather == null){
            Text("Updating weather",
                Modifier.padding(20.dp), color = Color.White.copy(alpha = 0.5f))
        }else {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = weatherVisuals.icon,
                        contentDescription = "Weather",
                        modifier = Modifier.size(48.dp),
                        tint = weatherVisuals.color
                    )

                    Column() {
                        Text(
                            text = weatherVisuals.text,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Light,
                            color = Color.White
                        )
                        Text(
                            text = "${currentWeather.location.city},${currentWeather.location.country}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }

                    Text(
                        text = buildAnnotatedString {
                            append(String.format("%.0f", currentWeather.temperature))
                            withStyle(style = SpanStyle(
                                fontSize = 14.sp ,
                                baselineShift = BaselineShift.Superscript
                            )){
                                append("o")
                            }
                        },
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraLight,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    weatherCardStat("${currentWeather.humidity} %","Humidity")
                    weatherCardStat("${currentWeather.pressure} hPa","Pressure")
                    weatherCardStat("${(String.format("%.0f", currentWeather.windSpeed))} m/s","Wind Speed")
                    weatherCardStat("${currentWeather.visibility} m","Visibility")

                }
            }

        }

    }
}

@Composable
fun weatherCardStat(data: String, title: String){
    Column {
        Text(
            text = data,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraLight,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

// Data class to hold our theme-specific visuals
data class WeatherVisuals(val icon: ImageVector, val color: Color, val text: String)

@Composable
private fun getWeatherVisuals(condition: WeatherCondition?): WeatherVisuals {
    return when (condition) {
        WeatherCondition.CLEAR -> WeatherVisuals(Icons.Rounded.WbSunny, Color(0xFFFFD60A),"Clear")
        WeatherCondition.RAIN -> WeatherVisuals(Icons.Rounded.Umbrella, Color(0xFF64D2FF),"Rain")
        WeatherCondition.CLOUDS -> WeatherVisuals(Icons.Rounded.Cloud, Color(0xFFEBEBF5),"Clouds")
        WeatherCondition.SNOW -> WeatherVisuals(Icons.Rounded.AcUnit, Color.White,"Snow")
        WeatherCondition.THUNDERSTORM -> WeatherVisuals(Icons.Rounded.Thunderstorm, Color(0xFFBF5AF2),"Thunderstorm")
        WeatherCondition.DRIZZLE -> WeatherVisuals(Icons.Rounded.Grain, Color(0xFF64D2FF),"Drizzle")
        null -> WeatherVisuals(Icons.Rounded.Cloud, Color.Gray,"")
    }
}


val mockTemperatureData =
    CurrentWeather(
        23.0095579034852,
        15,
        45,
        65.2,
        10,
        WeatherCondition.CLEAR,
        location = WeatherLocation(
            "Cape Town",
            "South Africa"
        )

    )


@Preview(name = "Weather Gallery", group = "System", showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun PreviewWeatherGallery() {
    OrbitalTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(Color(0xFF0A0A0A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Weather Visual System", color = Color.White, style = MaterialTheme.typography.titleLarge)

            WeatherCondition.entries.forEach { condition ->
                WeatherCard(mockTemperatureData.copy(condition = condition))
            }
        }
    }
}
