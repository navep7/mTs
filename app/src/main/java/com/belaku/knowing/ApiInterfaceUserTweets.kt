package com.belaku.knowing

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/*{
        val request = Request.Builder()
            .url("https://twitter154.p.rapidapi.com/user/tweets?username=omarmhaimdat&limit=40&user_id=96479162&include_replies=false&include_pinned=false")
            .get()
            .addHeader("x-rapidapi-key", "9e92cc4f67msh8bb4ede93f53bf7p1ecb22jsn26ea5014a6df")
            .addHeader("x-rapidapi-host", "twitter154.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
    }*/
interface ApiInterfaceUserTweets {

    @Headers("x-rapidapi-key: 9e92cc4f67msh8bb4ede93f53bf7p1ecb22jsn26ea5014a6df",
        "x-rapidapi-host: twitter154.p.rapidapi.com")
    @GET("user/tweets")
    fun getTweets(@Query("username") query: String) : Call<TweetsData?>
}