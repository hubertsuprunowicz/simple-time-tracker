package com.zk.timetracker.models

import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.annotations.SerializedName
import com.zk.timetracker.error

data class Event(
    @SerializedName("id")
    val id: Int,

    @SerializedName("userID")
    val userId: Int,

    @SerializedName("projectId")
    val projectId: Int,

    @SerializedName("started")
    val started: String,

    @SerializedName("ended")
    val ended: String,

    @SerializedName("isBillable")
    val isBillable: Boolean,

    @SerializedName("isRemote")
    val isRemote: Boolean,

    @SerializedName("description")
    val description: String,

    @SerializedName("tagsId")
    val tagsId: List<Int>
)

val eventsList = mutableListOf<Event>()

fun MutableList<Event>.getMovies(page: Int): Pagination {
    val limit = 5
    val from = page * limit
    val to = from + limit
    val results = eventsList.slice(from..to)
    return Pagination(page, results)
}

data class Pagination(val page: Int, val results: List<Event>)

class EventsSource : PagingSource<Int, Event>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        return try {
            val nextPage = params.key ?: 1
            val movieListResponse = eventsList.getMovies(nextPage)

            LoadResult.Page(
                data = movieListResponse.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = movieListResponse.page.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        TODO("Not yet implemented")
    }
}