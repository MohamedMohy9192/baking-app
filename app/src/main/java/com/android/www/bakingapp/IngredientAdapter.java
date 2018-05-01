package com.android.www.bakingapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.www.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by OWNER on 5/1/2018.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> mIngredients;

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        Ingredient ingredient = mIngredients.get(position);

        holder.ingredientQuantityTextView.setText(String.valueOf(ingredient.getQuantity()));
        holder.ingredientMeasureTextView.setText(ingredient.getMeasure());
        holder.ingredientTextView.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;

        return mIngredients.size();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.mIngredients = ingredients;

        if (mIngredients != null) {
            notifyDataSetChanged();
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient_quantity)
        TextView ingredientQuantityTextView;

        @BindView(R.id.tv_ingredient_measure)
        TextView ingredientMeasureTextView;

        @BindView(R.id.tv_ingredient)
        TextView ingredientTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
