package rs.org.habiprot.alciphron.alci.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import rs.org.habiprot.alciphron.alci.MainActivity;

/**
 * Created by Rade on 5/22/2015.
 */
public class AppUtils {

    private static Calendar cal;

    public static boolean isEmailValid(String pEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(pEmail).matches();
    }

    public static boolean isPasswordValid(String pPassword) {
        return pPassword.length() >= 3;
    }

    public static void hideKeyboard(Activity pActivity) {
        // Check if no view has focus:
        View view = pActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) pActivity.getSystemService(pActivity.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String genBase64(String pBaseString) {
        byte[] mData = new byte[0];
        try {
            mData = pBaseString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
        return "Basic " + Base64.encodeToString(pBaseString.getBytes(), Base64.NO_WRAP);
    }

    public static ProgressDialog showProgress(Context context) {
        return ProgressDialog.show(context, "", "Loading", true);
    }

    public static void hideProgress(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void go2MainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static String dateToString(Date date) {
        cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH) + "-" +
                (cal.get(Calendar.MONTH)+1) + "-" +
                cal.get(Calendar.YEAR);
    }
    public static String timeToString(Date time) {
        cal = GregorianCalendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.HOUR_OF_DAY) + ":" +
                cal.get(Calendar.MINUTE);
    }

    public static String addParameters(HashMap<String,String> map, String boundary) {
        StringBuilder parameters = new StringBuilder(); // multipart body
        String lineEnd = "\r\n";
        String twoHyphens = "--";


        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            parameters.append(twoHyphens + boundary + lineEnd);
            parameters.append("Content-Disposition: form-data; name=" + key + "" + lineEnd);
            parameters.append(lineEnd);
            parameters.append(value + lineEnd);
        }
        parameters.append(twoHyphens + boundary + twoHyphens);

        Log.e("DATA: ", parameters.toString());
        return parameters.toString();
    }


    public static Long getTimestampInMs() {
        Long retTime = System.currentTimeMillis();
        return retTime;
    }
}
