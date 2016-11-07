package com.suraksha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsn on 19/7/16.
 */
public class ShakeEventManager implements SensorEventListener {
    private SensorManager sManager;
    private Sensor s;


    private static final int MOV_COUNTS = 2;
    private static double MOV_THRESHOLD = 5;
    private static final float ALPHA = 0.8F;
    private static final int SHAKE_WINDOW_TIME_INTERVAL = 500; // milliseconds
    private boolean isGettingValues = false;

    // Gravity force on x,y,z axis
    private float gravity[] = new float[3];

    private int counter;
    private long firstMovTime;
    private ShakeListener listener;

    private Context ctx;

    List<Float> valueList;

    public ShakeEventManager() {
    }

    public void setListener(ShakeListener listener) {
        this.listener = listener;
    }

    public void init(Context ctx) {
        this.ctx = ctx;
        sManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        s = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        register();
    }

    public void register() {
        valueList = new ArrayList<>();
        sManager.registerListener(this, s, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float maxAcc = calcMaxAcceleration(sensorEvent);
//        Log.d("SwA", "Max Acc ["+maxAcc+"]");
        if (maxAcc >= MOV_THRESHOLD) {

            if (isGettingValues) {
//                Log.d("SwA", "Rec Value [" + maxAcc + "]");
                valueList.add(maxAcc);
                if (listener != null) {
                    listener.onShake();
                }
            }

            if (counter == 0) {
                counter++;
                firstMovTime = System.currentTimeMillis();
//                Log.d("SwA", "First mov..");
            } else {
                long now = System.currentTimeMillis();
                if ((now - firstMovTime) < SHAKE_WINDOW_TIME_INTERVAL)
                    counter++;
                else {
                    resetAllData();
                    counter++;
                    return;
                }

                if (counter == MOV_COUNTS) {
//                    Log.d("SwA", "Max Acc [" + maxAcc + "]");
//                    Log.d("SwA", "Mov counter [" + counter + "]");
                    if (listener != null && !isGettingValues) {
                        listener.onShake();
                    }
                    resetAllData();
                }
            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void deregister() {
        sManager.unregisterListener(this);
    }

    public void setMOV_THRESHOLD(double value) {

        if (value <= 15) {
            MOV_THRESHOLD = value;
        } else if (value > 15 && value < 20) {
            MOV_THRESHOLD = value - 5;
        } else if (value > 20 && value < 30) {
            MOV_THRESHOLD = value - 10;
        } else {
            MOV_THRESHOLD = value - 15;
        }

    }

    public void enableRecordingValues(boolean flag) {
        isGettingValues = flag;
    }

    private float calcMaxAcceleration(SensorEvent event) {
        gravity[0] = calcGravityForce(event.values[0], 0);
        gravity[1] = calcGravityForce(event.values[1], 1);
        gravity[2] = calcGravityForce(event.values[2], 2);

        float accX = event.values[0] - gravity[0];
        float accY = event.values[1] - gravity[1];
        float accZ = event.values[2] - gravity[2];

        float max1 = Math.max(accX, accY);
        return Math.max(max1, accZ);
    }

    // Low pass filter
    private float calcGravityForce(float currentVal, int index) {
        return ALPHA * gravity[index] + (1 - ALPHA) * currentVal;
    }


    private void resetAllData() {
//        Log.d("SwA", "Reset all data");
        counter = 0;
        firstMovTime = System.currentTimeMillis();
    }


    public interface ShakeListener {
        void onShake();
    }

    public void saveToDb() {

        String value = "";

        for (int i = 0; i < valueList.size(); i++) {
            SQLiteDatabase db = DbHandler.getInstance(ctx).getDbObj(1);
            DBDao.getInstance().insertData(valueList.get(i), db);
            value = value + "\n" + valueList.get(i);
        }

// get external storage file reference
        FileWriter writer = null;
        try {
            writer = new FileWriter(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/BackupJSN.txt");
            // Writes the content to the file
            writer.write(value);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
