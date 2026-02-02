package com.jbass.orbital.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.jbass.orbital.domain.model.device.DeviceType

/**
 * Maps Domain DeviceTypes to Material Design Icons.
 * Using Icons.Rounded to match the luxury/soft aesthetic of the theme.
 */
fun DeviceType.getIcon(): ImageVector {
    return when (this) {
        // Lighting
        DeviceType.LIGHT -> Icons.Rounded.Lightbulb
        DeviceType.DIMMER -> Icons.Rounded.BrightnessMedium
        DeviceType.RGB_LIGHT -> Icons.Rounded.Palette

        // Climate
        DeviceType.THERMOSTAT -> Icons.Rounded.Thermostat
        DeviceType.HVAC -> Icons.Rounded.Air
        DeviceType.FAN -> Icons.Rounded.Toys // Best representation for a fan blade

        // Media & AV
        DeviceType.TV -> Icons.Rounded.Tv
        DeviceType.AVR -> Icons.Rounded.SettingsInputComponent
        DeviceType.SOUNDBAR -> Icons.Rounded.SpeakerGroup
        DeviceType.PROJECTOR -> Icons.Rounded.Videocam
        DeviceType.MEDIA_PLAYER -> Icons.Rounded.PlayCircle

        // Audio Zones
        DeviceType.AUDIO_ZONE -> Icons.Rounded.Radio
        DeviceType.SPEAKER -> Icons.Rounded.Speaker

        // Security & Access
        DeviceType.DOOR_LOCK -> Icons.Rounded.Lock
        DeviceType.GARAGE_DOOR -> Icons.Rounded.Garage
        DeviceType.CAMERA -> Icons.Rounded.Videocam
        DeviceType.MOTION_SENSOR -> Icons.Rounded.Sensors

        // Shading
        DeviceType.BLIND -> Icons.Rounded.Blinds
        DeviceType.CURTAIN -> Icons.Rounded.Curtains

        // Energy & Power
        DeviceType.SMART_PLUG -> Icons.Rounded.Power
        DeviceType.ENERGY_METER -> Icons.Rounded.ElectricBolt
        DeviceType.UPS -> Icons.Rounded.BatteryChargingFull

        // Network & Infrastructure
        DeviceType.NETWORK_SWITCH -> Icons.Rounded.Lan
        DeviceType.ACCESS_POINT -> Icons.Rounded.Router
        DeviceType.CONTROLLER -> Icons.Rounded.Memory

        // Virtual / Automation
        DeviceType.SCENE -> Icons.Rounded.AutoAwesome
        DeviceType.VIRTUAL_BUTTON -> Icons.Rounded.RadioButtonChecked
        DeviceType.SYSTEM_MONITOR -> Icons.Rounded.MonitorHeart
    }
}