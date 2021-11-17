package com.zk.timetracker.models

import com.google.gson.annotations.SerializedName

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