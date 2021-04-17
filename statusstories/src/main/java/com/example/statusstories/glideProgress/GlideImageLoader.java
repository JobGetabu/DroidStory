package com.example.statusstories.glideProgress;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.statusstories.StoryStatusView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class GlideImageLoader {

    private ImageView mImageView;
    private LottieAnimationView mLottieAnim;
    private StoryStatusView mStoryStatusView;

    public GlideImageLoader(ImageView imageView, LottieAnimationView lottieAnim, StoryStatusView storyStatusView) {
        mImageView = imageView;
        mLottieAnim = lottieAnim;
        mStoryStatusView = storyStatusView;
    }

    public void load(final String url, RequestOptions options) {
        if (url == null || options == null) return;

        onConnecting();

        //set Listener & start
        ProgressAppGlideModule.expect(url, new ProgressAppGlideModule.UIonProgressListener() {
            @Override
            public void onProgress(long bytesRead, long expectedLength) {
                if (mLottieAnim != null) {
                    //mProgressBar.setProgress((int) (100 * bytesRead / expectedLength));
                    mLottieAnim.setProgress((100 * bytesRead / expectedLength));
                }

                if (mStoryStatusView != null) mStoryStatusView.pause();
            }

            @Override
            public float getGranualityPercentage() {
                return 1.0f;
            }
        });
        //Get Image
        Glide.with(mImageView.getContext())
                .load(url)
                .transition(withCrossFade())
                //.apply(options.skipMemoryCache(true))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ProgressAppGlideModule.forget(url);
                        onFinished();
                        if (mStoryStatusView != null) mStoryStatusView.resume();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ProgressAppGlideModule.forget(url);
                        onFinished();
                        return false;
                    }
                })
                .into(mImageView);
    }


    private void onConnecting() {
        if (mLottieAnim != null) mLottieAnim.setVisibility(View.VISIBLE);
        if (mStoryStatusView != null) mStoryStatusView.pause();
    }

    private void onFinished() {
        if (mLottieAnim != null && mImageView != null) {
            mLottieAnim.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }
        if (mStoryStatusView != null) mStoryStatusView.resume();
    }
}
