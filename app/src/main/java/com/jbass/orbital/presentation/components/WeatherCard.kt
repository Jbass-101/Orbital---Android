package com.jbass.orbital.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.domain.model.weather.CurrentWeather
import com.jbass.orbital.domain.model.weather.WeatherCondition
import com.jbass.orbital.domain.model.weather.WeatherLocation
import com.jbass.orbital.ui.theme.OrbitalTheme
import java.util.Locale

@Composable
fun WeatherCard(currentWeather: CurrentWeather?) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
//            .graphicsLayer { alpha = 0.5f },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        if(currentWeather == null){
            Text("....loading")
        }else {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Weather",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Column {
                        Text(
                            text = getWeatherConditionText(currentWeather.condition),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${currentWeather.location.city},${currentWeather.location.country}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Text(
                        text = buildAnnotatedString {
                            append(formattedDataText(currentWeather.temperature))
                            withStyle(style = SpanStyle(
                                fontSize = 12.sp ,
                                baselineShift = BaselineShift.Superscript
                            )){
                                append(" o")
                            }
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${currentWeather.humidity} %",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Humidity",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Column {
                        Text(
                            text = "${currentWeather.pressure} hPa",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pressure",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Column {
                        Text(
                            text = "${formattedDataText(currentWeather.windSpeed)} m/s",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Wind Speed",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Column {
                        Text(
                            text = "${currentWeather.visibility} m",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Visibility",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

        }

    }
}


private fun getWeatherConditionText(condition: WeatherCondition) : String {
    return when(condition){
        WeatherCondition.CLEAR -> "Clear"
        WeatherCondition.RAIN -> "Rain"
        WeatherCondition.CLOUDS -> "Clouds"
        WeatherCondition.SNOW -> "Snow"
        WeatherCondition.THUNDERSTORM -> "Thunderstorm"
        WeatherCondition.DRIZZLE -> "Drizzle"

    }
}


private fun formattedDataText(value: Double) :String {
    return String.format(Locale.getDefault(), "%.2f", value)

}



@PreviewLightDark
@Composable
fun PreviewWeatherCard (){
    OrbitalTheme() {
        WeatherCard(
            mockTemperatureData
        )
    }
}

val mockTemperatureData =
    CurrentWeather(
        23.02,
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
