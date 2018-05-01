package com.android.www.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.www.bakingapp.model.Ingredient;
import com.android.www.bakingapp.model.Recipe;
import com.android.www.bakingapp.model.Step;

public class DetailActivity extends AppCompatActivity
        implements StepListFragment.OnListFragmentListener {


    int mItemListFragmentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            Intent intent = getIntent();

            if (intent != null) {
                Recipe recipe = intent.getParcelableExtra(MainActivity.RECIPE_INTENT_EXTRA);
                StepListFragment stepListFragment = StepListFragment.newInstance(recipe);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_list_container, stepListFragment)
                        .commit();

                Step step = recipe.getSteps().get(mItemListFragmentPosition);
                DetailStepFragment detailStepFragment = DetailStepFragment.newInstance(step);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_step_container, detailStepFragment)
                        .commit();

                IngredientListFragment ingredientListFragment = IngredientListFragment.newInstance(recipe);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.ingredient_list_container, ingredientListFragment)
                        .commit();
            }
        }

    }

    @Override
    public void onListFragmentItemClicked(int itemListFragmentPosition) {
        mItemListFragmentPosition = itemListFragmentPosition;

        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra(MainActivity.RECIPE_INTENT_EXTRA);

        Step step = recipe.getSteps().get(mItemListFragmentPosition);
        DetailStepFragment detailStepFragment = DetailStepFragment.newInstance(step);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_step_container, detailStepFragment)
                .commit();
    }
}
