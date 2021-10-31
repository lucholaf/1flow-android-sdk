package com.circo.oneflow.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.circo.oneflow.BuildConfig;
import com.circo.oneflow.R;
import com.circo.oneflow.customwidgets.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Helper {

    //static boolean commanEnable = false;
    //static boolean verbose = commanEnable;
    //static boolean info = commanEnable;
    //static boolean debug = commanEnable;
    //static boolean error = false;
    static boolean builds = false;

    public static String headerKey = "";

    public static String gpsProviderInfo;


    /**
     * This method will return current app version
     * @param context
     * @return
     */
    public static String getAppVersion(Context context){
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getServiceProvider(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "no permission";
        }

        return telephonyManager.getNetworkOperatorName();
    }



    public static <T> ArrayList<T> fromJsonToArrayList(String rawData, Class<T> model) {
        Gson gson = new GsonBuilder().create();
        T gfromat = null;
        ArrayList<T> localArrayList = new ArrayList<>();
        try {
            JSONArray jsonInner = new JSONArray(rawData);
            int i = 0;
            while (i < jsonInner.length()) {
                gfromat = new Gson().fromJson(jsonInner.get(i).toString(), model);
                localArrayList.add(gfromat);
                i++;
            }
        } catch (Exception ex) {
            v("JsonError", "OneFlow Error:"+ex.getMessage());
        }
        return localArrayList;
    }

    public static String getJSONValues(String contents) {
        StringBuilder sb = new StringBuilder();
        try {

            JSONObject jsonObject = new JSONObject(contents.trim());
            Iterator<String> keys = jsonObject.keys();

            v("JSONConverter", "OneFlow JSON TO String keys[" + keys + "]");
            while (keys.hasNext()) {
                String key = keys.next();
                v("JSONConverter", "OneFlow JSON TO String key[" + key + "]");
                //if (jsonObject.get(key) instanceof JSONObject) {
                // do something with jsonObject here
                v("JSONConverter", "OneFlow JSON TO String[" + jsonObject.getString(key) + "]");
                sb.append(jsonObject.getString(key));
                //}
            }
        } catch (JSONException j) {
            e("HELPER", "Error [" + j.getMessage() + "]");
        }
        v("JSONConverter", "OneFlow JSON TO " +
                "String[" + sb.toString() + "]");
        return sb.toString();
    }

    public static String createSignature(String secret, String message) {
        String signature = "";
        try {

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            signature = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()), Base64.DEFAULT);
            signature = signature.replaceAll("\r", "").replaceAll("\n", "");
        } catch (Exception e) {
            signature = "ERROR";
        }
        v("HELPER", "OneFlow checksum signature[" + signature + "]");
        return signature;
    }

    public static String getGpsProviderInfo() {
        return gpsProviderInfo;
    }

    public static void setGpsProviderInfo(String gpsProviderInfo) {
        gpsProviderInfo = gpsProviderInfo;
    }
static int printCharLimit = 4000;
    //Log methods
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {

            //Log.v(tag,"OneFlow msg Length"+msg.length());
            if(msg.length()>printCharLimit){
                Log.v(tag,msg.substring(0,printCharLimit));
                Log.v("continue",msg.substring(printCharLimit,msg.length()));
            }else {
                Log.v(tag, msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if(msg.length()>4075){
                Log.d(tag,msg.substring(0,4075));
                Log.d("continue",msg.substring(4076,msg.length()));
            }else {
                Log.d(tag, msg);
            }
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if(msg.length()>4075){
                Log.i(tag,msg.substring(0,4075));
                Log.i("continue",msg.substring(4076,msg.length()));
            }else {
                Log.i(tag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if(msg.length()>4075){
                Log.e(tag,msg.substring(0,4075));
                Log.e("continue",msg.substring(4076,msg.length()));
            }else {
                Log.e(tag, msg);
            }
        }
    }

    /**
     * Toast with app theme
     * .show() function call not required
     */
    public static void makeText(Context context, String msg, int duration) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.oneflow_sdk_toast, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();

    }


    /*public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "no permission";
        }
        return telephonyManager.getDeviceId();
    }*/

    public static String formatedDate(long milisec, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(milisec));
    }

    public static String validateString(String str) {
        if (str == null) {
            return "NA";
        }
        str = str.trim();

        if (str.isEmpty() || str.length() == 0) {
            return "NA";
        }
        return str;
    }


    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        String deviceId = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (telephonyManager != null) {
                deviceId = telephonyManager.getDeviceId();
                v("Helper", "OneFlow  Telephony DeviceId [" + deviceId + "]");

                try {
                    BigInteger convertedDeviceId = new BigInteger(deviceId);
                    if (deviceId.isEmpty() || convertedDeviceId.compareTo(BigInteger.ZERO) == 0) {
                        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        v("Helper", "OneFlow AndriodId [" + deviceId + "]");
                    }
                } catch (Exception e) {
                    deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }

            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }

        } catch (Exception ex) {
            e("Helper", "OneFlow DeviceId ex[" + ex.getMessage() + "]");
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//            deviceId = "device id not accessible";
            v("Helper", "OneFlow AndriodId EX [" + deviceId + "]");
            ;//"device id not accessible";
        }
        v("Helper", "OneFlow DeviceId [" + deviceId + "]");
        return deviceId;
    }

    public static void hideKeyboard(Activity mActivity, EditText edt) {
        InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = null;
            if (cm != null) {
                info = cm.getActiveNetworkInfo();
            }
            connected = info != null && info.isConnected();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                int downSpeed = nc.getLinkDownstreamBandwidthKbps();
                v("OneFlow:::::", "Network bandwidth :::: " + downSpeed + "");
                if (downSpeed == 0.0) {
                    connected = false;
                } else {
                    connected = info != null && info.isConnected();
                }
            }
            return connected;
        } catch (Exception e) {
            e("Helper", "OneFlow Error[" + e.getMessage() + "]");
        }
        return connected;
    }

    public static void showAlert1(Context context, String titleStr, String message) {//, View.OnClickListener listenter) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public static void showAlert(Context context, String titleStr, String message) {
        showAlert1(context, titleStr, message, false);
    }

    public static void showAlert1(final Context context, String titleStr, String message, final boolean shouldClose) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        CustomTextView title = (CustomTextView) dialog.findViewById(R.id.selected_title);
        CustomTextView msg = (CustomTextView) dialog.findViewById(R.id.response_msg);
        CustomTextView okBtn = (CustomTextView) dialog.findViewById(R.id.submit_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (shouldClose) {
                    ((Activity) context).finish();
                }
            }
        });
        msg.setText(message);
        title.setText(titleStr);
        /*if(!titleStr.trim().equalsIgnoreCase("")){
            title.setVisibility(View.VISIBLE);
        }*/

        dialog.show();
    }

    public static void showAlertWithIntent(final Context context, String titleStr, String message, final boolean shouldClose, final Intent intent) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        CustomTextView title = (CustomTextView) dialog.findViewById(R.id.selected_title);
        CustomTextView msg = (CustomTextView) dialog.findViewById(R.id.response_msg);
        CustomTextView okBtn = (CustomTextView) dialog.findViewById(R.id.submit_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (shouldClose) {
                    ((Activity) context).startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });
        msg.setText(message);
        title.setText(titleStr);
        /*if(!titleStr.trim().equalsIgnoreCase("")){
            title.setVisibility(View.VISIBLE);
        }*/

        dialog.show();
    }

    public static void showAlertWithIntent2(final Context context, String titleStr, String message, final boolean shouldClose, final Intent intent) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        CustomTextView title = (CustomTextView) dialog.findViewById(R.id.selected_title);
        CustomTextView msg = (CustomTextView) dialog.findViewById(R.id.response_msg);
        CustomTextView okBtn = (CustomTextView) dialog.findViewById(R.id.submit_btn);

        dialog.setCancelable(false);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (shouldClose) {
                    ((Activity) context).startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });
        msg.setText(message);
        title.setText(titleStr);
        /*if(!titleStr.trim().equalsIgnoreCase("")){
            title.setVisibility(View.VISIBLE);
        }*/

        dialog.show();
    }

    

    public static void showAlertWithCancelListener(final Context context, String titleStr, String message, final boolean shouldClose, DialogInterface.OnCancelListener cancelListener) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        CustomTextView title = (CustomTextView) dialog.findViewById(R.id.selected_title);
        CustomTextView msg = (CustomTextView) dialog.findViewById(R.id.response_msg);
        CustomTextView okBtn = (CustomTextView) dialog.findViewById(R.id.submit_btn);

        //dialog.setCancelable(false);
        dialog.setOnCancelListener(cancelListener);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (shouldClose) {
                    ((Activity) context).finish();
                }
            }
        });
        msg.setText(message);
        title.setText(titleStr);
        dialog.show();
    }

    public static void showAlertWithCancelListener2(final Context context, String titleStr, String message, final boolean shouldClose, DialogInterface.OnCancelListener cancelListener) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        CustomTextView title = (CustomTextView) dialog.findViewById(R.id.selected_title);
        CustomTextView msg = (CustomTextView) dialog.findViewById(R.id.response_msg);
        CustomTextView okBtn = (CustomTextView) dialog.findViewById(R.id.submit_btn);

        dialog.setCancelable(false);
        dialog.setOnCancelListener(cancelListener);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (shouldClose) {
                    ((Activity) context).finish();
                }
            }
        });
        msg.setText(message);
        title.setText(titleStr);
        dialog.show();
    }

    public static void showAlertClose(final Context context, String titleStr, String message, final boolean shouldClose) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        CustomTextView title = (CustomTextView) dialog.findViewById(R.id.selected_title);
        CustomTextView msg = (CustomTextView) dialog.findViewById(R.id.response_msg);
        CustomTextView okBtn = (CustomTextView) dialog.findViewById(R.id.submit_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (shouldClose) {
                    ((Activity) context).finish();
                }
            }
        });
        msg.setText(message);
        title.setText(titleStr);

        dialog.show();
    }


    public static boolean validateEmail(String email) {
        //final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        final String EMAIL_REGEX = "^(.+)@(.+)$";

        Pattern pattern;

        Matcher matcher;

        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

        matcher = pattern.matcher(email);
        return matcher.matches();
    }

   
    public static String validateStringeReturnEmpty(String str) {
        if (str == null) {
            return "";
        }
        str = str.trim();

        if (str.isEmpty() || str.length() == 0) {
            return "";
        }
        return str;
    }

    public static String maskString(int startMask, int endMask, String inputString) {
        String outputString = "";


        int total = inputString.length();
        int masklen = total - (startMask + endMask);
        StringBuffer maskedbuf = new StringBuffer(inputString.substring(0, startMask));
        for (int i = 0; i < masklen; i++) {
            maskedbuf.append('X');
        }

        maskedbuf.append(inputString.substring(startMask + masklen, total));
        outputString = maskedbuf.toString();

        return outputString;
    }


   

    /**
     * Function to convert string to title case
     *
     * @param string - Passed string
     */
    public static String toTitleCase(String string) {

        // Check if String is null
        if (string == null) {

            return null;
        }

        boolean whiteSpace = true;

        StringBuilder builder = new StringBuilder(string); // String builder to store string
        final int builderLength = builder.length();

        // Loop through builder
        for (int i = 0; i < builderLength; ++i) {

            char c = builder.charAt(i); // Get character at builders position

            if (whiteSpace) {

                // Check if character is not white space
                if (!Character.isWhitespace(c)) {

                    // Convert to title case and leave whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    whiteSpace = false;
                }
            } else if (Character.isWhitespace(c)) {

                whiteSpace = true; // Set character is white space

            } else {

                builder.setCharAt(i, Character.toLowerCase(c)); // Set character to lowercase
            }
        }

        return builder.toString(); // Return builders text
    }

    public static Date convertStringToDate(String dateInString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(dateInString);
            v("OneFlow", "OneFlow Date in Helper :::: " + date.toString().trim());
        } catch (ParseException e) {
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return date;
    }

    public static String formatDateIntoCustomFormat(Date dateValue, String format) {
        String formattedDate = "";
        try {
            Calendar cl = Calendar.getInstance();
            cl.setTime(dateValue);
            SimpleDateFormat sdfNewThemeDate = new SimpleDateFormat(format);
            formattedDate = sdfNewThemeDate.format(dateValue);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }

        return formattedDate;
    }


    public static StringBuilder sb;
    public static int counter = 1;

    public static String getJSONAllValues(String jsonRaw) {
        try {

            JSONObject outerMost = new JSONObject(jsonRaw);
            JSONArray jName = outerMost.names();

            v("Helper getJSONAllValues", "OneFlow error value [" + jName.get(0).toString() + "]hasNext[" + jName.length() + "]");
            boolean more = false;
            /*do{*/
            for (int i = 0; i < jName.length(); i++) {

                if (outerMost.get(jName.get(i).toString()) instanceof JSONObject) {

                    getJSONAllValues(outerMost.getString(jName.get(i).toString()));

                } else if (outerMost.get(jName.get(i).toString()) instanceof JSONArray) {

                    JSONArray innerJson = outerMost.getJSONArray(jName.get(i).toString());
                    for (int j = 0; j < innerJson.length(); j++) {
                        if (innerJson.get(j) instanceof JSONObject) {
                            getJSONAllValues(innerJson.getString(j));
                        } else {
                            sb.append((counter++) + ". " + innerJson.getString(j) + "\n");
                        }
                    }

                } else {
                    sb.append((counter++) + ". " + outerMost.getString(jName.get(i).toString()) + "\n");
                }
                v("Helper getJSONAllValues", "OneFlow error value [" + sb.toString() + "]");
            }
            v("Helper getJSONAllValues", "OneFlow error value [" + sb.toString() + "]");
        } catch (JSONException je) {
            if (BuildConfig.DEBUG) {
                je.printStackTrace();
            }
        }
        return sb.toString();
    }

    /*public static ArrayList<Object> breakListIntoChunk(ArrayList<Object> arrayItem, int startPosition, int chunk) {
        ArrayList<Object> newList = new ArrayList<>();
        try {
            if (arrayItem.size() > chunk) {
                for (int i = startPosition; i < chunk; i++) {
                    Object item = arrayItem.get(i);
                    newList.add(item);
                }
            } else {
                newList.addAll(arrayItem);
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return newList;
    }*/


    public static File createLogFile() {
        File fl = null;
        try {
            String filePath = Environment.getExternalStorageDirectory() + File.separator + "OneFlowLog" + File.separator + "log.txt";
            v("Helper", "OneFlow file[creating files]");
            fl = new File(filePath);
            v("Helper", "OneFlow file[creating files]exist[" + fl.exists() + "]");
            if (!fl.exists()) {
                File folderOuter = new File(Environment.getExternalStorageDirectory(), "OneFlowLog");
                folderOuter.mkdir();
                v("Helper", "OneFlow file folder not found [" + folderOuter.exists() + "]");
                if (folderOuter.exists()) {
                    File logFile = new File(folderOuter, "log.txt");
                    logFile.createNewFile();
                    fl = logFile;
                } else {
                    v("Helper", "OneFlow file folder not found");
                }
            }
        } catch (Exception ue) {
            if (BuildConfig.DEBUG)
                ue.printStackTrace();
        }
        return fl;
    }

    public static String writeLogToFile(String body) {

        try {
            v("Helper", "OneFlow file [reached to file writer]");
            String writeText = formatedDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:SSS") + " ===> " + body;
            if (!BuildConfig.DEBUG)
                return "";
            File fl = createLogFile();
            if (fl.exists()) {

                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(fl, true)  //Set true for append mode
                );
                writer.newLine();   //Add new line
                writer.write(writeText);
                writer.close();


            } else {
                v("Helper", "OneFlow file log file not found");
            }

        } catch (Exception e) {
            e("Helper", "OneFlow log file not found");
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return body;
    }
    private String SD_CARD_PATH = "/sdcard/OneFlow/";

   
}

