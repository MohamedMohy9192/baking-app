package com.android.www.bakingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.www.bakingapp.model.Ingredient;
import com.android.www.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientListFragment extends Fragment {


    private IngredientAdapter mIngredientAdapter;

    @BindView(R.id.rv_recipe_ingredient)
    RecyclerView mRecyclerView;

    private Unbinder unbinder;

    private static final String RECIPE_INGREDIENT_LIST = "ingredient_list";

    public IngredientListFragment() {
        // Required empty public constructor
    }

    public static IngredientListFragment newInstance(Recipe recipe) {

        Bundle args = new Bundle();
        args.putParcelable(RECIPE_INGREDIENT_LIST, recipe);

        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mIngredientAdapter = new IngredientAdapter();

        mRecyclerView.setAdapter(mIngredientAdapter);

        Bundle bundle = getArguments();

        if (bundle != null) {
            Recipe recipe = bundle.getParcelable(RECIPE_INGREDIENT_LIST);

            if (recipe != null) {
                List<Ingredient> ingredients = recipe.getIngredients();
                mIngredientAdapter.setIngredients(ingredients);
            }
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
