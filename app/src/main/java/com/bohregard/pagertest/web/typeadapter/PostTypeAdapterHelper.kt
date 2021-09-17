package com.bohregard.pagertest.web.typeadapter

import com.bohregard.pagertest.model.Post

import com.bohregard.pagertest.extension.nextStringOrNull
import com.bohregard.pagertest.extension.readArray
import com.bohregard.pagertest.extension.readObject
import com.squareup.moshi.JsonReader
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Suppress("UNCHECKED_CAST")
object PostTypeAdapterHelper {

    fun parsePost(after: String?, reader: JsonReader): Post {
        var crossPost: Post? = null

        var author = ""
        var commentCount = 0
        var created = LocalDateTime.now()
        var domain = ""
        var hidden = false
        var id = ""
        var isArchived = false
        var isLocked = false
        var isNsfw = false
        var isOriginalContent = false
        var permalink = ""
        var selfText: String? = null
        var subreddit = ""
        var thumbnail = ""
        var title = ""
        var updoots = 0
        var url = ""

        // Link Flair
        val linkFlairMap = mutableMapOf<String, Any>()

        reader.readObject {
            when (nextName()) {
                "archived" -> isArchived = nextBoolean()
                "author" -> author = nextString()
                "created_utc" -> {
                    val utcTime = nextLong()
                    created = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(utcTime),
                        ZoneOffset.UTC
                    )
                }
                "crosspost_parent_list" -> {
                    readArray {
                        crossPost = parsePost(after, this)
                    }
                }
                "domain" -> domain = nextString()
                "hidden" -> hidden = nextBoolean()
                "id" -> id = nextString()
                "is_original_content" -> isOriginalContent = nextBoolean()
                "link_flair_background_color" -> linkFlairMap["bgColor"] = nextString()
                "link_flair_text" -> if (peek() == JsonReader.Token.NULL) skipValue() else linkFlairMap["text"] =
                    nextString()
                "link_flair_text_color" -> linkFlairMap["textColor"] = nextString()
                "link_flair_type" -> linkFlairMap["type"] = nextString()
                "locked" -> isLocked = nextBoolean()
                "num_comments" -> commentCount = nextInt()
                "over_18" -> isNsfw = nextBoolean()
                "permalink" -> permalink = nextString()
                "score" -> updoots = nextInt()
                "selftext" -> selfText = nextStringOrNull()
                "subreddit" -> subreddit = nextString()
                "thumbnail" -> thumbnail = nextString()
                "title" -> title = nextString()
                "url" -> url = nextString()
                else -> skipValue()
            }
        }

        // There are some default links we can add for images
        // self: https://www.reddit.com/static/self_default2.png
        // default: https://www.reddit.com/static/noimage.png
        // nsfw: https://www.reddit.com/static/nsfw2.png


        @Suppress("DEPRECATION")
        return Post(
            after = after,
            author = author,
            commentCount = commentCount,
            created = created,
            crossPost = crossPost,
            domain = domain,
            fullyQualifiedName = "r/${subreddit}",
            id = id,
            isArchived = isArchived,
            isHidden = hidden,
            isLocked = isLocked,
            isNsfw = isNsfw,
            isOriginalContent = isOriginalContent,
            permalink = permalink,
            selfText = selfText?.replace("&#39;", "'"),
            subreddit = subreddit,
            thumbnail = thumbnail,
            title = title,
            updoots = updoots,
            uri = url,
        )
    }
}
