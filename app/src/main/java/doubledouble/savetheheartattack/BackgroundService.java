package doubledouble.savetheheartattack;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by leesm10413 on 2016-09-03.
 */
public class BackgroundService extends Service{
    String ip;
    InputStream in;
    DataInputStream din = null;
    OutputStream out;
    DataOutputStream dout = null;
    String messageString;
    boolean mQuit;
    Socket soc = null;
    byte[] messageByte = new byte[1000];

    public void onCreate(){
        super.onCreate();
    }

    public void onDestroy(){
        super.onDestroy();

        Toast.makeText(this, "Service End", Toast.LENGTH_SHORT).show();
        mQuit = true;
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        //Socket soc = null;
        mQuit = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress address = new InetSocketAddress("192.168.1.40", 10419);
                Socket soc = null;
                try{
                    soc = new Socket("192.168.1.40", 10419);
                }catch(IOException e){
                    e.printStackTrace();
                }
                byte[] messageByte = new byte[1000];

                try{
                    Log.d("socket", soc.toString());
                    in = soc.getInputStream();
                    din = new DataInputStream(in);
                    out = soc.getOutputStream();
                    dout = new DataOutputStream(out);

                    dout.write("Text".getBytes("UTF-8"));

                    int bytesRead = din.read(messageByte);
                    messageString += new String(messageByte, 0, bytesRead);
                    //messageString = messageString.split("5")[1];
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = messageString;
                    mHandler.sendMessage(msg);
                    Log.d("message", messageString);
                }catch(Exception e){e.printStackTrace();}
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent){
        return null;
    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == 0){
                String news = (String)msg.obj;
                Toast.makeText(BackgroundService.this, news, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
