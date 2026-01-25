package com.jbass.orbital.domain.model


import kotlinx.serialization.Serializable


/**
 * A list of common devices in their category and flag for if it can be added to a scene
 */
@Serializable
enum class DeviceType(
    val category: DeviceCategory,
    val supportsScenes: Boolean
) {
    // Lighting
    LIGHT(DeviceCategory.LIGHTING, true),
    DIMMER(DeviceCategory.LIGHTING, true),
    RGB_LIGHT(DeviceCategory.LIGHTING, true),

    // Climate
    THERMOSTAT(DeviceCategory.CLIMATE, true),
    HVAC(DeviceCategory.CLIMATE, true),
    FAN(DeviceCategory.CLIMATE, false),

    // Media & AV
    TV(DeviceCategory.MEDIA, true),
    AVR(DeviceCategory.MEDIA, true),
    SOUNDBAR(DeviceCategory.MEDIA, true),
    PROJECTOR(DeviceCategory.MEDIA, true),
    MEDIA_PLAYER(DeviceCategory.MEDIA, true),

    // Audio Zones
    AUDIO_ZONE(DeviceCategory.AUDIO, true),
    SPEAKER(DeviceCategory.AUDIO, true),

    // Security & Access
    DOOR_LOCK(DeviceCategory.SECURITY, false),
    GARAGE_DOOR(DeviceCategory.SECURITY, false),
    CAMERA(DeviceCategory.SECURITY, false),
    MOTION_SENSOR(DeviceCategory.SECURITY, false),

    // Shading
    BLIND(DeviceCategory.SHADING, true),
    CURTAIN(DeviceCategory.SHADING, true),

    // Energy & Power
    SMART_PLUG(DeviceCategory.ENERGY, true),
    ENERGY_METER(DeviceCategory.ENERGY, false),
    UPS(DeviceCategory.ENERGY, false),

    // Network & Infrastructure
    NETWORK_SWITCH(DeviceCategory.NETWORK, false),
    ACCESS_POINT(DeviceCategory.NETWORK, false),
    CONTROLLER(DeviceCategory.NETWORK, false),

    // Virtual / Automation
    SCENE(DeviceCategory.VIRTUAL, false),
    VIRTUAL_BUTTON(DeviceCategory.VIRTUAL, false),
    SYSTEM_MONITOR(DeviceCategory.VIRTUAL, false)
}
