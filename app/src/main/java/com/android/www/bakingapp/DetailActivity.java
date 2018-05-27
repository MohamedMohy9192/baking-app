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


    public static final String STEP_DETAILS_EXTRA_RECIPE = "step_details_recipe";
    public static final String STEP_DETAILS_EXTRA_STEP = "step_details";
    private int mItemListFragmentPosition = 0;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();

        if (findViewById(R.id.detail_linearLayout) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {


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
        } else {
            mTwoPane = false;
            if (savedInstanceState == null) {

                if (intent != null) {

                    Recipe recipe = intent.getParcelableExtra(MainActivity.RECIPE_INTENT_EXTRA);
                    StepListFragment stepListFragment = StepListFragment.newInstance(recipe);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_list_container, stepListFragment)
                            .commit();

                    IngredientListFragment ingredientListFragment = IngredientListFragment.newInstance(recipe);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.ingredient_list_container, ingredientListFragment)
                            .commit();
                }
            }
        }

    }

    @Override
    public void onListFragmentItemClicked(int itemListFragmentPosition) {
        mItemListFragmentPosition = itemListFragmentPosition;
        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra(MainActivity.RECIPE_INTENT_EXTRA);

        Step step = recipe.getSteps().get(mItemListFragmentPosition);
        if (mTwoPane) {

            DetailStepFragment detailStepFragment = DetailStepFragment.newInstance(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_step_container, detailStepFragment)
                    .commit();

        } else {
            Intent openStepDetails = new Intent(this, StepDetailsActivity.class);
            openStepDetails.putExtra(STEP_DETAILS_EXTRA_RECIPE, recipe);
            openStepDetails.putExtra(STEP_DETAILS_EXTRA_STEP, step);

            startActivity(openStepDetails);
        }
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
