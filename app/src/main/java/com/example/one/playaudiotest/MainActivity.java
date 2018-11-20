package com.example.one.playaudiotest;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button play;
    private Button pause;
    private Button stop;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button)findViewById(R.id.play);
        pause = (Button)findViewById(R.id.pause);
        stop = (Button)findViewById(R.id.stop);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        PermissionUtils.isGrantExternalRW(this,1);

        initMediaPlayer();

    }

    private void initMediaPlayer(){
        try {
            File file = new File(Environment.getExternalStorageDirectory(),"music.mp3");
            mediaPlayer.setDataSource(file.getPath());  //指定音频文件的路径
            mediaPlayer.prepare();  //让Mediaplayer进入到准备状态
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.play:
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();    //开始播放
                }
                break;

            case R.id.pause:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();    //暂停播放
                }
                break;

            case R.id.stop:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();    //停止播放
                    initMediaPlayer();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //检验是否获取权限，如果获取权限，外部存储会处于开放状态，会弹出一个toast提示获得授权
                    String sdCard = Environment.getExternalStorageState();
                    if (sdCard.equals(Environment.MEDIA_MOUNTED)){
                        Toast.makeText(this,"获得授权",Toast.LENGTH_LONG).show();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "没有读取文件权限", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }
}









