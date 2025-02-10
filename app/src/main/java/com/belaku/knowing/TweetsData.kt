package com.belaku.knowing

data class TweetsData(
    val continuation_token: String,
    val results: List<Result>
)