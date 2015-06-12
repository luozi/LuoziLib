package com.luozi.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.luozi.lib.widget.dialog.SpotsDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Luozi
 * Date: 2015-05-22
 * Content 工具类:
 */
public class Utils {

    private static final String TIME_ZONE_CHINA = "Asia/Shanghai";

    /**
     * 屏幕宽度,单位像素(px).
     */
    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        return displayMetrics.widthPixels;
    }

    /**
     * 屏幕高度,单位像素(px).
     */
    public static int getHeight(Context context) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        return displayMetrics.heightPixels;
    }

    // 屏幕像素对象
    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * Loads an integer array asset into a list.
     */
    public static ArrayList<Integer> loadIntegerArray(Resources r, int resNum) {
        int[] vals = r.getIntArray(resNum);
        int size = vals.length;
        ArrayList<Integer> list = new ArrayList<Integer>(size);

        for (int i = 0; i < size; i++) {
            list.add(vals[i]);
        }

        return list;
    }

    /**
     * Loads a String array asset into a list.
     */
    public static ArrayList<String> loadStringArray(Resources r, int resNum) {
        String[] labels = r.getStringArray(resNum);
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(labels));
        return list;
    }

    /**
     * 获取中国时区的time控件.
     */
    public static GregorianCalendar getGregorianCalendar() {
        return new GregorianCalendar(TimeZone.getTimeZone(TIME_ZONE_CHINA));
    }

    /**
     * 显示用户交互等待提示框.
     */
    public static AlertDialog show(Context context, CharSequence title) {
        return show(context, title, true);
    }

    /**
     * 显示用户交互等待提示框.
     */
    public static AlertDialog show(Context context, CharSequence title, boolean cancelable) {
        return show(context, title, cancelable, null);
    }

    /**
     * 显示用户交互等待提示框.
     */
    public static AlertDialog show(Context context, CharSequence title, boolean cancelable,
                                   DialogInterface.OnCancelListener cancelListener) {
        AlertDialog mPd = SpotsDialog.show(context, title, cancelable, cancelListener);
        return mPd;
    }

    public static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * 拨打电话
     *
     * @param activity
     * @param phone    电话号码
     */
    public static void call(Activity activity, String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }

    /**
     * 弹出长时间的Toast提示框.
     */
    public static void showLongToast(Context c, String text) {
        Toast.makeText(c, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 弹出长时间的Toast提示框.
     */
    public static void showLongToast(Context c, int resId) {
        Toast.makeText(c, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 弹出长时间的Toast提示框.
     */
    public static void showShortToast(Context c, String text) {
        Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出长时间的Toast提示框.
     */
    public static void showShortToast(Context c, int resId) {
        Toast.makeText(c, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 验证手机号
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^0?[1][3578][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    /**
     * 获取组合数据,a,b,c -- > abc;
     *
     * @param val 数据集
     * @return String.
     */
    public static String getCombinationValue(Object... val) {
        if ((val == null) || (val.length == 0)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (Object v : val) {
            sb.append(v);
        }
        return sb.toString();
    }

    /**
     * 判断当前网络是否可用.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取String类型的储存信息从SharedPreference.
     *
     * @param key 在 {@link com.luozi.lib.GeneralPreferences} 统一找到.
     */
    public static String getSharedPreference(Context context, String key, String defaultValue) {
        SharedPreferences prefs = GeneralPreferences.getSharedPreferences(context);
        return prefs.getString(key, defaultValue);
    }

    /**
     * 获取Integer类型的储存信息从SharedPreference.
     *
     * @param key 在 {@link com.luozi.lib.GeneralPreferences} 统一找到.
     */
    public static int getSharedPreference(Context context, String key, int defaultValue) {
        SharedPreferences prefs = GeneralPreferences.getSharedPreferences(context);
        return prefs.getInt(key, defaultValue);
    }

    /**
     * 获取Boolean类型的储存信息从SharedPreference.
     *
     * @param key 在 {@link com.luozi.lib.GeneralPreferences} 统一找到.
     */
    public static boolean getSharedPreference(Context context, String key, boolean defaultValue) {
        SharedPreferences prefs = GeneralPreferences.getSharedPreferences(context);
        return prefs.getBoolean(key, defaultValue);
    }

    /**
     * 获取Long类型的储存信息从SharedPreference.
     *
     * @param key 在 {@link com.luozi.lib.GeneralPreferences} 统一找到.
     */
    public static long getSharedPreference(Context context, String key, long defaultValue) {
        SharedPreferences prefs = GeneralPreferences.getSharedPreferences(context);
        return prefs.getLong(key, defaultValue);
    }

    /**
     * 添加String类型的储存信息到SharedPreference.
     *
     * @param key 在 {@link com.luozi.lib.GeneralPreferences} 统一设置.
     */
    public static void setSharedPreference(Context context, String key, String value) {
        SharedPreferences prefs = GeneralPreferences.getSharedPreferences(context);
        prefs.edit().putString(key, value).apply();
    }

    /**
     * 添加Boolean类型的储存信息到SharedPreference.
     *
     * @param key 在 {@link com.luozi.lib.GeneralPreferences} 统一设置.
     */
    public static void setSharedPreference(Context context, String key, boolean value) {
        SharedPreferences prefs = GeneralPreferences.getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 添加Integer类型的储存信息到SharedPreference.
     *
     * @param key 在 {@link com.luozi.lib.GeneralPreferences} 统一设置.
     */
    public static void setSharedPreference(Context context, String key, int value) {
        SharedPreferences prefs = GeneralPreferences.getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 添加Long类型的储存信息到SharedPreference.
     *
     * @param key 在 {@link com.luozi.lib.GeneralPreferences} 统一设置.
     */
    public static void setSharedPreference(Context context, String key, long value) {
        SharedPreferences prefs = GeneralPreferences.getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

}
