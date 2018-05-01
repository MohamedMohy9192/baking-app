package com.android.www.bakingapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.www.bakingapp.model.Recipe;
import com.android.www.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepListFragment extends Fragment {

    public static final String RECIPE_STEP_LIST = "recipe_step_list";

    @BindView(R.id.rv_recipe_steps)
    RecyclerView mRecyclerView;

    private StepListAdapter mStepListAdapter;

    private Unbinder unbinder;

    public StepListFragment() {
        // Required empty public constructor
    }

    public static StepListFragment newInstance(Recipe recipe) {

        Bundle args = new Bundle();
        args.putParcelable(RECIPE_STEP_LIST, recipe);

        StepListFragment fragment = new StepListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mStepListAdapter = new StepListAdapter();

        mRecyclerView.setAdapter(mStepListAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {

            Recipe recipe = bundle.getParcelable(RECIPE_STEP_LIST);

            if (recipe != null) {
                List<Step> steps = recipe.getSteps();
                mStepListAdapter.setSteps(steps);
            }


        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
