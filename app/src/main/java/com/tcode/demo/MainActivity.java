package com.tcode.demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.tcode.demo.util.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private RecyclerView rv_main;
    private MAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initData();
        initView();
    }

    private void init() {
        mContext = this;
        rv_main = (RecyclerView)findViewById(R.id.rv_main);

    }

    private void initData() {
        dataList = new ArrayList<>();
        for (int i = 0;i<20;i++){
            dataList.add(""+i);
        }
    }

    private void initView(){

        linearLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new MAdapter(mContext,dataList);

        rv_main.setLayoutManager(linearLayoutManager);
        rv_main.setAdapter(mAdapter);
//        rv_main.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL)); //垂直分割线


        mAdapter.setOnDelListener(new MAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < dataList.size()) {
                    Toast.makeText(mContext, "删除:" + pos, Toast.LENGTH_SHORT).show();
                    dataList.remove(pos);
                    mAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTop(int pos) {
                if (pos > 0 && pos < dataList.size()) {
                    String positionStr = dataList.get(pos);
                    dataList.remove(positionStr);
                    mAdapter.notifyItemInserted(0);
                    dataList.add(0, positionStr);
                    mAdapter.notifyItemRemoved(pos + 1);
                    if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                        rv_main.scrollToPosition(0);
                    }
                }

            }
        });

        // 可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。我个人觉得意义不大
        rv_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });

    }


}
