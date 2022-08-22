package com.example.lxc.runtimepermissiontest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button makecall = (Button) findViewById(R.id.make_call);
        makecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否授过权
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                   != PackageManager.PERMISSION_GRANTED){
                    //申请授权,弹出权限申请对话框
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                    call();
                }
            }
        });
    }


    private void call(){
        try{
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    //不论哪种结果,回调到该方法,授权结果封装到grantResults
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"你需要打开权限",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    call();
                }else {
                    Toast.makeText(this,"你需要打开权限",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }



    public static final String[] PERMISSIONS_LIST = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static boolean hasRequiredPermission(final Activity activity, final String[] permissions, final int requestCode) {

        ArrayList<String> absenceSet = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                absenceSet.add(permission);
            }
        }

        if (!absenceSet.isEmpty()) {
            String[] requestArray = new String[absenceSet.size()];
            absenceSet.toArray(requestArray);

            ActivityCompat.requestPermissions(activity, requestArray, requestCode);
            return false;
        }
        return true;

    }
}
