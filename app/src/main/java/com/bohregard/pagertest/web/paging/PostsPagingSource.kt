package com.bohregard.pagertest.web.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bohregard.pagertest.extension.debug
import com.bohregard.pagertest.model.Post
import com.bohregard.pagertest.web.RedditApi
import java.lang.Exception

class PostsPagingSource(
    val redditApi: RedditApi
): PagingSource<String, Post>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        debug("Loading...${params.key}")
        try {
            val response = redditApi.getAllPosts(
                "r/all",
                "hot",
                params.key,
                null
            )

            return LoadResult.Page(
                data = response.posts,
                prevKey = null,
                nextKey = response.after,
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        debug("Refresh Key")
        return null
    }
}