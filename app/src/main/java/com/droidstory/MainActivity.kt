package com.droidstory

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.statusstories.StatusStoriesActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    var isCacheEnabled = true
    var isImmersiveEnabled = false
    var isTextEnabled = false
    var storyDuration = 5000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (findViewById<View>(R.id.isCacheEnabled) as SwitchCompat).setOnCheckedChangeListener { compoundButton, b ->
            isCacheEnabled = b
        }
        (findViewById<View>(R.id.isImmersiveEnabled) as SwitchCompat).setOnCheckedChangeListener { compoundButton, b ->
            isImmersiveEnabled = b
        }
        (findViewById<View>(R.id.isTextEnabled) as SwitchCompat).setOnCheckedChangeListener { compoundButton, b ->
            isTextEnabled = b
        }
        (findViewById<View>(R.id.seekbar) as SeekBar).setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                storyDuration = if (i < 4) 3 * 1000L else i * 1000L
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        findViewById<View>(R.id.storyTime).setOnClickListener { view ->
            val a = Intent(view.context, StatusStoriesActivity::class.java)
            a.putExtra(StatusStoriesActivity.STATUS_DURATION_KEY, storyDuration)
            a.putExtra(StatusStoriesActivity.IS_IMMERSIVE_KEY, isImmersiveEnabled)
            a.putExtra(StatusStoriesActivity.IS_CACHING_ENABLED_KEY, isCacheEnabled)
            a.putExtra(StatusStoriesActivity.IS_TEXT_PROGRESS_ENABLED_KEY, isTextEnabled)
            a.putExtra(StatusStoriesActivity.STATUS_STORY_KEY, storyModel)
            startActivity(a)
        }
    }
}