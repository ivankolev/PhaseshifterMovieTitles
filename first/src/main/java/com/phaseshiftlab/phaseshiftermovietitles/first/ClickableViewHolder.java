package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ClickableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    OnClickListener onClickListener;


    public ClickableViewHolder(View itemView, OnClickListener onClickListener) {
        super(itemView);
        this.onClickListener = onClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onClickListener.onClick(view, getPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        return onClickListener.onLongClick(view, getPosition());
    }

    public static interface OnClickListener extends View.OnClickListener {
        void onClick(View view, int position);
        boolean onLongClick(View view, int position);
    }
}