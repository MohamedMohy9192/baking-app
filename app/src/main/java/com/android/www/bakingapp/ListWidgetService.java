package com.android.www.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.www.bakingapp.model.Ingredient;
import com.android.www.bakingapp.model.Recipe;
import com.android.www.bakingapp.utilities.NetworkUtilities;

import java.util.List;

/**
 * Created by OWNER on 5/4/2018.
 */

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        int recipeId = 0;
        if (intent != null){
            recipeId = intent.getIntExtra(IngredientWidgetProvider.RECIPE_INTENT_EXTRA, 0);
        }
        return new ListRemoteViewsFactory(this.getApplicationContext(), recipeId);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIngredients;
    private int mRecipeId;

    public ListRemoteViewsFactory(Context context, int recipeId) {
        this.mContext = context;
        this.mRecipeId = recipeId;

    }

    @Override
    public void onCreate() {}
    @Override
    public void onDataSetChanged() {

        if (mIngredients != null) mIngredients.clear();
        String jsonResponse = NetworkUtilities.getJsonResponseFromHttpUrl(
                NetworkUtilities.buildUrl(mContext.getString(R.string.recipe_request_url)));

        List<Recipe> recipes = NetworkUtilities.getRecipeFromJson(jsonResponse);
        if (recipes == null) {
            return;
        }

        Recipe recipe = recipes.get(mRecipeId);

        mIngredients = recipe.getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients == null) return 0;

        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = mIngredients.get(position);

        String ingredientName = ingredient.getIngredient();
        String ingredientMeasure = ingredient.getMeasure();
        float ingredientQuantity = ingredient.getQuantity();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_widget);

        String allIngredient = ingredientName + " - " + ingredientMeasure + " - " + ingredientQuantity;
        views.setTextViewText(R.id.tv_all_ingredient, allIngredient);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
