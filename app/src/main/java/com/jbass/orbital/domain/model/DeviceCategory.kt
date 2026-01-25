package com.jbass.orbital.domain.model

import kotlinx.serialization.Serializable

/**
 *So we can later group in the UI without fancy coding
 */
@Serializable
enum class DeviceCategory {
    LIGHTING,
    CLIMATE,
    MEDIA,
    AUDIO,
    SECURITY,
    SHADING,
    ENERGY,
    NETWORK,
    VIRTUAL
}
