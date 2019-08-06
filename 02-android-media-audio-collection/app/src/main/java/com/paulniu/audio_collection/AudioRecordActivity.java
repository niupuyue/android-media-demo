package com.paulniu.audio_collection;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.paulniu.audio_collection.AudioRecorder.AudioRecorder;
import com.paulniu.audio_collection.AudioRecorder.AudioStatus;
import com.paulniu.audio_collection.AudioRecorder.IAudioCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Coder: niupuyue
 * Date: 2019/8/6
 * Time: 10:02
 * Desc:
 * Version:
 */
public class AudioRecordActivity extends AppCompatActivity implements IAudioCallback {

    private Button btn_start, btn_end, btn_play;
    private TextView tv_status;

    private AudioRecorder audioRecorder;
    private boolean isKeepTime;

    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    private List<String> mPermissionList = new ArrayList<>();
    private final static int ACCESS_FINE_ERROR_CODE = 0x0245;

    private String desFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);

        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);
        btn_play = findViewById(R.id.btn_play);
        tv_status = findViewById(R.id.tv_status);

        audioRecorder = AudioRecorder.getInstance(this);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (audioRecorder.getStatus() == AudioStatus.STATUS_NO_READY) {
                    //初始化录音
                    String fileName = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA).format(new Date());
                    audioRecorder.createDefaultAudio(fileName);
                    audioRecorder.startRecord();
                    isKeepTime = true;
                } else {
                    if (audioRecorder.getStatus() == AudioStatus.STATUS_START) {
                        phoneToPause();
                    } else {
                        audioRecorder.startRecord();
                        isKeepTime = true;
                    }
                }
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndReset();
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioRecorder.play(desFile);
            }
        });
        setPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_SETTINGS,
                        Manifest.permission.RECORD_AUDIO},
                ACCESS_FINE_ERROR_CODE);
    }

    /**
     * 暂停录音和状态修改
     */
    private void phoneToPause() {
        audioRecorder.pauseRecord();
        isKeepTime = false;
    }

    private void finishAndReset() {
        isKeepTime = false;
        audioRecorder.stopRecord();
    }


    /**
     * 权限
     */
    private void setPermissions(String[] permissions, int permissionsCode) {
        mPermissionList.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permission);
                }
            }

            //未授予的权限为空，表示都授予了
            if (mPermissionList.isEmpty()) {

            } else {
                //将List转为数组
                permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, permissionsCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
            if (showRequestPermission) {

            }
        }
    }

    @Override
    public void showPlay(String filePath) {
        this.desFile = filePath;
    }
}
