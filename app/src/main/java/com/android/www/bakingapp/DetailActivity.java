package com.android.www.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.www.bakingapp.model.Recipe;
import com.android.www.bakingapp.model.Step;

public class DetailActivity extends AppCompatActivity
        implements StepListFragment.OnListFragmentListener {


    private int mItemListFragmentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
