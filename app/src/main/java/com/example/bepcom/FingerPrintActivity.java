package com.example.bepcom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suprema.BioMiniFactory;
import com.suprema.CaptureResponder;
import com.suprema.IBioMiniDevice;
import com.suprema.IUsbEventHandler;

public class FingerPrintActivity extends AppCompatActivity {
    AppCompatButton exit;
    private BioMiniFactory mBioMiniFactory=null;
    private IBioMiniDevice mCurrentDevice=null;

    ImageView right_thumb;
    ImageView Left_thumb;
    ImageView right_index;
    ImageView left_index;

    TextView right_thumb_text;
    TextView left_thumb_text;
    TextView right_index_text;
    TextView left_index_text;
    private FingerPrintActivity mainContext;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        getWindow().setStatusBarColor(ContextCompat.getColor(FingerPrintActivity.this, R.color.colorPrimary));
        inits();
    }

    private void inits() {
        exit = (AppCompatButton) findViewById(R.id.exit);
        right_thumb_text= (TextView) findViewById(R.id.right_thumb_text);
        left_thumb_text= (TextView) findViewById(R.id.Left_thumb_text);
        left_index_text= (TextView) findViewById(R.id.left_index_text);
        right_index_text= (TextView) findViewById(R.id.right_index_text);
        mainContext=this;
        boolean b = false;
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        right_thumb_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_thumb= (ImageView) findViewById(R.id.right_Thumb);
                mBioMiniFactory = new BioMiniFactory(mainContext){
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev)
                    {
                        if (event== IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice==null)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt=0;
                                    while (mBioMiniFactory==null && cnt<20){
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory!=null)
                                    {
                                        mCurrentDevice=mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }

                        IBioMiniDevice.CaptureOption captureOption= new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate=true;

                        mCurrentDevice.captureSingle(captureOption, new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o, final Bitmap bitmap, final IBioMiniDevice.TemplateData templateData, final IBioMiniDevice.FingerState fingerState) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.right_Thumb);
                                            if (iv != null) {
                                                iv.setImageBitmap(bitmap);
                                            }
                                        }
                                    }
                                });
                                if (templateData!=null)
                                {

                                }
                                else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev))
                                {  mCurrentDevice = null; }

                                ;
                                return false;
                            }

                        }, false);
                    }
                };

            }
        });

        left_thumb_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_thumb= (ImageView) findViewById(R.id.right_Thumb);
                mBioMiniFactory = new BioMiniFactory(mainContext){
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev)
                    {
                        if (event== IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice==null)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt=0;
                                    while (mBioMiniFactory==null && cnt<20){
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory!=null)
                                    {
                                        mCurrentDevice=mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }


                        IBioMiniDevice.CaptureOption captureOption= new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate=true;

                        mCurrentDevice.captureSingle(captureOption, new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o,final Bitmap bitmap,final IBioMiniDevice.TemplateData templateData,final IBioMiniDevice.FingerState fingerState) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.right_Thumb);
                                            if (iv != null) {
                                                iv.setImageBitmap(bitmap);
                                            }
                                        }
                                    }
                                });
                                if (templateData!=null)
                                {

                                }
                                else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev))
                                {  mCurrentDevice = null; }
                                return false;
                            }

                        },false);
                    }
                };
            }
        });

        right_index_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_thumb= (ImageView) findViewById(R.id.right_Thumb);
                mBioMiniFactory = new BioMiniFactory(mainContext){
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev)
                    {
                        if (event== IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice==null)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt=0;
                                    while (mBioMiniFactory==null && cnt<20){
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory!=null)
                                    {
                                        mCurrentDevice=mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }

                        IBioMiniDevice.CaptureOption captureOption= new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate=true;

                        mCurrentDevice.captureSingle(captureOption, new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o,final Bitmap bitmap,final IBioMiniDevice.TemplateData templateData,final IBioMiniDevice.FingerState fingerState) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.right_Thumb);
                                            if (iv != null) {
                                                iv.setImageBitmap(bitmap);
                                            }
                                        }
                                    }
                                });
                                 if (templateData!=null)
                                {

                                }
                                else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev))
                                {  mCurrentDevice = null; }
                                return false;
                            }

                        }, true);
                    }
                };
            }
        });

        left_index_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_thumb= (ImageView) findViewById(R.id.right_Thumb);
                mBioMiniFactory = new BioMiniFactory(mainContext){
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev)
                    {
                        if (event== IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice==null)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt=0;
                                    while (mBioMiniFactory==null && cnt<20){
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory!=null)
                                    {
                                        mCurrentDevice=mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }

                        IBioMiniDevice.CaptureOption captureOption= new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate=true;

                        mCurrentDevice.captureSingle(captureOption,new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o,final Bitmap bitmap,final IBioMiniDevice.TemplateData templateData,final IBioMiniDevice.FingerState fingerState) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.right_Thumb);
                                            if (iv != null) {
                                                iv.setImageBitmap(bitmap);
                                            }
                                        }
                                    }
                                });
                                if (templateData!=null)
                                {

                                }
                                else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev))
                                {  mCurrentDevice = null; }
                                return false;

                            }

                        }, true);
                    }
                };
            }
        });
    }
}