package com.android.www.bakingapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.www.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by OWNER on 4/29/2018.
 */

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListViewHolder> {

    private List<Step> mSteps;

    private OnStepItemClickListener mOnStepItemClickListener;

    public interface OnStepItemClickListener {
        void onStepItemClicked(int itemPosition);
    }

    public StepListAdapter(OnStepItemClickListener stepItemClickListener) {
        this.mOnStepItemClickListener = stepItemClickListener;
    }

    @NonNull
    @Override
    public StepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);
        return new StepListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepListViewHolder holder, int position) {
        Step step = mSteps.get(position);

        holder.stepShortDescriptionTextView.setText(step.getShortDescription());

    }

    @Override
    public int getItemCount() {

        if (mSteps == null) return 0;

        return mSteps.size();
    }

    public void setSteps(List<Step> steps) {
        this.mSteps = steps;

        if (mSteps != null) {
            notifyDataSetChanged();
        }
    }

    public class StepListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_short_description)
        TextView stepShortDescriptionTextView;

        public StepListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();

            mOnStepItemClickListener.onStepItemClicked(itemPosition);
        }
    }
}
