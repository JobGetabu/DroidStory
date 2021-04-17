package com.example.statusstories.utils

import android.view.View

fun View.showView() {
    this.animate().alpha(1.0f)
        .withEndAction {
            this.visibility = View.VISIBLE
        }
}

fun View.hideView() {
    this.animate().alpha(0.0f)
        .withEndAction {
            this.visibility = View.GONE
        }
}