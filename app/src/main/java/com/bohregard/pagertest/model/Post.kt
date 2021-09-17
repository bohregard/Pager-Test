package com.bohregard.pagertest.model

import java.time.LocalDateTime

data class Post(
    val after: String?,
    val author: String,
    val created: LocalDateTime,
    val crossPost: Post? = null,
    val commentCount: Int,
    val domain: String,
    val fullyQualifiedName: String,
    val id: String,
    val isArchived: Boolean,
    val isHidden: Boolean,
    val isLocked: Boolean,
    val isNsfw: Boolean,
    val isOriginalContent: Boolean,
    val permalink: String,
    val selfText: String? = null,
    val subreddit: String,
    val thumbnail: String? = null,
    val title: String,
    val updoots: Int,
    val uri: String,
)