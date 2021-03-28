package com.testa.htmlckq;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BTAdapter extends RecyclerView.Adapter<BTAdapter.ViewHolder> {
    ArrayList arrayList=new ArrayList();
    private List<BT_list> mbtList;
    private int aposition;
    //创建了一个点击接口
    private ClickInterface clickInterface;
    private Map mmap;
    public void setOnclick(ClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }
    //这是回调接口
    public interface ClickInterface {
        void onItemClick(View view, int position, ArrayList arraylist);
        void onFileName(View view, int position, String TheFileName);
    }

    //
    public BTAdapter(List<BT_list> btList, Map map){
        mbtList=btList;
        mmap=map;
    }
    //图片点击函数
    public void imageclick(String filename,ViewHolder h){
        int image=-1;
        if(mmap.get(filename).equals(true)){
            image= R.drawable.tes2;
            h.iv_test.setImageResource(image);
            arrayList.add(filename);
            mmap.put(filename,false);
        }else{
            image= R.drawable.tes1;
            h.iv_test.setImageResource(image);
            int index=arrayList.indexOf(filename);
            arrayList.remove(index);
            mmap.put(filename,true);
        }
    }
   //
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_bt,viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);
        //行点击事件
        holder.btview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                BT_list bt_list=mbtList.get(position);
                String onebt=bt_list.getName();
                    Intent intent=new Intent(v.getContext(),Activity_edit.class);
                    intent.putExtra("id","1");
                    intent.putExtra("bt_data",onebt);
                    v.getContext().startActivity(intent);
            }
        });
        //图片点击事件
        holder.iv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                BT_list bt_list=mbtList.get(position);
                //Toast.makeText(v.getContext(),"图片点击事件you clicked——"+bt_list.getName(),Toast.LENGTH_LONG).show();
                imageclick(bt_list.getName(),holder);
                if (clickInterface != null) {
                    clickInterface.onItemClick(v, position,arrayList);
                }
            }
        });

        return holder;
    }
    //移除OnlongClickListenr监听
    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }
    //
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        BT_list bt_list=mbtList.get(i);
        viewHolder.iv_test.setImageResource(bt_list.getImaged());
        viewHolder.tv_btxs.setText(bt_list.getName());
        //bt长按事件
        viewHolder.tv_btxs.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position=viewHolder.getLayoutPosition();
                setAposition(position);
                BT_list bt_list=mbtList.get(position);
                String fileName=bt_list.getName();
                if (clickInterface!= null) {
                    clickInterface.onFileName(v,viewHolder.getLayoutPosition(),fileName);
                }
                return false;
            }
        });
    }
    //
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        //为view设置点击事件
        View btview;
        ImageView iv_test;
        TextView tv_btxs;
        public ViewHolder(@NonNull View view) {
            super(view);
            btview=view;
            iv_test=view.findViewById(R.id.iv_test);
            tv_btxs=view.findViewById(R.id.tv_btxs);
            //
            tv_btxs.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            BT_list bt_list=mbtList.get(getAposition());
            menu.setHeaderTitle(bt_list.getName());
            //String selectedPosition=bt_list.getName();
            //menu.add(1, 1000, 0, "置顶");
            menu.add(1, 1001, 1, "删除");
            menu.add(1, 1002, 2, "拷贝");
            //menu.add(1, 1003, 3, "信息");
            menu.add(1, 1004, 4, "重命名");

        }
    }
    //
    @Override
    public int getItemCount() {
        return mbtList.size();
    }
    public int getAposition() {
        return aposition; }
    public void setAposition(int aposition) {
        this.aposition = aposition; }
}
