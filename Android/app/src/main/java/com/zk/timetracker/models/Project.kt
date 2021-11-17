package com.zk.timetracker.models

import com.google.gson.annotations.SerializedName

data class Project(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("userId")
    val usersId: List<Int>,
)

val projectsList = mutableListOf<Project>()