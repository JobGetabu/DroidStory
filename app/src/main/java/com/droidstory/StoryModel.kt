package com.droidstory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class StoryModel(
    val user: User? = User(),
    val stories: List<Story>? = arrayListOf(),
) : Parcelable

@Parcelize
data class User(
    val fullName: String? = "",
    val image: String? = "",
): Parcelable

@Parcelize
data class Story(
    val image: String? = "",
    val time: Date? = Calendar.getInstance().time,
    val description: String? = "",
): Parcelable