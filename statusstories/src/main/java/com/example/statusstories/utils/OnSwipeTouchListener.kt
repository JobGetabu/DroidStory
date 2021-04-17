package com.example.statusstories.utils

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.example.statusstories.StatusStoriesActivity
import kotlin.math.abs
internal open class OnSwipeTouchListener(c: Context?) :
    OnTouchListener {
    private val gestureDetector: GestureDetector
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                onDown()
                Log.d("SWIPE", "onDown: ")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("SWIPE", "onMove: ")
            }
            MotionEvent.ACTION_UP -> {
                onUp()
                Log.d("SWIPE", "onUp: ")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("SWIPE", "onCancel: ")
            }
            MotionEvent.ACTION_OUTSIDE -> {
                Log.d("SWIPE", "ACTION_OUTSIDE: ")
            }
            //else -> return true
        }

        return gestureDetector.onTouchEvent(motionEvent)
    }
    private inner class GestureListener : SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD: Int = 100
        private val SWIPE_VELOCITY_THRESHOLD: Int = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            onClick()
            return super.onSingleTapUp(e)
        }
        override fun onDoubleTap(e: MotionEvent): Boolean {
            onDoubleClick()
            return super.onDoubleTap(e)
        }
        override fun onLongPress(e: MotionEvent) {
            onLongClick()
            super.onLongPress(e)
        }
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(
                            velocityX
                        ) > SWIPE_VELOCITY_THRESHOLD
                    ) {
                        if (diffX > 0) {
                            onSwipeRight()
                        }
                        else {
                            onSwipeLeft()
                        }
                    }
                }
                else {
                    if (abs(diffY) > SWIPE_THRESHOLD && abs(
                            velocityY
                        ) > SWIPE_VELOCITY_THRESHOLD
                    ) {
                        if (diffY < 0) {
                            onSwipeUp()
                        }
                        else {
                            onSwipeDown()
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return false
        }
    }
    open fun onUp() {}
    open fun onDown() {}
    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeUp() {
        Log.d("SWIPE", "onSwipeUp: ")
    }
    open fun onSwipeDown() {
        Log.d("SWIPE", "onSwipeDown: ")
    }
    private fun onClick() {}
    private fun onDoubleClick() {}
    private fun onLongClick() {}
    init {
        gestureDetector = GestureDetector(c, GestureListener())
    }
}