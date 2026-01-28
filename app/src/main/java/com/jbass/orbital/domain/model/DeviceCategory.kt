package com.jbass.orbital.domain.model

import kotlinx.serialization.Serializable

/**
 *So we can later group in the UI without fancy coding
 */
@Serializable
enum class DeviceCategory {
    LIGHTING,
    AUDIO,
    MEDIA,
    CLIMATE,
    SHADING,
    SECURITY,
    ENERGY,
    NETWORK,
    VIRTUAL
}
