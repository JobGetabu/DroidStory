package com.example.statusstories


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import com.example.statusstories.glideProgressBar.DelayBitmapTransformation
import com.example.statusstories.glideProgressBar.LoggingListener
import com.example.statusstories.glideProgressBar.ProgressTarget
import kotlinx.android.synthetic.main.activity_status_stories.*
import java.util.*

class StatusStoriesActivity : AppCompatActivity(), StoryStatusView.UserInteractionListener {
    private var counter = 0
    private var statusResources: Array<String> = arrayOf()

    //    private long[] statusResourcesDuration;
    private var statusDuration: Long = 0
    private var isImmer = true
    private var isCaching = true
    private var target: ProgressTarget<String?, Bitmap?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_stories)
        statusResources = intent.getStringArrayExtra(STATUS_RESOURCES_KEY)!!
        statusDuration = intent.getLongExtra(STATUS_DURATION_KEY, 3000L)
        //        statusResourcesDuration = getIntent().getLongArrayExtra(STATUS_DURATIONS_ARRAY_KEY);
        isImmer = intent.getBooleanExtra(IS_IMMERSIVE_KEY, true)
        isCaching = intent.getBooleanExtra(IS_CACHING_ENABLED_KEY, true)
        isTextEnabled = intent.getBooleanExtra(IS_TEXT_PROGRESS_ENABLED_KEY, true)
        storyStatusView = findViewById(R.id.storyStatusViewId)


        storyStatusView?.setStoriesCount(statusResources.size)
        storyStatusView?.setStoryDuration(statusDuration)
        // or
        // statusView.setStoriesCountWithDurations(statusResourcesDuration);
        storyStatusView?.setUserInteractionListener(this)
        storyStatusView?.playStories()
        target = MyProgressTarget(BitmapImageViewTarget(image), lottieAnim, textView)
        image?.setOnClickListener { storyStatusView?.skip() }
        storyStatusView?.pause()
        target?.setModel(statusResources[counter])
        Glide.with(image!!.context)
                .load(target?.model)
                .asBitmap()
                .skipMemoryCache(!isCaching)
                .diskCacheStrategy(if (isCaching) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)
                .transform(CenterCrop(image!!.context), DelayBitmapTransformation(1000))
                .listener(LoggingListener())
                .into(target)


        // bind reverse view
        findViewById<View>(R.id.reverse).setOnClickListener { storyStatusView?.reverse() }

        // bind skip view
        findViewById<View>(R.id.skip).setOnClickListener { storyStatusView?.skip() }

        findViewById<View>(R.id.center).setOnTouchListener { _, motionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    storyStatusView?.pause()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    true
                }
                MotionEvent.ACTION_UP -> {
                    storyStatusView?.resume()
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    true
                }
                MotionEvent.ACTION_OUTSIDE -> {
                    true
                }
                else -> super.onTouchEvent(motionEvent)
            }

        }

        /*findViewById(R.id.center).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    storyStatusView.pause();
                } else {
                    storyStatusView.resume();
                }
                return true;
            }
        });

        findViewById(R.id.actions).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    storyStatusView.pause();
                } else {
                    storyStatusView.resume();
                }
                return true;
            }
        });*/
    }

    override fun onNext() {
        storyStatusView!!.pause()
        ++counter
        target!!.setModel(statusResources[counter])
        Glide.with(image!!.context)
                .load(target!!.model)
                .asBitmap()
                .crossFade()
                .centerCrop()
                .skipMemoryCache(!isCaching)
                .diskCacheStrategy(if (isCaching) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)
                .transform(CenterCrop(image!!.context), DelayBitmapTransformation(1000))
                .listener(LoggingListener())
                .into(target)
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        storyStatusView!!.pause()
        --counter
        target!!.setModel(statusResources[counter])
        Glide.with(image!!.context)
                .load(target!!.model)
                .asBitmap()
                .centerCrop()
                .crossFade()
                //.skipMemoryCache(!isCaching)
                //.diskCacheStrategy(if (isCaching) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)
                .transform(CenterCrop(image!!.context), DelayBitmapTransformation(1000))
                .listener(LoggingListener())
                .into(target)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (isImmer && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }
    }

    override fun onComplete() {
        finish()
    }

    override fun onDestroy() {
        // Very important !
        storyStatusView!!.destroy()
        super.onDestroy()
    }

    /**
     * Demonstrates 3 different ways of showing the progress:
     *
     *  * Update a full fledged progress bar
     *  * Update a text view to display size/percentage
     *  * Update the placeholder via Drawable.level
     *
     * This last one is tricky: the placeholder that Glide sets can be used as a progress drawable
     * without any extra Views in the view hierarchy if it supports levels via `usesLevel="true"`
     * or `level-list`.
     *
     * @param <Z> automatically match any real Glide target so it can be used flexibly without reimplementing.
    </Z> */
    @SuppressLint("SetTextI18n") // text set only for debugging
    private class MyProgressTarget<Z>(target: Target<Z>?, private val lottieAnim: LottieAnimationView, private val text: TextView) : ProgressTarget<String?, Z>(target) {
        override fun getGranualityPercentage(): Float {
            return 0.1f // this matches the format string for #text below
        }

        override fun onConnecting() {
            lottieAnim.visibility = View.VISIBLE

            if (isTextEnabled) {
                text.visibility = View.VISIBLE
                text.text = "connecting"
            } else {
                text.visibility = View.INVISIBLE
            }
            storyStatusView?.pause()
        }

        override fun onDownloading(bytesRead: Long, expectedLength: Long) {
            //progress.isIndeterminate = false
            //progress.progress = (100 * bytesRead / expectedLength).toInt()



            if (isTextEnabled) {
                text.visibility = View.VISIBLE
                text.text = String.format(Locale.ROOT, "downloading %.2f/%.2f MB %.1f%%",
                        bytesRead / 1e6, expectedLength / 1e6, 100f * bytesRead / expectedLength)
            } else {
                text.visibility = View.INVISIBLE
            }
            storyStatusView!!.pause()
        }

        override fun onDownloaded() {
            //progress.isIndeterminate = true
            if (isTextEnabled) {
                text.visibility = View.VISIBLE
                text.text = "decoding and transforming"
            } else {
                text.visibility = View.INVISIBLE
            }
            storyStatusView!!.pause()
        }

        override fun onDelivered() {
            lottieAnim.visibility = View.INVISIBLE
            text.visibility = View.INVISIBLE
            storyStatusView!!.resume()
        }
    }

    companion object {
        const val STATUS_RESOURCES_KEY = "statusStoriesResources"
        const val STATUS_DURATION_KEY = "statusStoriesDuration"
        const val STATUS_DURATIONS_ARRAY_KEY = "statusStoriesDurations"
        const val IS_IMMERSIVE_KEY = "isImmersive"
        const val IS_CACHING_ENABLED_KEY = "isCaching"
        const val IS_TEXT_PROGRESS_ENABLED_KEY = "isText"
        private var storyStatusView: StoryStatusView? = null
        private var isTextEnabled = true
    }
}