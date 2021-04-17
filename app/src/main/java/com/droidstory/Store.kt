package com.droidstory

import com.example.statusstories.Story
import com.example.statusstories.StoryModel
import com.example.statusstories.User
import java.util.*

val st1 = Story(
    image = "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00001.jpg?alt=media&token=460667e4-e084-4dc5-b873-eefa028cec32",
    time = getTime(),
    description = getD()
)

val st2 = Story(
    image = "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00002.jpg?alt=media&token=e8e86192-eb5d-4e99-b1a8-f00debcdc016",
    time = getTime(),
    description = getD()
)

val st3 = Story(
    image ="https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00004.jpg?alt=media&token=af71cbf5-4be3-4f8a-8a2b-2994bce38377",
    time = getTime(),
    description = getD()
)

val st4 = Story(
    image = "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00005.jpg?alt=media&token=7d179938-c419-44f4-b965-1993858d6e71",
    time = getTime(),
    description = getD()
)

val st5 = Story(
    image = "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00006.jpg?alt=media&token=cdd14cf5-6ed0-4fb7-95f5-74618528a48b",
    time = getTime(),
    description = getD()
)

val st6 = Story(
    image = "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00007.jpg?alt=media&token=98524820-6d7c-4fb4-89b1-65301e1d6053",
    time = getTime(),
    description = getD()
)

val st7 = Story(
    image ="https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00008.jpg?alt=media&token=7ef9ed49-3221-4d49-8fb4-2c79e5dab333",
    time = getTime(),
    description = getD()
)

val st8 = Story(
    image ="https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00009.jpg?alt=media&token=00d56a11-7a92-4998-a05a-e1dd77b02fe4",
    time = getTime(),
    description = getD()
)

val st9 = Story(
    image = "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00010.jpg?alt=media&token=24f8f091-acb9-432a-ae0f-7e6227d18803",
    time = getTime(),
    description = getD()
)

var u = User(
    "Lue Steve",
    "https://lh3.googleusercontent.com/ogw/ADGmqu8_8vzkTpRaHOyCW-UJzZhOjit_2THluAN2DKB2xw=s64-c-mo"
)

var storyModel = StoryModel(
    user = u,
    stories = arrayListOf(st1, st2, st3, st4, st5, st6, st7, st8, st9, st1, st2, st6)
)


private val resources = arrayOf(
    "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00001.jpg?alt=media&token=460667e4-e084-4dc5-b873-eefa028cec32",
    "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00002.jpg?alt=media&token=e8e86192-eb5d-4e99-b1a8-f00debcdc016",
    "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00004.jpg?alt=media&token=af71cbf5-4be3-4f8a-8a2b-2994bce38377",
    "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00005.jpg?alt=media&token=7d179938-c419-44f4-b965-1993858d6e71",
    "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00006.jpg?alt=media&token=cdd14cf5-6ed0-4fb7-95f5-74618528a48b",
    "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00007.jpg?alt=media&token=98524820-6d7c-4fb4-89b1-65301e1d6053",
    "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00008.jpg?alt=media&token=7ef9ed49-3221-4d49-8fb4-2c79e5dab333",
    "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00009.jpg?alt=media&token=00d56a11-7a92-4998-a05a-e1dd77b02fe4",
    "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00010.jpg?alt=media&token=24f8f091-acb9-432a-ae0f-7e6227d18803"
)

private fun getTime(): Date {
    val now = Calendar.getInstance()
    now.add(Calendar.HOUR, Random().nextInt(336) * -1)
    now.add(Calendar.HOUR, Random().nextInt(336))
    return now.time
}

private fun getD() : String {
    val d = Random().nextInt(336)
    if(d / 2 == 0){
        return "It has four arguments ; first argument holds details of point where down gesture performed, second argument holds details of point where the scrolling ends, third arguments represent velocity of scroll along X-axis of screen and fourth arguments represent velocity of scroll along Y-axis of screen\n" +
                "\n" +
                "Read more: http://mrbool.com/how-to-work-with-swipe-gestures-in-android/28088#ixzz6sJbXnPRM"
    }else return ""
}