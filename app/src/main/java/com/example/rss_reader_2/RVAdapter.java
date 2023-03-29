package com.example.rss_reader_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    interface OnNewsClickListener {
        void onNewsClick(News news, int position);
    }

    private final OnNewsClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<News> news_list;

    RVAdapter(Context context, List<News> news_list, OnNewsClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.news_list = news_list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVAdapter.ViewHolder holder, int position) {
        News news = news_list.get(position);

        holder.titleView.setText(news.getNewsName());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onNewsClick(news, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView;

        ViewHolder(View view){
            super(view);
            titleView = view.findViewById(R.id.news_title);
        }
    }
}
