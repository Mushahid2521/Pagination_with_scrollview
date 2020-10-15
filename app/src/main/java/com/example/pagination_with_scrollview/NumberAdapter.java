package com.example.pagination_with_scrollview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter{

    private List<Integer> mNumbers;
    private Context mContext;


    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public NumberAdapter(@NonNull Context context, List<Integer> mNumbers, RecyclerView recyclerView){
        this.mNumbers = mNumbers;
        mContext = context;


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if(!loading && totalItemCount <= (lastVisibleItem+visibleThreshold)){

                    if(onLoadMoreListener!=null){
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_ITEM) {
            Log.e("Binding view", String.valueOf(viewType));
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
            return new NumberViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.progress_item, parent, false);
            return new ProgressViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NumberViewHolder) {
            Integer number = mNumbers.get(position);
            ((NumberViewHolder)holder).number_view.setText(String.valueOf(number));
        } else {
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if(mNumbers!=null){
            return mNumbers.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(mNumbers.get(position)==null) {
            return VIEW_PROG;
        } else {
            return VIEW_ITEM;
        }
    }

    public void addAll(List<Integer> integers){
        mNumbers.addAll(integers);
        notifyDataSetChanged();;
    }
    public void addNull(){
        mNumbers.add(null);
    }

    public void setLoaded(){
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class NumberViewHolder extends RecyclerView.ViewHolder{

        TextView number_view;

        public NumberViewHolder(@NonNull View itemView) {
            super(itemView);
            number_view = itemView.findViewById(R.id.number_);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
}
