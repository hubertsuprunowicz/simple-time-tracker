package com.zk.timetracker.models

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("users")
    val users: List<User>,

    @SerializedName("projects")
    val projects: List<Project>,

    @SerializedName("events")
    val events: List<Event>
)