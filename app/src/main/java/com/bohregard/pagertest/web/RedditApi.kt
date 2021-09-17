package com.bohregard.pagertest.web

import com.bohregard.pagertest.web.model.PostResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApi {
    @GET("{subreddit}/{filter}?raw_json=1&limit=5")
    suspend fun getAllPosts(
        @Path("subreddit") subreddit: String,
        @Path("filter") filter: String,
        @Query("after") after: String? = null,
        @Query("t") time: String? = null
    ): PostResponse
}