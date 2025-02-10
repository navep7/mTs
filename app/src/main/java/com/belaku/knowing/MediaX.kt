package com.belaku.knowing

data class MediaX(
    val display_url: String,
    val expanded_url: String,
    val ext_media_availability: ExtMediaAvailability,
    val features: FeaturesX,
    val id_str: String,
    val indices: List<Int>,
    val media_key: String,
    val media_url_https: String,
    val original_info: OriginalInfo,
    val sizes: Sizes,
    val type: String,
    val url: String
)