package com.android.www.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.www.bakingapp.model.Recipe;
import com.android.www.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailStepFragment extends Fragment {

    private static final String LOG_TAG = DetailStepFragment.class.getSimpleName();

    private static final String STEP_LIST_ITEM = "step_list_item";

    @BindView(R.id.tv_step_description)
    TextView mStepDescription;
    @BindView(R.id.tv_step_short_description)
    TextView mStepShortDescription;

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;
    private ExoPlayer mExoPlayer;

    private Recipe mRecipe;

    private Unbinder unbinder;

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            Step step = bundle.getParcelable(STEP_LIST_ITEM);

            if (step != null) {
                mStepShortDescription.setText(step.getShortDescription());
                initializedPlayer(step.getVideoURL());
                mStepDescription.setText(step.getDescription());

            }
        }

        return rootView;
    }


    public void initializedPlayer(String videoUrl) {

        Uri mediaUri = Uri.parse(videoUrl);
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);

        String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);

        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);

    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
