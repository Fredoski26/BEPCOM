package com.example.bepcom;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bepcom.constant.Constant;
import com.example.bepcom.model.FingerprintModel;
import com.example.bepcom.model.PassportModel;
import com.example.bepcom.network.Api;
import com.example.bepcom.network.ApiInterface;
import com.google.gson.JsonObject;
import com.suprema.BioMiniFactory;
import com.suprema.CaptureResponder;
import com.suprema.IBioMiniDevice;
import com.suprema.IUsbEventHandler;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FingerPrintActivity extends AppCompatActivity {
    AppCompatButton exit, done;
    private BioMiniFactory mBioMiniFactory = null;
    private IBioMiniDevice mCurrentDevice = null;

    ProgressBar progressBar;
    ImageView right_thumb;
    ImageView Left_thumb;
    ImageView right_index;
    ImageView left_index;
    ImageView right_middle;
    ImageView left_middle;

    Button right_thumb_text;
    Button left_thumb_text;
    Button right_index_text;
    Button left_index_text;


    Button Left_middle_text;
    Button Right_middle_text;
    private FingerPrintActivity mainContext;

    Dictionary<String, Bitmap> fingerprintImage = new Hashtable<>();
    Dictionary<String, String> fingerBase64 = new Hashtable<>();

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
        done = findViewById(R.id.done);
        progressBar = findViewById(R.id.progressBar);
        right_thumb_text = (Button) findViewById(R.id.right_thumb_text);
        left_thumb_text = (Button) findViewById(R.id.Left_thumb_text);
        left_index_text = (Button) findViewById(R.id.left_index_text);
        right_index_text = (Button) findViewById(R.id.right_index_text);
        Right_middle_text = (Button) findViewById(R.id.Right_middle_text);
        Left_middle_text = (Button) findViewById(R.id.Left_middle_text);
        mainContext = this;
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
                right_thumb = (ImageView) findViewById(R.id.right_Thumb);
                mBioMiniFactory = new BioMiniFactory(mainContext) {
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev) {
                        if (event == IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt = 0;
                                    while (mBioMiniFactory == null && cnt < 20) {
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory != null) {
                                        mCurrentDevice = mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }

                        IBioMiniDevice.CaptureOption captureOption = new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate = true;

                        mCurrentDevice.captureSingle(captureOption, new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o, final Bitmap bitmap, final IBioMiniDevice.TemplateData templateData, final IBioMiniDevice.FingerState fingerState) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.right_Thumb);
                                            if (iv != null) {
                                                Bitmap rightThumb = bitmap;
                                                fingerprintImage.put("Right_thumb", rightThumb);
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                                String right_thumb = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                                fingerBase64.put("Right_thumb", right_thumb);
                                                Toast.makeText(FingerPrintActivity.this, "BaseWhat  " + fingerBase64, Toast.LENGTH_SHORT).show();
                                                iv.setImageBitmap(rightThumb);
                                            }
                                        }
                                    }
                                });
                                if (templateData != null) {

                                } else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev)) {
                                    mCurrentDevice = null;
                                }

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
                Left_thumb = (ImageView) findViewById(R.id.Left_Thumb);
                mBioMiniFactory = new BioMiniFactory(mainContext) {
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev) {
                        if (event == IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt = 0;
                                    while (mBioMiniFactory == null && cnt < 20) {
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory != null) {
                                        mCurrentDevice = mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }


                        IBioMiniDevice.CaptureOption captureOption = new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate = true;

                        mCurrentDevice.captureSingle(captureOption, new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o, final Bitmap bitmap, final IBioMiniDevice.TemplateData templateData, final IBioMiniDevice.FingerState fingerState) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.Left_Thumb);
                                            if (iv != null) {
                                                Bitmap LeftThumb = bitmap;
                                                fingerprintImage.put("Left_thumb", LeftThumb);
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                                String Left_thumb = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                                fingerBase64.put("Left_thumb", Left_thumb);
                                                iv.setImageBitmap(LeftThumb);
                                            }
                                        }
                                    }
                                });
                                if (templateData != null) {

                                } else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev)) {
                                    mCurrentDevice = null;
                                }
                                return false;
                            }

                        }, false);
                    }
                };
            }
        });

        right_index_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_index = (ImageView) findViewById(R.id.Right_Index);
                mBioMiniFactory = new BioMiniFactory(mainContext) {
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev) {
                        if (event == IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt = 0;
                                    while (mBioMiniFactory == null && cnt < 20) {
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory != null) {
                                        mCurrentDevice = mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }

                        IBioMiniDevice.CaptureOption captureOption = new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate = true;

                        mCurrentDevice.captureSingle(captureOption, new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o, final Bitmap bitmap, final IBioMiniDevice.TemplateData templateData, final IBioMiniDevice.FingerState fingerState) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.Right_Index);
                                            if (iv != null) {
                                                fingerprintImage.put("right_Index", bitmap);
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                                String right_Index = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                                fingerBase64.put("right_Index", right_Index);
                                                iv.setImageBitmap(bitmap);
                                            }
                                        }
                                    }
                                });
                                if (templateData != null) {

                                } else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev)) {
                                    mCurrentDevice = null;
                                }
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
                left_index = (ImageView) findViewById(R.id.Left_index);
                mBioMiniFactory = new BioMiniFactory(mainContext) {
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev) {
                        if (event == IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt = 0;
                                    while (mBioMiniFactory == null && cnt < 20) {
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory != null) {
                                        mCurrentDevice = mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }

                        IBioMiniDevice.CaptureOption captureOption = new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate = true;

                        mCurrentDevice.captureSingle(captureOption, new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o, final Bitmap bitmap, final IBioMiniDevice.TemplateData templateData, final IBioMiniDevice.FingerState fingerState) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.Left_index);
                                            if (iv != null) {
                                                fingerprintImage.put("Left_index", bitmap);
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                                String left_index = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                                fingerBase64.put("left_index", left_index);
                                                iv.setImageBitmap(bitmap);
                                            }
                                        }
                                    }
                                });
                                if (templateData != null) {

                                } else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev)) {
                                    mCurrentDevice = null;
                                }
                                return false;

                            }

                        }, true);
                    }
                };
            }
        });

        Right_middle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_middle = (ImageView) findViewById(R.id.Right_middle);
                mBioMiniFactory = new BioMiniFactory(mainContext) {
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev) {
                        if (event == IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt = 0;
                                    while (mBioMiniFactory == null && cnt < 20) {
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory != null) {
                                        mCurrentDevice = mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }


                        IBioMiniDevice.CaptureOption captureOption = new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate = true;

                        mCurrentDevice.captureSingle(captureOption, new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o, final Bitmap bitmap, final IBioMiniDevice.TemplateData templateData, final IBioMiniDevice.FingerState fingerState) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.Right_middle);
                                            if (iv != null) {
                                                fingerprintImage.put("right_middle", bitmap);
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                                String right_middle = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                                fingerBase64.put("right_middle", right_middle);
                                                iv.setImageBitmap(bitmap);
                                            }
                                        }
                                    }
                                });
                                if (templateData != null) {

                                } else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev)) {
                                    mCurrentDevice = null;
                                }
                                return false;
                            }

                        }, false);
                    }
                };
            }
        });

        Left_middle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left_middle = (ImageView) findViewById(R.id.Left_middle);
                mBioMiniFactory = new BioMiniFactory(mainContext) {
                    @Override
                    public void onDeviceChange(IUsbEventHandler.DeviceChangeEvent event, Object dev) {
                        if (event == IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int cnt = 0;
                                    while (mBioMiniFactory == null && cnt < 20) {
                                        SystemClock.sleep(1000);
                                        cnt++;
                                    }
                                    if (mBioMiniFactory != null) {
                                        mCurrentDevice = mBioMiniFactory.getDevice(0);
                                    }
                                }
                            }).start();
                        }


                        IBioMiniDevice.CaptureOption captureOption = new IBioMiniDevice.CaptureOption();
                        captureOption.captureTemplate = true;

                        mCurrentDevice.captureSingle(captureOption, new CaptureResponder() {
                            @Override
                            public boolean onCaptureEx(final Object o, final Bitmap bitmap, final IBioMiniDevice.TemplateData templateData, final IBioMiniDevice.FingerState fingerState) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            ImageView iv = findViewById(R.id.Left_middle);
                                            if (iv != null) {
                                                fingerprintImage.put("Left_middle", bitmap);
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                                String Left_middle = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                                fingerBase64.put("Left_middle", Left_middle);
                                                iv.setImageBitmap(bitmap);
                                            }
                                        }
                                    }
                                });
                                if (templateData != null) {

                                } else if
                                (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev)) {
                                    mCurrentDevice = null;
                                }
                                return false;
                            }

                        }, false);
                    }
                };
            }
        });

        done.setOnClickListener(v -> {
           // apiFingerPrintUpload();
        });
    }


    private String convertJson(Map<String, String> dictionary) {
        JSONObject jsonObject = new JSONObject(dictionary);
        String jsonString = jsonObject.toString();
        return jsonString;
    }


    private void apiFingerPrintUpload() {
        String dataImage = "data:image/jpeg;base64,";
        String imageBase64 = dataImage + fingerBase64;
        done.setVisibility(View.GONE);

        ApiInterface api = Api.CreateNodeApi();
        JsonObject object = new JsonObject();
        object.addProperty("file_number", Constant.fileNumber);
        object.addProperty("fingerprints", imageBase64);
        object.addProperty("fingerprint_images", fingerprintImage.toString());

         progressBar.setVisibility(View.VISIBLE);
        final Call<FingerprintModel> fingerprintModel = api.getFingerprint(object);

        fingerprintModel.enqueue(new Callback<FingerprintModel>() {
            @Override
            public void onResponse(@NonNull Call<FingerprintModel> call, @NonNull Response<FingerprintModel> response) {
                if (response.isSuccessful() && response.errorBody() == null) {
                    if (response.code() == 200) {
                        Toast.makeText(FingerPrintActivity.this, "We are all good", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mainContext, "Something went wrong! please try again", Toast.LENGTH_SHORT).show();

                    }

                    if (response.code() == 422) {
                        done.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(mainContext, "Something went wrong! please try again", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<FingerprintModel> call, @NonNull Throwable t) {
                Toast.makeText(mainContext, "Something went wrong! please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.d("Throwable  ------- > ", t.toString());
                done.setVisibility(View.VISIBLE);
                 progressBar.setVisibility(View.GONE);
            }
        });
    }


}