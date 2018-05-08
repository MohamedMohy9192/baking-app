package com.android.www.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.www.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailStepFragment extends Fragment {

    private static final String LOG_TAG = DetailStepFragment.class.getSimpleName();

    private static final String STEP_LIST_ITEM = "step_list_item";
    private static final String PLAY_STATE_KEY = "play_state";
    private static final String WINDOW_INDEX_KEY = "window_index";
    private static final String CURRENT_POSITION_KEY = "current_position";
    private static final String VIDEO_URL_KEY = "video_key";

    @BindView(R.id.tv_step_description)
    TextView mStepDescription;
    @BindView(R.id.tv_step_short_description)
    TextView mStepShortDescription;
    @BindView(R.id.player_view)
    PlayerView mPlayerView;
    @BindView(R.id.iv_thumbnail)
    ImageView mThumbnailImageView;

    private SimpleExoPlayer mSimpleExoPlayer;

    private boolean mPlayWhenReady = false;
    private int mCurrentWindow = 0;
    private long mPlayBackPosition = 0;

    private Unbinder unbinder;
    private String mVideoUrl;

    public DetailStepFragment() {
        // Required empty public constructor
    }

    public static DetailStepFragment newInstance(Step step) {

        Bundle args = new Bundle();
        args.putParcelable(STEP_LIST_ITEM, step);

        DetailStepFragment fragment = new DetailStepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail_step, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_STATE_KEY);
            mCurrentWindow = savedInstanceState.getInt(WINDOW_INDEX_KEY);
            mPlayBackPosition = savedInstanceState.getLong(CURRENT_POSITION_KEY);
            mVideoUrl = savedInstanceState.getString(VIDEO_URL_KEY);

        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            Step step = bundle.getParcelable(STEP_LIST_ITEM);

            if (step != null) {
                mStepShortDescription.setText(step.getShortDescription());
                mStepDescription.setText(step.getDescription());
                mVideoUrl = step.getVideoURL();
                String stepThumbnailUrl = step.getThumbnailURL();
                int thumbnailViewHolder = R.drawable.iv_reicpe_place_holder;

//                if (TextUtils.isEmpty(stepThumbnailUrl)) {
//                    mThumbnailImageView.setImageResource(thumbnailViewHolder);
//                } else {
//                    Picasso.get().load(stepThumbnailUrl)
//                            .placeholder(thumbnailViewHolder)
//                            .error(thumbnailViewHolder)
//                            .into(mThumbnailImageView);
//                }
            }
        }

        return rootView;
    }

    private void initializePlayer() {
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mPlayerView.setPlayer(mSimpleExoPlayer);

        mSimpleExoPlayer.setPlayWhenReady(mPlayWhenReady);
        mSimpleExoPlayer.seekTo(mCurrentWindow, mPlayBackPosition);


        Uri uri = Uri.parse(mVideoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        mSimpleExoPlayer.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("BakingApp"))
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mSimpleExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PLAY_STATE_KEY, mPlayWhenReady);
        outState.putInt(WINDOW_INDEX_KEY, mCurrentWindow);
        outState.putLong(CURRENT_POSITION_KEY, mPlayBackPosition);

        outState.putString(VIDEO_URL_KEY, mVideoUrl);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
        mCurrentWindow = mSimpleExoPlayer.getCurrentWindowIndex();
        mPlayBackPosition = mSimpleExoPlayer.getCurrentPosition();

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
