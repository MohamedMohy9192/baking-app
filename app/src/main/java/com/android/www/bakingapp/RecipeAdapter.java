package com.android.www.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.www.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by OWNER on 4/28/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;
    private Context mContext;

    private OnRecipeClickListener mOnRecipeClickListener;

    public interface OnRecipeClickListener {
        void onRecipeClicked(Recipe recipe);
    }

    public RecipeAdapter(Context context, OnRecipeClickListener recipeClickListener) {
        this.mContext = context;
        this.mOnRecipeClickListener = recipeClickListener;

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);

        holder.recipeNameTextView.setText(recipe.getName());

        holder.servingsTextView.setText(String.valueOf(recipe.getServings()));

        String recipePosterPath = recipe.getImage();
        int recipePosterViewHolder = R.drawable.iv_reicpe_place_holder;

        if (TextUtils.isEmpty(recipePosterPath)) {
            holder.recipePosterImageView.setImageResource(recipePosterViewHolder);
        } else {
            Picasso.get().load(recipePosterPath)
                    .placeholder(recipePosterViewHolder)
                    .error(recipePosterViewHolder)
                    .into(holder.recipePosterImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {

        this.mRecipes = recipes;

        if (mRecipes != null) {
            notifyDataSetChanged();
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_recipe_name)
        TextView recipeNameTextView;

        @BindView(R.id.tv_recipe_servings)
        TextView servingsTextView;

        @BindView(R.id.iv_recipe_poster)
        ImageView recipePosterImageView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int itemPosition = getAdapterPosition();
            Recipe recipe = mRecipes.get(itemPosition);

            mOnRecipeClickListener.onRecipeClicked(recipe);
        }
    }
}
