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

    companion object {
        private var _lastFetchedPosts = listOf<Post>()
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        debug("Loading...${params.key}")
        try {
            val response = redditApi.getAllPosts(
                "r/all",
                "hot",
                params.key,
                null
            )

            val loadResult = LoadResult.Page(
                data = response.posts,
                prevKey = params.key,
                nextKey = response.after,
            )
            _lastFetchedPosts = response.posts
            return loadResult
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        debug("Refresh Key")
        return null
    }
}