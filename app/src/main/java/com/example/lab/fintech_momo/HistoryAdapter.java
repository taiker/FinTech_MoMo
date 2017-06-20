package com.example.lab.fintech_momo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lab on 2017/6/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private List<History> historyList;
    private RecycleViewItmeClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView keyword, date;

        public MyViewHolder(View view) {
            super(view);
            keyword = (TextView) view.findViewById(R.id.text_keyword);
            date = (TextView) view.findViewById(R.id.text_date);
        }
    }


    public HistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        final History history = historyList.get(position);
        holder.keyword.setText(history.getKeyword());
        holder.date.setText(history.getDate());
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                onItemClickListener.onItemClick(history);
            }
        };
        holder.keyword.setOnClickListener(listener);
        holder.date.setOnClickListener(listener);
    }

    public int getItemCount() {
        return historyList.size();
    }

    public RecycleViewItmeClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(RecycleViewItmeClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

