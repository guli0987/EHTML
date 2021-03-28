package com.testa.htmlckq;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

public class Activity_light extends AppCompatActivity {
    //高亮库
    private CodeView codeView;
    String s;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_codeview);
        /*ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/
        //获取信息
        Intent intent=getIntent();
        s=intent.getStringExtra("html");
        //高亮设置
        codeView=findViewById(R.id.codeview1);
        codeView.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor();
        codeView.showCode(s);
        //codeView.showCodeHtmlByClass(Constant.HTML,"code");
        //codeView.showCodeHtmlByCssSelect(Constant.HTML,".code");
        //fab2
        FabSpeedDial fab2=findViewById(R.id.fab2);
        fab2.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                // TODO: Do something with yout menu items, or return false if you don't want to show them
                return true;
            }
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                int id=menuItem.getItemId();
                if(id== R.id.action_save2s){
                    //Log.d("Activity_light.this","11111111");
                    showNewFilename_li();
                   /* Activity_edit.teststatic();*/
                }
                if(id== R.id.action_startbj){
                    Intent intent=new Intent(Activity_light.this,Activity_edit.class);
                    intent.putExtra("id","0");
                    intent.putExtra("bj",s);
                    startActivity(intent);
                }
                return true;
            }
        });

    }
    private void showNewFilename_li(){
        final EditText et_name=new EditText(this);
            et_name.setGravity(Gravity.CENTER);
            et_name.setText(".html");
        AlertDialog.Builder dialog=new AlertDialog.Builder(Activity_light.this);
        dialog.setTitle("快速保存");
        dialog.setView(et_name);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = et_name.getText().toString();
                if (input.equals("")) {
                    Toast.makeText(Activity_light.this, "名称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //Log.d("Activity_light",testFileExist_li(input)+"------------------");
                    if(testFileExist_li(input)==1){
                        Toast.makeText(Activity_light.this, "文件已存在,保存...失败！", Toast.LENGTH_SHORT).show();
                    }else{
                    baocuns_li(input);}
                }
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }
    //测试文件名是否存在
    private int testFileExist_li(String filename){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ehtmldatas");
        File delFile=new File(file,filename);
        int exist=-1;
        if(delFile.exists()){
            /*Toast mess = Toast.makeText(Activity_light.this,
                    "文件存在！", Toast.LENGTH_LONG);
            mess.show();*/
            exist=1;
        }else{
            /*Toast mess = Toast.makeText(Activity_light.this,
                    "no exist", Toast.LENGTH_LONG);
            mess.show();*/
            exist=0;
        }
        return exist;
    }
    private void baocuns_li(String name) {
        try {
            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"ehtmldatas");
            if(!file.exists()){
                file.mkdirs();
            }
            //FileWriter writer=new FileWriter(Environment.getExternalStorageDirectory()+"/ehtmldatas/"+name+".txt");
            FileWriter writer=new FileWriter(Environment.getExternalStorageDirectory()+"/ehtmldatas/"+name);
            //BufferedWriter bw=new BufferedWriter(writer);
            writer.write(s);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Toast mess = Toast.makeText(Activity_light.this,
                    "保存成功！文件名为："+name, Toast.LENGTH_LONG);
            mess.show();
        }

    }

}
