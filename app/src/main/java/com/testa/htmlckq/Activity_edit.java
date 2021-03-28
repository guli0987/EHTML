package com.testa.htmlckq;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Activity_edit extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private EditText et_bj;
    private String bt_data;
    private String id;
    private int gjl=1;
    private int dd=-1;
    //工具栏转换
    private int i=0;
    //输入法上面工具栏
    private View layout_edit_Container;
    private TextView view_start_Text;
    private TextView view_end_Text;
    private TextView view_first_Text;
    private TextView view_second_Text;
    private TextView view_third_Text;
    private TextView view_fourth_Text;
    private View view_tip_Container;
    private SeekBar view_Seek_Bar;
    //popupwindow
    private PopupWindow popWindow;
    //移动光标
    private boolean mIsCanMoveCursor = false;
    private int mLastSeekBarProgress = 25;
    private ValueAnimator mExtendSeekBarAnimator;
    private ValueAnimator mShrinkSeekBarAnimator;
    //工具栏内容
    ArrayList<String> gjlArraylist=new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdmenu);
        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        layout_edit_Container=findViewById(R.id.layout_edit_container);

        et_bj = findViewById(R.id.et_bj);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String bj = intent.getStringExtra("bj");
        bt_data= intent.getStringExtra("bt_data");
        //Log.d("Activity_edit","idididididiididid"+id);
        //0为未命名文件，1为已有文件名且保存
        if(id.equals("0")){
            et_bj.setText(bj);}
        else if(id.equals("1")){
            et_bj.setText(duquFile(bt_data));
        }else{
            et_bj.setText("");
    }
        if(ContextCompat.checkSelfPermission(Activity_edit.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Activity_edit.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        NavigationView navigationView = findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showAlertDialog(String title, final String message, String positive, String negative, final String inputFilename, final int showCode){
        AlertDialog.Builder dialog1=new AlertDialog.Builder(Activity_edit.this);
        dialog1.setTitle(title);
        dialog1.setMessage(message);
        dialog1.setPositiveButton(positive, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(showCode){
                    case 0:
                        showNewFilename();
                        Toast.makeText(Activity_edit.this, "重命名", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        showNewFilename();
                        break;
                    default:
                        break;
                }

            }
        });
        dialog1.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(showCode){
                    case 0:
                        Toast.makeText(Activity_edit.this, "覆盖成功", Toast.LENGTH_SHORT).show();
                        saveFiles(inputFilename);
                        break;
                    case 1:
                        Toast.makeText(Activity_edit.this, "文件更名成功", Toast.LENGTH_SHORT).show();
                        renameFile(bt_data,inputFilename);
                        bt_data=inputFilename;
                        break;
                    default:
                        break;
                }

            }
        });
        dialog1.show();
    }
    private void showNewFilename(){
        final EditText et_name=new EditText(this);
        //Log.d("Activity_edit","bt_databt_databt_data"+bt_data);
        if(id.equals("1")||bt_data!=null){
            //et_name.setText(bt_data.substring(0,bt_data.length()-4));
            //bt_data.replace(".txt","");
            et_name.setGravity(Gravity.CENTER);
            et_name.setText(bt_data);
        }else{
            //et_name.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
            et_name.setGravity(Gravity.CENTER);
            et_name.setText(".html");
        }
        AlertDialog.Builder dialog=new AlertDialog.Builder(Activity_edit.this);
        dialog.setTitle("保存");
        dialog.setView(et_name);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et_name.getText().toString();
                        //Log.d("Activity_edit",testFileExist(input)+"------------------");
                        if(input.equals("")){
                            Toast.makeText(Activity_edit.this, "文件名称不能为空", Toast.LENGTH_SHORT).show();
                            showNewFilename();
                        }
                        if(id.equals("1")||bt_data!=null){
                            if(!input.equals(bt_data)){
                                showAlertDialog("文件名与之前不符","是否更改文件名","取消","确认",input,1);
                            }else{
                                saveFiles(input);
                            }
                        }else if(id.equals("0")){
                            if(testFileExist(input)==1){
                                showAlertDialog("文件已存在","是否覆盖","重命名","覆盖",input,0);
                            }
                            else {
                                saveFiles(input);
                            }
                        }
                    }
                });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }
    //保存文件
    private void saveFiles(String name) {

        String wenben=et_bj.getText().toString();
        //Log.d("Activity_edit.class",wenben);
        try {
            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"ehtmldatas");
            if(!file.exists()){
                file.mkdirs();
            }
            //FileWriter writer=new FileWriter(Environment.getExternalStorageDirectory()+"/ehtmldatas/"+name+".txt");
            FileWriter writer=new FileWriter(Environment.getExternalStorageDirectory()+"/ehtmldatas/"+name);
            //BufferedWriter bw=new BufferedWriter(writer);
            writer.write(wenben);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Toast mess = Toast.makeText(Activity_edit.this,
                    "保存成功！文件名为："+name, Toast.LENGTH_LONG);
            mess.show();
            bt_data=name;
        }

    }
    //读取文件
    private String duquFile(String name) {
        FileReader reader;
        BufferedReader br;
        StringBuffer sb;
        String sbs="";
        try {
            reader=new FileReader(Environment.getExternalStorageDirectory()+"/ehtmldatas/"+name);
            br=new BufferedReader(reader);
            sb=new StringBuffer();
            String s=null;
            while((s=br.readLine())!=null) {
                sb.append(s+"\n");}
            //Log.d("Activity_edit.this",sb.toString());
            sbs=sb.toString();
            reader.close();
            reader.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();}
        return sbs;
    }
    //文件重命名
    private void renameFile(String oldfilename,String newfilename){
        //Log.d("Activity_edit","------------"+oldfilename+newfilename);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ehtmldatas");
        File oldFile=new File(file,oldfilename);
        File newFile=new File(file,newfilename);
        //执行重命名
        oldFile.renameTo(newFile);
    }
    //拷贝文件
    private void copyFile(String filename){

    }
    //测试文件名是否存在
    private int testFileExist(String filename){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ehtmldatas");
        File delFile=new File(file,filename);
        int exist=-1;
        if(delFile.exists()){
            /*Toast mess = Toast.makeText(Activity_edit.this,
                    "文件存在！", Toast.LENGTH_LONG);
            mess.show();*/
            exist=1;
        }else{
            /*Toast mess = Toast.makeText(Activity_edit.this,
                    "no exist", Toast.LENGTH_LONG);
            mess.show();*/
            exist=0;
        }
        return exist;
    }
    //删除文件
    private void deleteFiles(String filename){
        if(bt_data==null){Toast mess = Toast.makeText(Activity_edit.this,
                "本就没有，何来删除？", Toast.LENGTH_LONG);mess.show();}
        else{
            try {
                // 找到文件所在的路径并删除该文件
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ehtmldatas");
                File delFile=new File(file,filename);
                delFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast mess = Toast.makeText(Activity_edit.this,
                    "删除"+filename+"成功！", Toast.LENGTH_LONG);
            mess.show();}
    }
    //打开并显示文件效果
    private void OpenAndShow(String filename){
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"ehtmldatas");
        File newFile=new File(file,filename);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT>=24) {
        Uri apkUri = FileProvider.getUriForFile(Activity_edit.this, "com.tes.ehtml.fileprovider", newFile);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, "text/*");}
        else{
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(newFile),"text/*");
        }
        (Activity_edit.this).startActivity(intent);

    }


    //菜单界面
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            if(bt_data==null){Toast mess = Toast.makeText(Activity_edit.this,
                    "本就没有，何来删除？", Toast.LENGTH_LONG);mess.show();}
            else{
                deleteFiles(bt_data);
                /*Toast mess = Toast.makeText(Activity_edit.this,
                    "删除成功！", Toast.LENGTH_LONG);
                mess.show();*/}
            return true;
        }
        if (id == R.id.action_save3) {
            showNewFilename();
            return true;
        }
        //插入图片
        if (id == R.id.action_insertPhoto) {
            Toast mess = Toast.makeText(Activity_edit.this,
                    "唉，正在测试，估计没戏", Toast.LENGTH_LONG);mess.show();
            return true;
        }
        if (id == R.id.action_gjl) {
            if(gjl==1){
            GJL();
            showPopWindow();
            gjl=0;
            }else {
                gjlArraylist.clear();
                //关闭工具栏
                popWindow.dismiss();
                gjl=1;
                //重归i为零
                i=0;

            }
            return true;
        }
        if (id == R.id.action_show) {
            //效果
            if(bt_data==null){Toast mess = Toast.makeText(Activity_edit.this,
                    "未保存，不出效果", Toast.LENGTH_LONG);mess.show();}
            else{OpenAndShow(bt_data);}
            return true;
        }
        if (id == R.id.action_clear) {
            //baocun();
            et_bj.setText("");
            Toast mess = Toast.makeText(Activity_edit.this,
                    "清空成功！", Toast.LENGTH_LONG);
            mess.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_liebiao) {
            // Handle the camera action
            Intent intent=new Intent(Activity_edit.this, Activity_List.class);
            startActivity(intent);
        } else if (id == R.id.nav_address) {
            //testFileExist("zhs.html");
            Toast.makeText(this, "努力发掘中>>>", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_hsz) {
            Toast.makeText(this, "<<<努力发掘中", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_create) {
            Intent intent=new Intent(Activity_edit.this, Activity_edit.class);
            intent.putExtra("id","0");
            intent.putExtra("bj","");
            startActivity(intent);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "拒绝权限将无法正常使用该应用", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
    //输入法工具栏
    private void showPopWindow() {
        View popupView = getLayoutInflater().inflate(R.layout.layout_view, null);
        //获取
        view_start_Text = popupView.findViewById(R.id.view_start_text);
        view_end_Text = popupView.findViewById(R.id.view_end_text);
        view_first_Text = popupView.findViewById(R.id.view_first_text);
        view_second_Text = popupView.findViewById(R.id.view_second_text);
        view_third_Text =  popupView.findViewById(R.id.view_third_text);
        view_fourth_Text = popupView.findViewById(R.id.view_fourth_text);
        view_Seek_Bar =  popupView.findViewById(R.id.view_seek_bar);
        view_tip_Container = popupView.findViewById(R.id.view_tip_container);
        //点击事件
        view_start_Text.setOnClickListener(this);
        view_end_Text.setOnClickListener(this);
        view_first_Text.setOnClickListener(this);
        view_second_Text.setOnClickListener(this);
        view_third_Text.setOnClickListener(this);
        view_fourth_Text.setOnClickListener(this);
        view_Seek_Bar.setOnSeekBarChangeListener(this);
        popWindow=new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setFocusable(false);
        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); //解决遮盖输入法
        popWindow.showAtLocation(layout_edit_Container, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        //左右转换没有深入理解，不能被四整除的标签无法显示
        String txt="";
        switch (v.getId()) {
            case R.id.view_first_text:
                txt = view_first_Text.getText().toString();
                break;
            case R.id.view_second_text:
                txt = view_second_Text.getText().toString();
                break;
            case R.id.view_third_text:
                txt = view_third_Text.getText().toString();
                break;
            case R.id.view_fourth_text:
                txt = view_fourth_Text.getText().toString();
            case R.id.view_start_text:
                ArrayList<String> startList=gjlArraylist;
                //for(int i=0;i<list.size();i++){
                //String index=String.valueOf();
                //Log.d("Avtivity_edit.class",i+"前end----------"+startList.size());
                switch (i%4){
                    case 0:
                        if(i-8>=0) {//8
                            view_first_Text.setText(startList.get(i -4-4));
                            view_second_Text.setText(startList.get(i -3-4));
                            view_third_Text.setText(startList.get(i -2-4));
                            view_fourth_Text.setText(startList.get(i -1-4));
                            i-=4;
                        }
                        break;
                    case 1:
                        view_first_Text.setText(startList.get(i -4-1));
                        view_second_Text.setText(startList.get(i -4-0));
                        view_third_Text.setText(startList.get(i -4+1));
                        view_fourth_Text.setText(startList.get(i -4+2));
                        i-=1;
                        break;
                    case 2:
                        view_first_Text.setText(startList.get(i -4-2));
                        view_second_Text.setText(startList.get(i -4-1));
                        view_third_Text.setText(startList.get(i -4-0));
                        view_fourth_Text.setText(startList.get(i -4+1));
                        i-=2;
                        break;
                    case 3:
                        view_first_Text.setText(startList.get(i -4-3));
                        view_second_Text.setText(startList.get(i -4-2));
                        view_third_Text.setText(startList.get(i -4-1));
                        view_fourth_Text.setText(startList.get(i -4-0));
                        i-=3;
                        break;
                    default:
                        break;
                }
                //Log.d("Avtivity_edit.class",i+"后end----------"+startList.size());
                break;
            case R.id.view_end_text:
                ArrayList<String> endList=gjlArraylist;
                //Log.d("Avtivity_edit.class",i+"前start----------"+endList.size());
                    int pd=endList.size()%4;//1
                    if(i+4<=endList.size()) {//9
                        view_first_Text.setText(endList.get(i));
                        view_second_Text.setText(endList.get(i + 1));
                        view_third_Text.setText(endList.get(i + 2));
                        view_fourth_Text.setText(endList.get(i + 3));
                        i+=4;}
                    //if(i+4>endList.size()) {//9
                        else{
                        switch (pd) {
                            case 0:
                                break;
                            case 1:
                                view_first_Text.setText(endList.get(endList.size()-1));
                                view_second_Text.setText("");
                                view_third_Text.setText("");
                                view_fourth_Text.setText("");
                                if(i<endList.size()){
                                    i=i+1;
                                }
                                break;
                            case 2:
                                view_first_Text.setText(endList.get(endList.size()-2));
                                view_second_Text.setText(endList.get(endList.size()-1));
                                view_third_Text.setText("");
                                view_fourth_Text.setText("");
                                if(i<endList.size()){
                                    i=i+2;
                                }
                                /*if(endAdd==1){
                                    i=i+2;
                                    endAdd=0;
                                }*/
                                break;
                            case 3:
                                view_first_Text.setText(endList.get(endList.size()-3));
                                view_second_Text.setText(endList.get(endList.size()-2));
                                view_third_Text.setText(endList.get(endList.size()-1));
                                view_fourth_Text.setText("");
                                if(i<endList.size()){
                                    i=i+3;
                                }
                                break;
                            default:
                                break;
                        }
                        //Log.d("Avtivity_edit.class",i+"soso----------"+endList.size());
                    }
                //Log.d("Avtivity_edit.class",i+"后start----------"+endList.size());
                //}
                break;
            default:
                break;

        }
        insertTextToEditText(txt);
    }

    private void insertTextToEditText(String txt) {
        if (TextUtils.isEmpty(txt)) return;
        //获取光标位置
        int start=et_bj.getSelectionStart();
        //int end = et_1.getSelectionEnd();
        Editable editable = et_bj.getText();
        editable.insert(start, txt);

    }
    //标签在这里加,感觉用Map阔以，把图片功能添加后try一try
    public ArrayList<String> GJL(){
        gjlArraylist.add("<p>");
        gjlArraylist.add("</p>");
        gjlArraylist.add("<a>");
        gjlArraylist.add("</a>");
        gjlArraylist.add("<title>");
        gjlArraylist.add("</title>");
        gjlArraylist.add("<div>");
        gjlArraylist.add("</div>");
        gjlArraylist.add("<body>");
        gjlArraylist.add("</body>");
        gjlArraylist.add("<table>");
        gjlArraylist.add("</table>");
        gjlArraylist.add("<th>");
        gjlArraylist.add("</th>");
        gjlArraylist.add("<tr>");
        gjlArraylist.add("</tr>");
        gjlArraylist.add("<td>");
        gjlArraylist.add("</td>");
        gjlArraylist.add("<html>");
        gjlArraylist.add("</html>");
        gjlArraylist.add("<head>");
        gjlArraylist.add("</head>");
        gjlArraylist.add("<h1>");
        gjlArraylist.add("</h1>");
        gjlArraylist.add("<style>");
        gjlArraylist.add("</style>");
        gjlArraylist.add("<form>");
        gjlArraylist.add("</form>");
        gjlArraylist.add("<input/>");
        gjlArraylist.add("<br/>");
        gjlArraylist.add("<b>");
        gjlArraylist.add("</b>");
        gjlArraylist.add("<i>");
        gjlArraylist.add("</i>");
/*        gjlArraylist.add("<strong>");
        gjlArraylist.add("</strong>");*/
        gjlArraylist.add("<ul>");
        gjlArraylist.add("</ul>");
        gjlArraylist.add("<li>");
        gjlArraylist.add("</li>");
        gjlArraylist.add("<ol>");
        gjlArraylist.add("</ol>");
        gjlArraylist.add("&nbsp;");
        gjlArraylist.add("&copy;");
        gjlArraylist.add("&lt;");
        gjlArraylist.add("&gt;");
        gjlArraylist.add("&reg;");
        gjlArraylist.add("&trade;");
        return gjlArraylist;
    }
    //SeekBar事件
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mIsCanMoveCursor) {
            moveEditViewCursor(mLastSeekBarProgress > progress);
        }
        mLastSeekBarProgress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mIsCanMoveCursor = false;
        extendSeekBarAnimator();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mIsCanMoveCursor = false;
        shrinkSeekBarAnimator();
    }
    private void moveEditViewCursor(boolean isMoveLeft) {
        int index=et_bj.getSelectionStart();
        if (isMoveLeft) {
            if (index <= 0) return;
            et_bj.setSelection(index - 1);
        } else {
            Editable edit = et_bj.getEditableText();//获取EditText的文字
            if (index >= edit.length()) return;
            et_bj.setSelection(index + 1);
        }
    }
    private void extendSeekBarAnimator() {
        if (mShrinkSeekBarAnimator != null && mShrinkSeekBarAnimator.isRunning()) {
            mShrinkSeekBarAnimator.cancel();
            mShrinkSeekBarAnimator = null;
        }
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth =displayMetrics.widthPixels;
        /*int screenWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getWidth();*/
        final int seekBarWidth = view_Seek_Bar.getWidth();
        mExtendSeekBarAnimator = ValueAnimator.ofInt(screenWidth - seekBarWidth);
        mExtendSeekBarAnimator.setDuration(300);
        mExtendSeekBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                if (view_Seek_Bar == null) return;
                Integer value = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view_Seek_Bar.getLayoutParams();
                layoutParams.width = value + seekBarWidth;
                view_Seek_Bar.setLayoutParams(layoutParams);
            }
        });
        mExtendSeekBarAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                if (view_tip_Container != null) {
                    view_tip_Container.setVisibility(View.INVISIBLE);
                    mIsCanMoveCursor = true;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        mExtendSeekBarAnimator.start();
    }

    private void shrinkSeekBarAnimator() {
        if (mExtendSeekBarAnimator != null && mExtendSeekBarAnimator.isRunning()) {
            mExtendSeekBarAnimator.cancel();
            mExtendSeekBarAnimator = null;
        }

        final int seekBarWidth = view_Seek_Bar.getWidth();
        final int minWidth = getResources().getDimensionPixelOffset(R.dimen.view_seek_bar_min_width);
        mShrinkSeekBarAnimator = ValueAnimator.ofInt(seekBarWidth - minWidth);
        mShrinkSeekBarAnimator.setDuration(300);
        mShrinkSeekBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                if (view_Seek_Bar == null) return;
                Integer value = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view_Seek_Bar.getLayoutParams();
                layoutParams.width = seekBarWidth - value;
                view_Seek_Bar.setLayoutParams(layoutParams);
                int normalProgress = view_Seek_Bar.getMax() / 2;
                int progress = (view_Seek_Bar.getProgress() - normalProgress) * (value / (seekBarWidth - minWidth)) + normalProgress;
                view_Seek_Bar.setProgress(progress);
            }
        });
        mShrinkSeekBarAnimator.start();

        if (view_tip_Container != null) {
            view_tip_Container.setVisibility(View.VISIBLE);
        }
    }

}
