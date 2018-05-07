package com.android.www.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.www.bakingapp.model.Ingredient;
import com.android.www.bakingapp.model.Recipe;
import com.android.www.bakingapp.utilities.NetworkUtilities;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetConfigurationActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Recipe>>, RecipeAdapter.OnRecipeClickListener {

    private static final int WIDGET_LOADER_ID = 88;

    @BindView(R.id.rv_widget_recipe)
    RecyclerView mRecyclerView;

    @BindView(R.id.pb_widget_loading_indicator)
    ProgressBar mLoadingIndicatorProgressBar;

    private RecipeAdapter mRecipeAdapter;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private AppWidgetManager mAppWidgetManger;
    private RemoteViews mRemoteViews;

    public static final String RECIPE_INTENT_EXTRA = "recipe_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        ButterKnife.bind(this);

        setResult(RESULT_CANCELED);

        mAppWidgetManger = AppWidgetManager.getInstance(this);

        mRemoteViews = new RemoteViews(this.getPackageName(), R.layout.ingredient_list_widget);
//        mRemoteViews = new RemoteViews(this.getPackageName(), R.layout.widget_list_view);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(
                this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        getSupportLoaderManager().initLoader(WIDGET_LOADER_ID, null, this);
    }


    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new RecipeAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        mLoadingIndicatorProgressBar.setVisibility(View.INVISIBLE);
        if (data != null) {
            mRecipeAdapter.setRecipes(data);
        } else {
            Toast.makeText(this, R.string.recipes_error_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

    @Override
    public void onRecipeClicked(Recipe recipe) {

        List<Ingredient> ingredients = recipe.getIngredients();

        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            builder.append("- ")
                    .append(ingredient.getIngredient())
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(" ")
                    .append(ingredient.getQuantity())
                    .append("\n");
        }

        mRemoteViews.setTextViewText(R.id.tv_all_ingredient, builder.toString());


        mAppWidgetManger.updateAppWidget(mAppWidgetId, mRemoteViews);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        setResult(RESULT_OK, resultValue);
        finish();


    }

    private static class RecipeAsyncTaskLoader extends AsyncTaskLoader<List<Recipe>> {

        private List<Recipe> mRecipes;

        private WeakReference<WidgetConfigurationActivity> mWidgetActivityWeakReference;

        public RecipeAsyncTaskLoader(WidgetConfigurationActivity widgetConfigurationActivity) {
            super(widgetConfigurationActivity);
            this.mWidgetActivityWeakReference = new WeakReference<>(widgetConfigurationActivity);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mRecipes != null) {
                deliverResult(mRecipes);
            } else {
                if (mWidgetActivityWeakReference.get() != null) {
                    mWidgetActivityWeakReference.get().mLoadingIndicatorProgressBar
                            .setVisibility(View.VISIBLE);
                }
                forceLoad();
            }
        }

        @Override
        public List<Recipe> loadInBackground() {

            URL requestUrl = NetworkUtilities.buildUrl(getContext()
                    .getResources().getString(R.string.recipe_request_url));

            if (requestUrl != null) {
                String recipesJsonResponse = NetworkUtilities.getJsonResponseFromHttpUrl(requestUrl);
                if (recipesJsonResponse != null) {
                    return NetworkUtilities.getRecipeFromJson(recipesJsonResponse);
                }
            }
            return null;
        }

        @Override
        public void deliverResult(List<Recipe> data) {
            this.mRecipes = data;
            super.deliverResult(data);
        }
    }
}
