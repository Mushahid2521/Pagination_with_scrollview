package com.example.pagination_with_scrollview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int ITEM_PAGE = 20;
    private List<Integer> mNumbers;

    private RecyclerView recyclerView;
    private NumberAdapter adapter;
    public static int page_number, total_page_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        page_number = 1;
        total_page_number = 4;


        mNumbers = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NumberAdapter(this, mNumbers, recyclerView);

        recyclerView.setAdapter(adapter);

        doApiCall();

        adapter.setOnLoadMoreListener(new NumberAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (page_number < total_page_number) {
                    adapter.addNull();
                    adapter.notifyItemInserted(mNumbers.size() - 1);
                    ++page_number;

                    doApiCall();
                }
            }
        });

//        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                //refresh data here
//                Log.e("Refreshing", "..........");
//            }
//
//            @Override
//            public void onLoadMore() {
//                // load more data here
//                ++page_number;
//                Log.e("Getting more",".............");
//                doApiCall();
//            }
//        });

    }

    public void doApiCall(){
        final List<Integer> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<=ITEM_PAGE; i++){
                    items.add(page_number*ITEM_PAGE+i);
                }

                if(page_number>1){
                    mNumbers.remove(mNumbers.size()-1);
                    adapter.notifyItemRemoved(mNumbers.size());
                }

                adapter.addAll(items);
                adapter.notifyItemInserted(items.size());

                adapter.setLoaded();
            }
        }, 1500);
    }




    List<Integer> getNumbers(int page) {
        List<Integer> numbers = new ArrayList<>();
        for(int i=0; i<=ITEM_PAGE; i++){
            numbers.add(page*ITEM_PAGE + i);
        }
        return numbers;
    }
}