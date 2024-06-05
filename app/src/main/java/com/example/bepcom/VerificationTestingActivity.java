package com.example.bepcom;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.suprema.BioMiniFactory;
import com.suprema.CaptureResponder;
import com.suprema.IBioMiniDevice;
import com.suprema.IUsbEventHandler;

public class VerificationTestingActivity extends AppCompatActivity {

    private static BioMiniFactory mBioMiniFactory = null;
    public IBioMiniDevice mCurrentDevice = null;
    boolean fast_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_testing);


        mBioMiniFactory = new BioMiniFactory(getApplicationContext()) {
            @Override
            public void onDeviceChange(DeviceChangeEvent deviceChangeEvent, Object o) {

                if (deviceChangeEvent == DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mBioMiniFactory != null) {
                               /* if (deviceChangeEvent == IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
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
                                                //printState(getResources().getText(R.string.device_attached));
                                                Log.d(TAG, "mCurrentDevice attached " + mCurrentDevice);
                                                if (mCurrentDevice != null *//*&& mCurrentDevice.getDeviceInfo() != null*//*) {
                                                    Log.d(TAG, "mCurrentDevice attached " + mCurrentDevice);
                                                }
                                            }
                                        }
                                    }).start();
                                } else if (mCurrentDevice != null && deviceChangeEvent == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(o)) {
                                    Log.d(TAG, "mCurrentDevice removed " + mCurrentDevice);
                                    mCurrentDevice = null;
                                }*/

                                mCurrentDevice = mBioMiniFactory.getDevice(0);

                            }

                        }
                    }).start();
                }
            }
        };


    }




//                              mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.ENABLE_AUTOSLEEP, auto_sleep ? 1 : 0));
//                                mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.EXT_TRIGGER, ext_trigger ? 1 : 0));
//                                mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.SCANNING_MODE, crop_mode ? 1 : 0));
//
//                                mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.FAST_MODE, fast_mode ? 1 : 0));
//                                mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.DETECT_FAKE, lfd_level));
//                                mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.SECURITY_LEVEL, security_level));
//                                mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.SENSITIVITY, sensitivity_level));
//                                mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.TIMEOUT, timeout));

}