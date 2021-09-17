package com.bohregard.pagertest.web.model

import com.bohregard.pagertest.model.Post

data class PostResponse(
    val after: String?,
    val posts: List<Post>
)