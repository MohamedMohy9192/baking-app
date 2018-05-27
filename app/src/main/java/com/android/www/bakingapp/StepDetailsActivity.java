package com.android.www.bakingapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.www.bakingapp.model.Recipe;
import com.android.www.bakingapp.model.Step;

public class StepDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();

        if (savedInstanceState == null) {


            if (intent != null) {

                Step step = intent.getParcelableExtra(DetailActivity.STEP_DETAILS_EXTRA_STEP);
                DetailStepFragment detailStepFragment = DetailStepFragment.newInstance(step);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_step_container, detailStepFragment)
                        .commit();

            }
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
