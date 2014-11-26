package com.yixi.window.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppData {

    public static SharedPreferences mSharedPreferences;

    public static float sensitivity = 2.96f;
    public static int interval = 200;

    public AppData(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
        }
    }

    public float sensitivity() {
        String sensitivityStr = mSharedPreferences.getString("sensitivity",
                "10.00");
        sensitivity = Float.parseFloat(sensitivityStr);
        return sensitivity;
    }

    public int angle() {
        String angle = mSharedPreferences.getString("angle", "30");
        return Integer.parseInt(angle);
    }

    public int interval() {
        String interval = mSharedPreferences.getString("interval", "200");
        return Integer.parseInt(interval);
    }

    public int acceleration() {
        String acceleration = mSharedPreferences.getString("acceleration", "5");
        return Integer.parseInt(acceleration);
    }

    public int samplingCount() {
        String samplingCount = mSharedPreferences.getString("samplingCount",
                "30");
        return Integer.parseInt(samplingCount);
    }

    public float samplingPercent() {
        String samplingPercent = mSharedPreferences.getString(
                "samplingPercent", "0.5");
        return Float.parseFloat(samplingPercent);
    }

    public float qualifiedPercent() {
        String qualifiedPercent = mSharedPreferences.getString(
                "qualifiedPercent", "0.2");
        return Float.parseFloat(qualifiedPercent);
    }

    public boolean isMetric() {
        return mSharedPreferences.getString("units", "imperial").equals(
                "metric");
    }

    public boolean isRunning() {
        return mSharedPreferences.getString("exercise_type", "running").equals(
                "running");
    }

    public float getStepLength() {
        try {
            return Float.valueOf(mSharedPreferences.getString("step_length",
                    "50").trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    public float getBodyWeight() {
        try {
            return Float.valueOf(mSharedPreferences.getString("body_weight",
                    "60").trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

}
