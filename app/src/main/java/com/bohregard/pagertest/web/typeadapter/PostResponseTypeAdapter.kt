package com.bohregard.pagertest.web.typeadapter

import com.bohregard.pagertest.extension.findAfter
import com.bohregard.pagertest.extension.readArray
import com.bohregard.pagertest.extension.readObject
import com.bohregard.pagertest.extension.readObjectByName
import com.bohregard.pagertest.model.Post
import com.bohregard.pagertest.web.model.PostResponse
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson

class PostResponseTypeAdapter {

    private val posts = mutableListOf<Post>()
    private var after: String? = null

    private val options: JsonReader.Options = JsonReader.Options.of("kind", "data")

    @Suppress("unused", "UNUSED_PARAMETER")
    @ToJson
    fun toJson(postResponse: PostResponse): String {
        TODO("NOT IMPLEMENTED")
    }

    @Suppress("unused")
    @FromJson
    fun fromJson(reader: JsonReader): PostResponse {
        posts.clear()
        reader.readObjectByName("data") {
            after = findAfter()
            readObjectByName("children") {
                readArray {
                    var kind = ""
                    readObject {
                        when(reader.selectName(options)) {
                            0 -> {
                                kind = nextString()
                            }
                            1 -> {
                                if(kind == "t3") {
                                    posts.add(PostTypeAdapterHelper.parsePost(after, this))
                                } else {
                                    //TODO this is a saved comment
                                    skipValue()
                                }
                            }
                            else -> {
                                skipValue()
                            }
                        }
                    }
                }
            }
        }

        return PostResponse(after, posts)
    }
}