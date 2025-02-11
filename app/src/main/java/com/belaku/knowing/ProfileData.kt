package com.belaku.knowing

data class ProfileData(
    val bot: Boolean,
    val category: Any,
    val creation_date: String,
    val default_profile: Boolean,
    val default_profile_image: Boolean,
    val description: String,
    val external_url: String,
    val favourites_count: Int,
    val follower_count: Int,
    val following_count: Int,
    val has_nft_avatar: Boolean,
    val is_blue_verified: Boolean,
    val is_private: Any,
    val is_verified: Boolean,
    val listed_count: Int,
    val location: String,
    val name: String,
    val number_of_tweets: Int,
    val profile_banner_url: String,
    val profile_pic_url: String,
    val timestamp: Int,
    val user_id: String,
    val username: String,
    val verified_type: Any
)