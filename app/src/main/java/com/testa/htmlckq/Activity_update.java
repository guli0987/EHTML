package com.testa.htmlckq;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_update extends AppCompatActivity implements View.OnClickListener {
    public static final int Update_TEXT=1;
    public static final int NotUpdate_TEXT=-1;
    public static final int EqualityUpdate_TEXT=0;
    public static final int Update_TEST=11;
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection=new ServiceConnection(){
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder=(DownloadService.DownloadBinder)service;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update);
        Button start=findViewById(R.id.start);
        Button pause=findViewById(R.id.pause);
        Button cancel=findViewById(R.id.cancel);
        Button checkU=findViewById(R.id.checkU);
        Button install=findViewById(R.id.install);
        Button updateNote=findViewById(R.id.updateNote);
        Button howMake=findViewById(R.id.howMake);
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);
        checkU.setOnClickListener(this);
        install.setOnClickListener(this);
        updateNote.setOnClickListener(this);
        howMake.setOnClickListener(this);
        Intent intent=new Intent(this,DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
        if(ContextCompat.checkSelfPermission(Activity_update.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Activity_update.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

    }
    //获取当前程序版本号,right
    private String getVersionName() {
        PackageManager packageManager=getPackageManager();
        PackageInfo packageInfo= null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            //Log.d("MainActivity","本地版本号为："+packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;

    }
    //获取线上程序版本号，对接parseXML(responseData),right1
    private void checkVersion(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //https://gdown.baidu.com/data/wisegame/fd84b7f6746f0b18/baiduyinyue_4802.apk
                //http://192.168.43.127:8080/TesServer/version.xml
                //http://192.168.0.100:8080/TesServer/version.xml
                //http://188.131.221.105:8080/TesServer/version.xml
                try {
                    OkHttpClient cilents = new OkHttpClient();
                    Request requests = new Request.Builder()
                            .url("http://188.131.221.105:8080/TesServer/version.xml")
                            .build();
                    Response response = cilents.newCall(requests).execute();
                    String responseData = response.body().string();
                    parseXML(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //解析xml,right2
    private void parseXML(String xmlData){
        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType=xmlPullParser.getEventType();
            String id="";
            String name="";
            String version="";
            while(eventType!=xmlPullParser.END_DOCUMENT){
                String nodeName=xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:{
                        if("id".equals(nodeName)){
                            id=xmlPullParser.nextText();
                        }else if("name".equals(nodeName)){
                            name=xmlPullParser.nextText();
                        }else if("version".equals(nodeName)){
                            version=xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if("app".equals(nodeName)){
                            String lineversionName=version;
                            //Log.d("MainActivity","线上版本号为："+lineversionName);
                            String localversionName=getVersionName();
                            //比较线上线下版本号
                            int bigVersion=compareVersion(lineversionName, localversionName);
                            //Log.d("MainActivity","bigVersion__"+bigVersion);
                            Message message=new Message();
                            message.what=bigVersion;
                            handler.sendMessage(message);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType=xmlPullParser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断线上线下版本高低,right
    public static int compareVersion(String line, String local) {
        if (line.equals(local)) {
            return 0;
        }else{
            String lineS=line.replace(".","");
            String locaL=local.replace(".","");
            int minLength=Math.min(lineS.length(),locaL.length());
            int flag=996;
            if(lineS.length()>locaL.length()){
                flag=1;
            }else if(lineS.length()<locaL.length()){
                flag=-1;
            }else{
                flag=0;
            }
            int linES=Integer.parseInt(lineS.substring(0,minLength));
            int locAL=Integer.parseInt(locaL.substring(0,minLength));
            int data=886;
            if(linES==locAL){
                //等于，还要判断
                if(flag==1){
                    data=1;
                }else if(flag==0){
                    data=0;
                }else if(flag==-1){
                    data=-1;
                }

            }else if(linES<locAL){
                //线上版本小于线下版本，太令人吃惊了
                data=-1;
            }else{
                //线上版本大于线下版本，需要更新
                data=1;
            }
            /*String linES=lineS.substring(0,minLength);
            String locAL=locaL.substring(0,minLength);*/
            /*System.out.println("linES"+linES);
            System.out.println("locAL"+locAL);*/
            int result=data;
            //System.out.println("result"+result);
            return result;
        }
    }
    //接收哪个版本大，right
private Handler handler=new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case NotUpdate_TEXT:
                Toast.makeText(Activity_update.this,"当前版本比线上版本高",Toast.LENGTH_SHORT).show();
                break;
            case Update_TEXT:
                showUpdateDialog();
                break;
            case EqualityUpdate_TEXT:
                Toast.makeText(Activity_update.this,"当前版本为最新版本！",Toast.LENGTH_SHORT).show();
                break;
            case Update_TEST:
                Toast.makeText(Activity_update.this,"test11测试专用",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(Activity_update.this,"出现错误",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;//原本false
    }
});
    //提示更新
    public void showUpdateDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(Activity_update.this);
        dialog.setTitle("更新提示");
        dialog.setMessage("版本需要更新哦");
        dialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //http://192.168.43.127:8080/TesServer/EHTML.apk
                //http://192.168.0.100:8080/TesServer/EHTML.apk
                String url="http://188.131.221.105:8080/TesServer/EHTML.apk";
                downloadBinder.startDownload(url);
                //installPermission();
                //如果下载成功再跳转安装界面,没搞懂
                //installAPK(Activity_update.this);
            }
        });
        dialog.setNegativeButton("稍后", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Activity_update.this,"您取消了更新，推送将稍后",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        //dialog.setCanceledOnTouchOutside(false);//可选，点击dialog其它地方dismiss无效
        //dialog.setCancelable(false);//可选,点击返回键无效

    }

//方法回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10086){
            //installAPK();
        }
    }
    //安装APK,right
    private void installAPK(Context context){
        //File file= new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "/EHTML.apk");
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Download");
        File newFile=new File(file,"EHTML.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT>=24) {
        Uri apkUri = FileProvider.getUriForFile(context, "com.tes.ehtml.fileprovider", newFile);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");}
        else{
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(newFile),"application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
            if(downloadBinder==null){
                //Log.d("Activity_update","------------null");
                return;
            }
            switch (v.getId()){
                case R.id.start:
                    //Log.d("MainActivity","_____start");
                    //http://192.168.43.127:8080/TesServer/EHTML.apk
                    //http://192.168.0.100:8080/TesServer/EHTML.apk
                    //https://gdown.baidu.com/data/wisegame/fd84b7f6746f0b18/baiduyinyue_4802.apk
                    //http://188.131.221.105:8080/TesServer/EHTML.apk
                    String url="http://188.131.221.105:8080/TesServer/EHTML.apk";
                    downloadBinder.startDownload(url);
                    break;
                case R.id.pause:
                    downloadBinder.pausedDownload();
                    break;
                case R.id.cancel:
                    downloadBinder.cancelDownload();
                    break;
                case R.id.checkU:
                    //检测版本号
                    checkVersion();
                    break;
                case R.id.install:
                    installAPK(Activity_update.this);
                    break;
                case R.id.updateNote:
                    Intent intent1=new Intent(Activity_update.this, Activity_app.class);
                    startActivity(intent1);
                    break;
                case R.id.howMake:
                    Intent intent2=new Intent(Activity_update.this, Activity_make.class);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
