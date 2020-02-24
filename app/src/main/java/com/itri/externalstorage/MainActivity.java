package com.itri.externalstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2;
    ImageView iv1;
    TextView tv1;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        iv1 = findViewById(R.id.imageView);
        tv1 = findViewById(R.id.textView);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File file = new File(path, "Derek.jpg"); //storage
                saveFile(file);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                Log.d(TAG, "path: " + path);
                File file = new File(path, "Jerry.jpg");
                saveFile(file);
            }
        });
    }

    public void callMediaScanner(String paths[]){
        MediaScannerConnection.scanFile(MainActivity.this, paths, null, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {

            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.v("掃描圖案索引", "path=" +path+"uri=" + uri);
            }
        });
    }

    public void saveFile(File file){
        Toast.makeText(MainActivity.this, "進入SAVE", Toast.LENGTH_SHORT).show();
        if(!isSDExit())
            Toast.makeText(MainActivity.this, "SD CARD尚未掛載!", Toast.LENGTH_SHORT).show();
        File path=file.getParentFile();

        if(!path.exists())
            path.mkdirs();
        if(file.exists())
            file.delete();
//        Resources resources=getResources();
        InputStream is=getResources().openRawResource(R.drawable.a11);
        try {
            FileOutputStream fos=new FileOutputStream(file);
            int size=is.available();
            byte data[]=new byte[size];
            is.read(data);
            fos.write(data);
            fos.flush();
            is.close();
            fos.close();
            Toast.makeText(MainActivity.this, "1.儲存成功..", Toast.LENGTH_SHORT).show();
            tv1.setText("檔案大小:"+size+"位元組\n檔案位置:"+file.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String paths[] = {file.toString()};
        callMediaScanner(paths);
    }


    public boolean isSDExit(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }
}
