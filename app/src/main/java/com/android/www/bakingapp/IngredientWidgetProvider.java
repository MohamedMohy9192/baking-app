package com.android.www.bakingapp;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.android.www.bakingapp.model.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidgetProvider extends AppWidgetProvider {

    private static final int NO_FLAG = 0;
    private static final int PENDING_INTENT_CODE = 1;

    public static final String RECIPE_INTENT_EXTRA = "recipe_extra";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipeId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(RECIPE_INTENT_EXTRA, recipeId);



        views.setRemoteAdapter(R.id.lv_widget, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //called once a new widget is created and every update interval which is set in widget_info xml file
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        //get the key from the preference.
//        IngredientService.startActionFetchIngredients(context);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeId = sharedPreferences.getString(context.getString(R.string.pref_widget_key),
                context.getString(R.string.pref_widget_nutella_pie_value));
        IngredientService.startActionFetchIngredients(context, Integer.valueOf(recipeId));

    }

    public static void updateIngredientWidget(Context context, AppWidgetManager appWidgetManager,
                                              int[] appWidgetIds, int recipeId) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

