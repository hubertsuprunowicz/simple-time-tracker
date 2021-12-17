package com.zk.timetracker.models

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("id")
    var id: Int,

    @SerializedName("userID")
    var userId: Int,

    @SerializedName("projectId")
    var projectId: Int,

    @SerializedName("started")
    var started: String,

    @SerializedName("ended")
    var ended: String,

    @SerializedName("isBillable")
    var isBillable: Boolean,

    @SerializedName("isRemote")
    var isRemote: Boolean,

    @SerializedName("description")
    var description: String,

    @SerializedName("tagsId")
    var tagsId: List<Int>
)

val eventsList = mutableListOf<Event>()

fun MutableList<Event>.getEvents(page: Int): List<Event> {
    val limit = 5
    val from = page * limit
    val to = from + limit - 1

    if(eventsList.size -1 > to) {
        return eventsList.slice(from..to)
    }

    return eventsList.slice(from until eventsList.size)
//    return Pagination(page, results)
}

data class Pagination(val page: Int, val results: List<Event>)

//class EventsSource : PagingSource<Int, Event>() {
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
//        return try {
//            val nextPage = params.key ?: 1
//            val movieListResponse = eventsList.getMovies(nextPage)
//
//            LoadResult.Page(
//                data = movieListResponse.results,
//                prevKey = if (nextPage == 1) null else nextPage - 1,
//                nextKey = movieListResponse.page.plus(1)
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
//        TODO("Not yet implemented")
//    }
//}