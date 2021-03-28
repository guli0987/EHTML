package com.testa.htmlckq;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_List extends AppCompatActivity{
    //Snackbar
    //private CoordinatorLayout coordinatorLayout;
    //LinearLayout linearLayout;
    private List<BT_list> btlist=new ArrayList<BT_list>();
    Map map=new HashMap();
    ArrayList al;
    String FileName;
    BTAdapter btAdapter;
    //下拉刷新
    SwipeRefreshLayout swipe_refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview);
        initbts();
        //Snackbar
        //coordinatorLayout.findViewById(R.id.snackbar_container);
        //linearLayout.findViewById(R.id.linearLayout);
        final RecyclerView recyclerView=findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //final BTAdapter btAdapter=new BTAdapter(btlist,map);
        btAdapter=new BTAdapter(btlist,map);
        //分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLongClickable(true);
        recyclerView.setAdapter(btAdapter);
        //recycleview点击事件
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                btAdapter.setOnclick(new BTAdapter.ClickInterface() {
                    @Override
                    public void onItemClick(View view, int position,ArrayList arraylist) {
                        //让选中同步
                        al=arraylist;
                        /*for(Object o:arraylist){
                            Log.d("Activity_List","选中————"+o);
                            //Toast.makeText(view.getContext(), "要删除——————"+o, Toast.LENGTH_SHORT).show();
                        }*/

                    }
                    @Override
                    public void onFileName(View view, int position,String TheFileName) {
                        FileName=TheFileName;
                        //Log.d("Activity_List","选中l————"+position+FileName);
                    }

                });

            }
        });
        //刷新下
        swipe_refresh=findViewById(R.id.swipe_refresh);
        swipe_refresh.setSize(SwipeRefreshLayout.DEFAULT);
        //swipe_refresh.setProgressBackgroundColorSchemeColor(R.color.white);
        swipe_refresh.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorPrimary);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message=new Message();
                        message.what=1;
                        handler_list.sendMessage(message);
                    }
                }).start();
            }
        });
        //刷新上
    }
    private Handler handler_list=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    /*String obj=(String)msg.obj;
                    Log.d("Activity_List","hander"+obj);
                    Toast.makeText(Activity_List.this,"刷新完成",Toast.LENGTH_SHORT).show();
                    //btAdapter.notifyDataSetChanged();
                    swipe_refresh.setRefreshing(false);*/
                    break;
                case 1:
                    btlist.clear();
                    initbts();
                    btAdapter.notifyDataSetChanged();
                    /*for(BT_list bt_lists:btlist){
                        Log.d("Activity_List","刷新测试刷新测试"+bt_lists.getName());

                    }*/
                    swipe_refresh.setRefreshing(false);
                    Toast.makeText(Activity_List.this,"刷新完成",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(Activity_List.this,"出现错误",Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;//原本false
        }
    });
    //删除文件P313
    public void delSelect(String filename){
            try {
                // 找到文件所在的路径并删除该文件
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ehtmldatas");
                File delFile=new File(file,filename);
                delFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    //测试文件名是否存在
private int testFileExist(String filename){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ehtmldatas");
        File delFile=new File(file,filename);
        int exist=-1;
        if(delFile.exists()){
            Toast mess = Toast.makeText(Activity_List.this,
                    "文件存在！", Toast.LENGTH_LONG);
            mess.show();
            exist=1;
        }else{
            /*Toast mess = Toast.makeText(Activity_List.this,
                    "no exist", Toast.LENGTH_LONG);
            mess.show();*/
            exist=0;
        }
        return exist;
    }
    //拷贝文件
public void copySelect(String filename){
    try {
        FileReader fileReader=new FileReader(Environment.getExternalStorageDirectory()+"/ehtmldatas/"+filename);
        BufferedReader bufferedReader=new BufferedReader(fileReader);
        int i=1;
        //String copyfilename=filename+"[拷贝]"+i;
        /*String copyfilename=filename+"[拷贝]"+i;
        while(testFileExist(copyfilename)==1){
                i++;
            copyfilename=copyfilename.substring(0,copyfilename.length()-1)+i;
        }*/
        int fileLastSpot=filename.lastIndexOf(".");
        String header=filename.substring(0,fileLastSpot);
        String middle="[拷贝"+i+"]";
        String tail=filename.substring(fileLastSpot,filename.length());
        String copyfilename=header+middle+tail;
        while(testFileExist(copyfilename)==1){
            i++;
            copyfilename=header+"[拷贝"+i+"]"+tail;
        }
        FileWriter fileWriter=new FileWriter(Environment.getExternalStorageDirectory()+"/ehtmldatas/"+copyfilename);
        BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
        String str;
        while((str=bufferedReader.readLine())!=null){
            bufferedWriter.write(str);
            bufferedWriter.newLine();
        }
        bufferedReader.close();
        bufferedWriter.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

}
//重命名文件
public void renameSelect(String oldfilename,String newfilename){
        //Log.d("Activity_edit","------------"+oldfilename+newfilename);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ehtmldatas");
        File oldFile=new File(file,oldfilename);
        File newFile=new File(file,newfilename);
        //执行重命名
        oldFile.renameTo(newFile);
        Toast.makeText(Activity_List.this, "更名成功", Toast.LENGTH_SHORT).show();


    }
private void showNewFilename(){
        final EditText et_name=new EditText(this);
            et_name.setGravity(Gravity.CENTER);
            et_name.setText(".html");
        AlertDialog.Builder dialog=new AlertDialog.Builder(Activity_List.this);
        dialog.setTitle("更换文件名称");
        dialog.setView(et_name);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = et_name.getText().toString();
                if(testFileExist(input)==1){
                    showNewFilename();
                    Toast.makeText(Activity_List.this, "文件名称不能重复哈", Toast.LENGTH_SHORT).show();
                }else{
                    renameSelect(FileName,input);
                }
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }
    //获取文件夹中文件名目录
    private List ehtnldatas(){
        List<String> list=new ArrayList<>();
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"ehtmldatas");
        File[] files=file.listFiles();
        if(files==null){
            //Log.d("Activity_List","null");
            return null;
        }else{
        for(int i=0;i<files.length;i++){
            //list.add(files[i].getAbsolutePath());
            list.add(files[i].getName());
        }
        //Log.d("Activity_List","000000000000"+list.get(0));
            //Log.d("Activity_List","1111111111111"+list.get(1));
        return list;}
    }
    //将文件名依次加入list列表中
    private void initbts(){
        if(ehtnldatas()==null){

        }else {
            for (int i = 0; i < 1; i++) {
                for (int j=0;j<ehtnldatas().size();j++) {
                    String s=(String)ehtnldatas().get(j);
                    //Log.d("Activity_List",s);
                    BT_list bt_list1=new BT_list(s, R.drawable.tes1);
                    btlist.add(bt_list1);
                    //先设map中每一个filename对应的值为true
                    map.put(s,true);
                }
            }
        }
    }
    //选中删除
    public void delPersonalSelect(){
        for(Object o:al){
            String olo=String.valueOf(o);
            delSelect(olo);
            //
            al.remove(o);
        }
    }
    //删除所有
    public void delAllPersonalSelect(){
        for(BT_list bt_lists:btlist){
            delSelect(bt_lists.getName());
                    }
    }
    //snackbar
    public void SnackbarAction(String one, String two, final String three,final int i){
        Snackbar.make(this.getWindow().getDecorView(), one, Snackbar.LENGTH_LONG)
                .setAction(two, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, three, Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        switch (event) {
                            //调用Dismiss消失
                            case DISMISS_EVENT_MANUAL:
                                break;
                            //有新的SnackBar产生
                            case DISMISS_EVENT_CONSECUTIVE:
                                break;
                            //滑动消失
                            case DISMISS_EVENT_SWIPE:
                                break;
                            //持续时间结束
                            case DISMISS_EVENT_TIMEOUT:
                                if(i==0){delPersonalSelect();
                                    Toast.makeText(Activity_List.this, "删除完成", Toast.LENGTH_SHORT).show();
                                }
                                if(i==1){delAllPersonalSelect();
                                    Toast.makeText(Activity_List.this, "清空完成", Toast.LENGTH_SHORT).show();
                                }
                                if(i==3){delSelect(FileName);
                                    Toast.makeText(Activity_List.this, "删除完成", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            //点击Action
                            case DISMISS_EVENT_ACTION:
                                //Toast.makeText(Activity_List.this, "???", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                        /*@Override
                        public void onShown(Snackbar sb) {
                            super.onShown(sb);
                        }*/
                })
                .show();
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int position = new Long(menuInfo.id).intValue();
        //Log.d("Activity_List","1234");
        /*AdapterView.AdapterContextMenuInfo menuInfo= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedPosition = menuInfo.position-1;
        Log.d("Activity_List","1234"+selectedPosition);*/
        int id = item.getItemId();
       /* if(id==1000){
            Toast.makeText(Activity_List.this, "item+"+id, Toast.LENGTH_SHORT).show();
        }*/
        if(id==1001){
            //删除
            String one="删除"+FileName+"成功";
            String two="撤销";
            String three="撤销成功";
            int i=3;
            SnackbarAction(one,two,three,i);
            //delSelect(FileName);
            //Toast.makeText(Activity_List.this, "item+"+FileName, Toast.LENGTH_SHORT).show();
            //Toast.makeText(Activity_List.this, "删除成功！", Toast.LENGTH_SHORT).show();
        }
        if(id==1002){
            //拷贝
            copySelect(FileName);
            //Log.d("Activity_List.this",FileName);
            //Toast.makeText(Activity_List.this, "拷贝成功！", Toast.LENGTH_SHORT).show();
        }
        /*if(id==1003){
            Toast.makeText(Activity_List.this, "item+"+id, Toast.LENGTH_SHORT).show();
        }*/
        if(id==1004){
            //重命名
            showNewFilename();
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_deleteall, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_select) {
            //Log.d("Activity_List","0000000000000"+al);
            if(al==null||al.isEmpty()){
                Toast.makeText(Activity_List.this, "太调皮了你，都没有选~", Toast.LENGTH_SHORT).show();
            }else{
            String one="删除成功";
            String two="撤销";
            String three="撤销成功";
            int i=0;
            SnackbarAction(one,two,three,i);}
            //View view=findViewById(R.id.delete_select);
            //Snackbar.make(,"",Snackbar.LENGTH_SHORT);
            //getWindow().getDecorView().findViewById(R.id.delete_select);
            //delPersonalSelect();
            //Log.d("Activity_List","bjbjbjbj"+this.getWindow().getDecorView());
            return true;
        }
        if (id == R.id.delete_all) {
            String one="清空成功";
            String two="撤销";
            String three="撤销成功";
            int i=1;
            SnackbarAction(one,two,three,i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
