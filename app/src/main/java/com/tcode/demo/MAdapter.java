package com.tcode.demo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tcode.demo.util.SwipeMenuLayout;

import java.util.List;

public class MAdapter extends RecyclerView.Adapter<MAdapter.MainViewHolder>  {


    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> dataList;

    public MAdapter(Context mContext,List<String> dataList){
        this.mContext = mContext;
        this.dataList = dataList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MainViewHolder(mInflater.inflate(R.layout.item_rv_main,parent,false));

    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuLayout) holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false);
        holder.tv_rv_main.setText(dataList.get(position) + (position % 2 == 0 ? "爱在左" : "情在右"));
        holder.btnUnRead.setVisibility(position % 3 == 0 ? View.GONE : View.VISIBLE);

        //验证长按
        holder.tv_rv_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "longClick", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });

        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        (holder.tv_rv_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "onClick:" + dataList.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });
        //置顶：
        holder.btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mOnSwipeListener){
                    mOnSwipeListener.onTop(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return null == dataList ? 0 : dataList.size();
    }


    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
        void onTop(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }


    class MainViewHolder extends RecyclerView.ViewHolder {

        TextView tv_rv_main;
        Button btnDelete;
        Button btnUnRead;
        Button btnTop;

        public MainViewHolder(View itemView) {
            super(itemView);
            tv_rv_main = (TextView)itemView.findViewById(R.id.tv_rv_main);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnUnRead = (Button) itemView.findViewById(R.id.btnUnRead);
            btnTop = (Button) itemView.findViewById(R.id.btnTop);
        }
    }

}


