package com.bohregard.pagertest.extension

import com.squareup.moshi.JsonReader
import java.time.Instant
import kotlin.reflect.full.createType


fun JsonReader.readObject(work: JsonReader.() -> Unit) {
    beginObject()
    while (hasNext()) {
        work()
    }
    endObject()
}

fun JsonReader.readArray(work: JsonReader.() -> Unit) {
    beginArray()
    while (hasNext()) {
        work()
    }
    endArray()
}

fun JsonReader.readArrayObject(work: JsonReader.() -> Unit) {
    readArray {
        readObject {
            work()
        }
    }
}

fun JsonReader.readObjectByName(name: String, work: JsonReader.() -> Unit) {
    readObject {
        when (nextName()) {
            name -> work()
            else -> skipValue()
        }
    }
}

inline fun <reified T> JsonReader.readArrayObject(
    list: MutableList<T>,
    crossinline work: JsonReader.(map: MutableMap<String, Any?>) -> Unit
) {
    readArray {
        val map = mutableMapOf<String, Any?>()
        readObject {
            work(map)
        }

        list.add(map.toClass())
    }
}

/**
 * Reads and returns the string if it exists, otherwise consumes the token and returns null
 *
 * @return
 */
fun JsonReader.nextStringOrNull(): String? {
    return if(peek() == JsonReader.Token.NULL) {
        nextNull<String>()
    } else {
        nextString()
    }
}

inline fun <reified T> MutableMap<String, Any?>.toClass(): T {
    val params = T::class.constructors.first().parameters.map {
        if(this[it.name] == null) {
            when (it.type) {
                String::class.createType() -> ""
                Int::class.createType() -> 0
                Instant::class.createType(nullable = true) -> null
                else -> ""
            }
        } else {
            this[it.name]
        }
    }.toTypedArray()
    return T::class.java.constructors.first().newInstance(*params) as T
}

fun JsonReader.findAfter(): String? {
    val newReader = peekJson()
    var after: String? = null
    newReader.readObjectByName("after") {
        after = nextStringOrNull()
    }
    return after
}