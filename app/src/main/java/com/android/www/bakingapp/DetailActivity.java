package com.android.www.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.www.bakingapp.model.Recipe;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null){

            Intent intent = getIntent();

            if (intent != null) {
                Recipe recipe = intent.getParcelableExtra(MainActivity.RECIPE_INTENT_EXTRA);
                StepListFragment stepListFragment = StepListFragment.newInstance(recipe);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_list_container, stepListFragment)
                        .commit();
            }
        }

    }
}
