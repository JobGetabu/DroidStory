package com.example.statusstories


import com.example.statusstories.R
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.statusstories.glideProgress.GlideImageLoader
import com.example.statusstories.utils.GetTimeAgo
import com.example.statusstories.utils.OnSwipeTouchListener
import com.example.statusstories.utils.hideView
import com.example.statusstories.utils.showView
import kotlinx.android.synthetic.main.activity_status_stories.*
import java.util.*


class StatusStoriesActivity : AppCompatActivity(), StoryStatusView.UserInteractionListener {
    private var counter = 0
    //private var statusResources: Array<String> = arrayOf()

    private var storyModels : StoryModel? = StoryModel()

    //    private long[] statusResourcesDuration;
    private var statusDuration: Long = 0
    private var isImmer = true
    private var isCaching = true
    //private var target: ProgressTarget<String?, Bitmap?>? = null


    val options: RequestOptions = RequestOptions()
        .centerCrop()
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.ic_pic_error)
        .diskCacheStrategy(if (isCaching) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)
        .priority(Priority.HIGH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            // set an exit transition
            enterTransition = Slide(Gravity.BOTTOM)
            exitTransition = Slide(Gravity.TOP)
        }
        setContentView(R.layout.activity_status_stories)

        storyModels = intent.getParcelableExtra(STATUS_STORY_KEY)
        if (storyModels == null) {
            Toast.makeText(this, "No Story Found", Toast.LENGTH_SHORT).show()
            finish()
        }

        statusDuration = intent.getLongExtra(STATUS_DURATION_KEY, 5000L)
        //        statusResourcesDuration = getIntent().getLongArrayExtra(STATUS_DURATIONS_ARRAY_KEY);
        isImmer = intent.getBooleanExtra(IS_IMMERSIVE_KEY, true)
        isCaching = true //intent.getBooleanExtra(IS_CACHING_ENABLED_KEY, true)
        isTextEnabled = intent.getBooleanExtra(IS_TEXT_PROGRESS_ENABLED_KEY, true)
        storyStatusView = findViewById(R.id.storyStatusViewId)


        storyStatusView?.setStoriesCount(storyModels!!.stories!!.size)
        storyStatusView?.setStoryDuration(statusDuration)
        // or
        // statusView.setStoriesCountWithDurations(statusResourcesDuration);
        storyStatusView?.setUserInteractionListener(this)
        storyStatusView?.playStories()
        //target = MyProgressTarget(BitmapImageViewTarget(image), lottieAnim, textView)
        //image?.setOnClickListener { storyStatusView?.skip() }

        storyStatusView?.pause()
        //target?.setModel(storyModels!!.stories!![counter].image)


        //set ui

        GlideImageLoader(image, lottieAnim, storyStatusView)
            .load(storyModels!!.stories!![counter].image,options)

        Glide.with(user_img.context)
            .load(storyModels?.user?.image)
            .error(R.drawable.avatar_placeholder)
            .into(user_img)

        user_name.text = storyModels?.user?.fullName
        story_time.text = GetTimeAgo.getTimeAgo(
            storyModels!!.stories!![counter].time,
            story_time.context
        )

        showSystemUI()

        // bind reverse view
        findViewById<View>(R.id.reverse).setOnClickListener { storyStatusView?.reverse() }

        // bind skip view
        findViewById<View>(R.id.skip).setOnClickListener { storyStatusView?.skip() }

        findViewById<View>(R.id.center).setOnTouchListener(object : OnSwipeTouchListener(this) {

            override fun onSwipeDown() {
                finish()
            }

            override fun onSwipeUp() {
                super.onSwipeUp()
            }

            override fun onDown() {
                super.onDown()
                storyStatusView!!.pause()
                hideItems()
            }

            override fun onUp() {
                super.onUp()
                storyStatusView!!.resume()
                showItems()
            }
        })

        //prefetch stories

    }

    override fun onNext() {
        storyStatusView!!.pause()
        ++counter



        story_time.text = GetTimeAgo.getTimeAgo(
            storyModels!!.stories!![counter].time,
            story_time.context
        )

        GlideImageLoader(image, lottieAnim, storyStatusView)
            .load(storyModels!!.stories!![counter].image,options)
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        storyStatusView!!.pause()
        --counter


        story_time.text = GetTimeAgo.getTimeAgo(
            storyModels!!.stories!![counter].time,
            story_time.context
        )

        GlideImageLoader(image, lottieAnim, storyStatusView)
            .load(storyModels!!.stories!![counter].image,options)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (isImmer && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasFocus) showSystemUI()
        }
    }

    private fun hideItems(){
        upperBody.hideView()
        lowerBody.hideView()
        storyStatusView?.hideView()
        showSystemUI()
    }
    private fun showItems(){
        upperBody.showView()
        lowerBody.showView()
        storyStatusView?.showView()
        showSystemUI()
    }

    private fun showSystemUI(){
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                //or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
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
    </Z>
    @SuppressLint("SetTextI18n") // text set only for debugging
    private class MyProgressTarget<Z>(target: Target<Z>?, private val lottieAnim: LottieAnimationView, private val text: TextView) : ProgressTarget<String?, Z>(target) {
    override fun getGranualityPercentage(): Float {
    return 0.1f // this matches the format string for #text below
    }

    override fun onConnecting() {
    lottieAnim.visibility = View.VISIBLE
    storyStatusView!!.pause()

    if (isTextEnabled) {
    text.visibility = View.VISIBLE
    text.text = "connecting"
    } else {
    text.visibility = View.INVISIBLE
    }
    storyStatusView!!.pause()
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
     */

    companion object {
        const val STATUS_STORY_KEY = "STATUS_STORY_KEY"
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