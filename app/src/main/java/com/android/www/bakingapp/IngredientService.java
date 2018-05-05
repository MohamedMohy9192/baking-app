package com.android.www.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.android.www.bakingapp.model.Recipe;
import com.android.www.bakingapp.utilities.NetworkUtilities;

import java.util.List;

/**
 * Created by OWNER on 5/4/2018.
 */

public class IngredientService extends IntentService {

    public static final String ACTION_FETCH_INGREDIENTS = "fetch_ingredients";

    private static final String RECIPE_KEY = "recipe_key";

    private static final int DEFAULT_RECIPE_ID = 0;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public IngredientService() {
        super("IngredientService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(ACTION_FETCH_INGREDIENTS)) {
                int recipeId = intent.getIntExtra(RECIPE_KEY, DEFAULT_RECIPE_ID);
                handleActionFetchIngredients(recipeId);
            }
        }
    }

    public static void startActionFetchIngredients(Context context, int recipeId) {
        Intent fetchIngredientsIntent = new Intent(context, IngredientService.class);

        fetchIngredientsIntent.putExtra(RECIPE_KEY, recipeId);
        fetchIngredientsIntent.setAction(ACTION_FETCH_INGREDIENTS);

        context.startService(fetchIngredientsIntent);
    }

    private void handleActionFetchIngredients(int recipeId) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, IngredientWidgetProvider.class));


        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);
        IngredientWidgetProvider.updateIngredientWidget(this, appWidgetManager, appWidgetIds, recipeId);
    }

}
