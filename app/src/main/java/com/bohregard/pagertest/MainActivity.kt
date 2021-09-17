package com.bohregard.pagertest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bohregard.pagertest.extension.debug
import com.bohregard.pagertest.ui.theme.PagerTestTheme
import com.bohregard.pagertest.web.RedditApi
import com.bohregard.pagertest.web.paging.PostsPagingSource
import com.bohregard.pagertest.web.typeadapter.PostResponseTypeAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.coroutines.CoroutineContext

class MainActivity : ComponentActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    private val redditApi by lazy {
        val moshi = Moshi.Builder().add(PostResponseTypeAdapter()).build()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.reddit.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(RedditApi::class.java)
    }

    private val posts = Pager(
        config = PagingConfig(
            enablePlaceholders = false,
            initialLoadSize = 5,
            pageSize = 5,
            prefetchDistance = 20
        ),
        initialKey = null,
        pagingSourceFactory = { PostsPagingSource(redditApi) }
    ).flow.cachedIn(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PagerTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val posts = posts.collectAsLazyPagingItems()
                    debug("Testing...")

                    posts.snapshot().items.forEach {
                        debug("Post: ${it.title}")
                    }
                    LazyColumn {
                        items(posts, {
                            it.id
                        }) { post ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text("Post: ${post!!.title}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PagerTestTheme {
        Greeting("Android")
    }
}