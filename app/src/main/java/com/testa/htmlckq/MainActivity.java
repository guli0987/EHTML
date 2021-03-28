package com.testa.htmlckq;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private ImageView image_View;
    private Button btn_huoquhtml;
    private EditText et_shuruhtml;
    private TextView tv_xianshihtml;
    private TextView tv_ls;
    public static final int SHOW = 0;
    public static final int ERROR = 1;
    public static  int a = 0;
    public static  String b="!!!∑(ﾟДﾟノ)ノ\n发生了一个错误,如有需要请反馈至邮箱：test_a@foxmail.com\n";
    public static  String c="发掘(☄ฺ◣ω◢)☄ฺ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Activity_light.class);
                String ss=  tv_xianshihtml.getText().toString();
                if(c.equals(ss)) {
                    Toast mess = Toast.makeText(MainActivity.this,
                            "米有东西哦，转换失败○○（＞＜）○○", Toast.LENGTH_LONG);
                    mess.show();
                    ss="";
                }
                    intent.putExtra("html",ss);
                    startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //
        View headView=navigationView.getHeaderView(0);
        image_View=headView.findViewById(R.id.imageView);
        //content事件
        btn_huoquhtml=findViewById(R.id.btn_huoquhtml);
        et_shuruhtml=findViewById(R.id.et_shuruhtml);
        tv_xianshihtml=findViewById(R.id.tv_xianshihtml);
        tv_ls=findViewById(R.id.tv_ls);
        btn_huoquhtml.setOnClickListener(this);
        //image_View=findViewById(R.id.imageView);
        image_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast meses = Toast.makeText(MainActivity.this,
                        "照片", Toast.LENGTH_LONG);
                meses.show();*/
                Intent intent=new Intent(MainActivity.this,Activity_app.class);
                startActivity(intent);
            }
        });
    }
    public String showHTML(){
        String shuru=et_shuruhtml.getText().toString();
        shuru=shuru.replace(" ","");
        if(!shuru.equals("")){
        String http=shuru.substring(0,7);
        if(!http.equals("http://")){
            shuru="http://"+shuru;
        }}

        return shuru;
    }
    //第一步，点击获取按钮，若输入网址为空，则访问无效，若不为空，执行线程
    @Override
    public void onClick(View v) {
        //获取网址输入框内容
        String shuru=showHTML();
        String sa="";
        //判断是否非空
        if("".equals(shuru)) {
            Toast mess = Toast.makeText(MainActivity.this,
                    "你空空我也空空，大家都云里雾里的", Toast.LENGTH_LONG);
            mess.show();
        }else {et_shuruhtml.setText(showHTML().substring(7,showHTML().length()));huoqu();//baocun(shuru);
        }
    }
    //保存和读取
    private void baocun(String shuru) {
        //Log.d("MainActivity","duqu()"+duqu());
        int pd=-1;
        String duqus="";
        if(duqu()!=null&&duqu()!=""){
            pd=1;
            duqus=duqu();
        }else{
            pd=0;
        }
        //Log.d("MainActivity","pd"+pd);
        //String wenben=et_bj.getText().toString();
        try {
            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"ehtmldatas");
            if(!file.exists()){
                file.mkdirs();
            }
            FileWriter writer=new FileWriter(Environment.getExternalStorageDirectory()+"/ehtmldatas/internetbars"+".txt");
            //BufferedWriter bw=new BufferedWriter(writer);
            //Log.d("MainActivity","shuru"+shuru);

            //待优化，在txt中筛选，涉及问题文件操作
            if(pd==0){
                //Log.d("MainActivity","shuru+duqu()"+shuru+duqu());
                writer.write(shuru);}else{
                writer.write(shuru+","+duqus);
                //Log.d("MainActivity",shuru);
            }
            //writer.write("");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            /*Toast mess = Toast.makeText(MainActivity.this,
                    "保存成功！", Toast.LENGTH_LONG);
            mess.show();*/
        }

    }
    private String duqu() {
        FileReader reader;
        BufferedReader br;
        StringBuffer sb;
        String sbs="";
        try {
            reader=new FileReader(Environment.getExternalStorageDirectory()+"/ehtmldatas/"+"internetbars.txt");
            br=new BufferedReader(reader);
            sb=new StringBuffer();
            String s=null;
            while((s=br.readLine())!=null) {
                sb.append(s);}
            //Log.d("Activity_edit.this",sb.toString());
            sbs=sb.toString();
            reader.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();}
        return sbs;
    }
    //网址不为空执行获取线程
    private void huoqu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取网址输入框内容
                ///String shuru="http://"+showHTML();
                String shuru=showHTML();
                Message message=new Message();
                try {
                    OkHttpClient cilent = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(shuru)
                            .build();
                    Response response = cilent.newCall(request).execute();
                    String responseData = response.body().string();
                   // Log.d("MainActivity","responseData__________"+responseData);
                    //Message message=new Message();
                    message.what=SHOW;
                    message.obj=responseData;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what=ERROR;
                   // Log.d("MainActivity","responseData__________error");
                }finally {
                    handler.sendMessage(message);
                }

            }
        }).start();
    }
    //传到Handler
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW:
                    //Log.d("MainActivity","hander");
                    String result=(String)msg.obj;
                    glsj(result);
                    break;
                case 1:
                    //String s=(String)msg.obj;
                    tv_xianshihtml.setText(b);
                    break;
                default:
                    Toast.makeText(MainActivity.this,"出现错误",Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;//原本false
        }
    });
    //把获取到的html源码显示
    public void glsj(String wb) {
        StringBuffer stringBuffer=new StringBuffer(wb);
        //1——将>符号加入集合并换行
        ArrayList<String> a=new ArrayList<String>();
        a.add(">");
        Iterator<String> it=a.iterator();
        while(it.hasNext()) {
            String str=(String) it.next();
            int index=wb.indexOf(str);
            /*if(index==-1){
            }*/
            while(index!=-1) {
                stringBuffer.insert(index+1,"\n");
                index=stringBuffer.indexOf(str, index+1);
            }
        }
        //2——将汉字+<符号加入集合并换行
        ArrayList<Integer> a2=new ArrayList<Integer>();
        //正则表达式提取汉字+<
        String regex="[\\u4e00-\\u9fa5]"+"<";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(stringBuffer);
        int index;
        int num=0;
        while(matcher.find()){
            //获取汉字+<后的索引
            index=matcher.end();
            System.out.println(index);
            num++;
            //StringBuffer加上换行符索引会变化，故加一
            a2.add(index+(num-1)*1);
        }
        for(Object o:a2) {
            int e=(int)o;
            stringBuffer.insert(e-1, "\n");
        }
        //Log.d("MainActivity","glsj");
        tv_xianshihtml.setText(stringBuffer);
        //判断保存网址条件，如果不为Loading默认值说明无异常
        String exceptions=tv_xianshihtml.getText().toString();
        //String noexception=et_shuruhtml.getText().toString().trim();
        String noexception=showHTML().substring(7,showHTML().length());
        //Log.d("MainActivity",exceptions);
        //Log.d("MainActivity",b);
        if(!exceptions.equals("Loading")&&!exceptions.equals(b)){
            baocun(noexception);
        }
    }
    //筛选网址下
    public String shaixuan(String xinxi){
        String []strArray=xinxi.split(",");
        StringBuffer sb2=new StringBuffer();
        //ArrayList aa=new ArrayList(strArray);
        for(int i=0;i<strArray.length;i++){
            for(int j=i+1;j<strArray.length;j++){
                if(strArray[i].equals(strArray[j])){
                    strArray[j]="删除";
                }
            }
            if(!strArray[i].equals("删除")){
                sb2.append(strArray[i]+"\n");
            }
           // Log.d("MainActivity",strArray[i]);

        }

        return sb2.toString();
    }

    //筛选网址上

    //查找功能
    private void chazhao(){
        final EditText et_soso=new EditText(this);
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("搜索");
        dialog.setView(et_soso);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input=et_soso.getText().toString();
                if(input.equals("")) {
                    Toast.makeText(MainActivity.this,"空内容怎么搜索哇",Toast.LENGTH_SHORT).show();
                }else {
                    String xs=tv_xianshihtml.getText().toString();
                    SpannableStringBuilder sb = new SpannableStringBuilder(xs);
                    UnderlineSpan underlineSpan = new UnderlineSpan();
                    StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                    BackgroundColorSpan bgColorSpan = new BackgroundColorSpan(Color.parseColor("#FD528D"));
                    int a=xs.indexOf(input);
                    if(a==-1){
                        Toast.makeText(MainActivity.this,"找不到怎么办，在线等，挺急的",Toast.LENGTH_SHORT).show();
                    }
                    while(a!=-1) {
                        bgColorSpan = new BackgroundColorSpan(Color.parseColor("#FD528D"));
                        sb.setSpan(bgColorSpan, a, a+input.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        underlineSpan = new UnderlineSpan();
                        sb.setSpan(underlineSpan, a, a+input.length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        styleSpan = new StyleSpan(Typeface.BOLD);
                        sb.setSpan(styleSpan, a, a+input.length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        a=xs.indexOf(input, a+1);
                    }
                    tv_xianshihtml.setText(sb);
                }

            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id== R.id.action_liulanqi){
            String shuru=showHTML();
            if("".equals(shuru)) {
                Toast mess = Toast.makeText(MainActivity.this,
                        "输入网址不能为空哈", Toast.LENGTH_LONG);
                mess.show();
            }else {
                Intent intent=new Intent();
                intent.setData(Uri.parse(shuru));
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(intent);
            }
            return true;
        }
        if (id == R.id.action_lishi) {
                if(a%2==0){
                    if(duqu()!=""&&duqu()!=null){
                        String sp=duqu();
                        //tv_ls.setText(sp.replace(",","\n"));
                        tv_ls.setText(shaixuan(sp));
                        a++;}
                    else{tv_ls.setText("历史为空");a++;}
                }else {
                tv_ls.setText("");
                a++;}
            return true;
        }
        if (id == R.id.action_clearshuru) {
            Toast.makeText(this,"清除成功！",Toast.LENGTH_SHORT).show();
            et_shuruhtml.setText("");
            tv_xianshihtml.setText("");

            return true;
        }
        if (id == R.id.action_search) {
            //Toast.makeText(this,"soso",Toast.LENGTH_SHORT).show();
            chazhao();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id== R.id.nav_create0){
            Intent intent=new Intent(MainActivity.this,Activity_edit.class);
            intent.putExtra("id","0");
            intent.putExtra("bj","");
            startActivity(intent);
        }
        if (id == R.id.nav_ziliao) {
            /*Intent intent=new Intent(MainActivity.this,test_light.class);
            startActivity(intent);*/
            Toast.makeText(this,"资料馆正在建设阶段✧⁺⸜(●˙▾˙●)⸝⁺✧ ",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_list0) {
                Intent intent=new Intent(MainActivity.this, Activity_List.class);
            startActivity(intent);
        } else if (id == R.id.nav_shezhi) {
                //天哪，还没做呢
            Toast.makeText(this,"莫着急，憋着放大招呢(￢_￢)瞄(￢_￢)瞄",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this,"蟹蟹啦，但是人家还莫有做٩(❛ัᴗ❛ั)好感动",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            //连接到网页留言板
            Intent intent=new Intent();
            intent.setData(Uri.parse("http://188.131.221.105:8080/TestOne/test.jsp"));
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivity(intent);
        }else if (id == R.id.nav_update) {
                Intent intent=new Intent(MainActivity.this, Activity_update.class);
                startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
