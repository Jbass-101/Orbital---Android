package com.jbass.orbital.presentation.dashboard.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.domain.model.device.DeviceState
import com.jbass.orbital.domain.model.device.DeviceType
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.util.getIcon


@Composable
fun LiveStatusSection(devices: List<SmartDevice>) {
    //@Todo move to viewmodel
    val activeLights = devices.count { it.type.category == DeviceCategory.LIGHTING && (it.state as? DeviceState.OnOff)?.isOn == true }
    val currentTemp = (devices.find { it.type.category == DeviceCategory.CLIMATE }?.state as? DeviceState.Temperature)?.current ?: 21

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StatusHeroItem(
            title = "Lighting",
            status = if (activeLights > 0) "$activeLights lights are currently on" else "All lights are off",
            icon = DeviceType.LIGHT.getIcon(),
            isActive = activeLights > 0
        )

        StatusHeroItem(
            title = "Climate",
            status = "Indoor average is $currentTemp°C",
            icon = DeviceType.THERMOSTAT.getIcon(),
            isActive = true
        )

        StatusHeroItem(
            title = "Security",
            status = "System Armed • All doors locked",
            icon = Icons.Rounded.Security,
            isActive = true
        )
    }
}