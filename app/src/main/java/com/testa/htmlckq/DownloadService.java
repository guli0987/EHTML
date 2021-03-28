package com.testa.htmlckq;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;


public class DownloadService extends Service {
    private DownLoadTask downloadTask;
    private String downloadUrl;
    private  DownLoadListener listener=new DownLoadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("正在下载...",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载成功",-1));
            Toast.makeText(DownloadService.this,"下载完成",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载失败",-1));
            Toast.makeText(DownloadService.this,"下载未完成",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask=null;
            Toast.makeText(DownloadService.this,"暂停下载",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask=null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"取消下载",Toast.LENGTH_SHORT).show();
        }
    };
    public DownloadBinder mBinder=new DownloadBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    class DownloadBinder extends Binder{
        public void startDownload(String url){
            if(downloadTask==null){
                downloadUrl=url;
                downloadTask=new DownLoadTask(listener);
                downloadTask.execute(downloadUrl);
                //System.out.println("-------------------");
                startForeground(1,getNotification("下载中...",0));
                Toast.makeText(DownloadService.this,"开始下载",Toast.LENGTH_SHORT).show();
            }
        }
        public void pausedDownload(){
            if(downloadTask!=null){
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload(){
            if(downloadTask!=null){
                downloadTask.cancelDownload();
            }else{
                if(downloadUrl!=null){
                    String fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file=new File(directory+fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this,"下载取消",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    private Notification getNotification(String title,int progress){


        //8.0 以后需要加上channelId 才能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "id";
            String channelName = "通知";
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            getNotificationManager().createNotificationChannel(channel);
        }

        Intent intent=new Intent(this, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"id");
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置通知的小图标，要纯alpha图层的图片进行设置
        builder.setOnlyAlertOnce(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);//支持7.1 25以下设备
        //builder.setAutoCancel(true);//点击了这个通知，这个通知会自动取消掉或getNotificationManager()。cancel（id）
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));//设置通知的大图标，下拉可看
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);//title
        if(progress>0){//Progress
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }

}
