package com.zk.timetracker.models

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)

val tagsList = mutableListOf<Tag>()