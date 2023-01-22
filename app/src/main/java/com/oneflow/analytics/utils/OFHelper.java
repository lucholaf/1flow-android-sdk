/*
 *  Copyright 2021 1Flow, Inc.
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.oneflow.analytics.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.ColorUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneflow.analytics.BuildConfig;
import com.oneflow.analytics.R;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OFHelper {

    public static boolean commanLogEnable = false;
    //static boolean verbose = commanEnable;
    //static boolean info = commanEnable;
    //static boolean debug = commanEnable;
    //static boolean error = false;
    static boolean builds = false;

    public static String headerKey = "";

    public static String gpsProviderInfo;


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

        }
        return localArrayList;
    }

    public static String getJSONValues(String contents) {
        StringBuilder sb = new StringBuilder();
        try {

            JSONObject jsonObject = new JSONObject(contents.trim());
            Iterator<String> keys = jsonObject.keys();


            while (keys.hasNext()) {
                String key = keys.next();

                sb.append(jsonObject.getString(key));

            }
        } catch (JSONException j) {

        }

        return sb.toString();
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

        int printRange = 4000;

        if (commanLogEnable){//OFConstants.MODE.equalsIgnoreCase("dev")) {

            if (msg.length() > printRange) {
                long range = msg.length() / printRange;
                //       Log.v(tag,"OneAxis length range["+range+"]");
                for (int k = 0; k <= range; k++) {
                    if (k == range) {
                        Log.v(tag, "continueLast::" + msg.substring((k * printRange)));
                        break;
                    }
                    Log.v(tag, "continue[" + k + "]::" + msg.substring((k * printRange), (k * printRange) + printRange) + "]");
                }
            } else {
                Log.v(tag, msg);
            }
        }
    }

    public static void d(String tag, String msg, boolean shouldPrint) {
        if (shouldPrint){//OFConstants.MODE.equalsIgnoreCase("dev")) {
            if (msg.length() > 4075) {
                Log.d(tag, msg.substring(0, 4075));
                Log.d("continue", msg.substring(4076, msg.length()));
            } else {
                Log.d(tag, msg);
            }
        }
    }

    public static void i(String tag, String msg, boolean shouldPrint) {
        if (shouldPrint){//OFConstants.MODE.equalsIgnoreCase("dev")) {
            if (msg.length() > 4075) {
                Log.i(tag, msg.substring(0, 4075));
                Log.i("continue", msg.substring(4076, msg.length()));
            } else {
                Log.i(tag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (commanLogEnable){//OFConstants.MODE.equalsIgnoreCase("dev")) {
            if (msg.length() > 4075) {
                Log.e(tag, msg.substring(0, 4075));
                Log.e("continue", msg.substring(4076, msg.length()));
            } else {
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





    public static String getDeviceId(Context context) {
        String deviceId = "";


        // KAI suggested to use random key 19-feb-2022

        OFOneFlowSHP shp = OFOneFlowSHP.getInstance(context);
        deviceId = shp.getStringValue(OFConstants.SHP_DEVICE_UNIQUE_ID);

       // v("Helper", "OneFlow DeviceId 0[" + deviceId + "]");
        if(deviceId.equalsIgnoreCase("NA")){
            deviceId = UUID.randomUUID().toString().replace("-","").substring(0,24);//getDeviceNewId();
           // v("Helper", "OneFlow DeviceId 1[" + deviceId + "]");
            shp.storeValue(OFConstants.SHP_DEVICE_UNIQUE_ID,deviceId);
        }


        //v("Helper", "OneFlow DeviceId 2[" + deviceId + "]");
        return deviceId;
    }

    public static void hideKeyboard(Activity mActivity, EditText edt) {
        InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }
    public static void showKeyboard(Activity mActivity, EditText edt) {
        InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edt, 0);
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = mgr.getActiveNetworkInfo();

            if (netInfo != null) {
                if (netInfo.isConnected()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }


   /* public static boolean isConnected(Context context) {
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
    }*/

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

    public static void showAlert1(final Context context, String titleStr, String message,
                                  final boolean shouldClose) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        OFCustomTextView title = (OFCustomTextView) dialog.findViewById(R.id.selected_title);
        OFCustomTextView msg = (OFCustomTextView) dialog.findViewById(R.id.response_msg);
        OFCustomTextView okBtn = (OFCustomTextView) dialog.findViewById(R.id.submit_btn);
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

    public static void showAlertWithIntent(final Context context, String titleStr, String
            message, final boolean shouldClose, final Intent intent) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        OFCustomTextView title = (OFCustomTextView) dialog.findViewById(R.id.selected_title);
        OFCustomTextView msg = (OFCustomTextView) dialog.findViewById(R.id.response_msg);
        OFCustomTextView okBtn = (OFCustomTextView) dialog.findViewById(R.id.submit_btn);
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

    public static void showAlertWithIntent2(final Context context, String titleStr, String
            message, final boolean shouldClose, final Intent intent) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        OFCustomTextView title = (OFCustomTextView) dialog.findViewById(R.id.selected_title);
        OFCustomTextView msg = (OFCustomTextView) dialog.findViewById(R.id.response_msg);
        OFCustomTextView okBtn = (OFCustomTextView) dialog.findViewById(R.id.submit_btn);

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


    public static void showAlertWithCancelListener(final Context context, String
            titleStr, String message, final boolean shouldClose, DialogInterface.
                                                           OnCancelListener cancelListener) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        OFCustomTextView title = (OFCustomTextView) dialog.findViewById(R.id.selected_title);
        OFCustomTextView msg = (OFCustomTextView) dialog.findViewById(R.id.response_msg);
        OFCustomTextView okBtn = (OFCustomTextView) dialog.findViewById(R.id.submit_btn);

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

    public static void showAlertWithCancelListener2(final Context context, String
            titleStr, String message, final boolean shouldClose, DialogInterface.
                                                            OnCancelListener cancelListener) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        OFCustomTextView title = (OFCustomTextView) dialog.findViewById(R.id.selected_title);
        OFCustomTextView msg = (OFCustomTextView) dialog.findViewById(R.id.response_msg);
        OFCustomTextView okBtn = (OFCustomTextView) dialog.findViewById(R.id.submit_btn);

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

    public static void showAlertClose(final Context context, String titleStr, String message,
                                      final boolean shouldClose) {//, View.OnClickListener listenter) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        OFCustomTextView title = (OFCustomTextView) dialog.findViewById(R.id.selected_title);
        OFCustomTextView msg = (OFCustomTextView) dialog.findViewById(R.id.response_msg);
        OFCustomTextView okBtn = (OFCustomTextView) dialog.findViewById(R.id.submit_btn);
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

            }

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

            fl = new File(filePath);

            if (!fl.exists()) {
                File folderOuter = new File(Environment.getExternalStorageDirectory(), "OneFlowLog");
                folderOuter.mkdir();

                if (folderOuter.exists()) {
                    File logFile = new File(folderOuter, "log.txt");
                    logFile.createNewFile();
                    fl = logFile;
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


            }

        } catch (Exception e) {

            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return body;
    }



    public static double[] getScreenSize(Activity context) {

        double[] data = new double[3];
        DisplayMetrics dm = new DisplayMetrics();

        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);

        data[0] = wi;
        data[1] = hi;
        data[2] = screenInches;

        return data;
    }

    /**
     * This method will accept HashMap and check for date object if found will convert date object to timestamp (in seconds) and return changed HashMap
     *
     * @param map
     * @return
     */
    public static HashMap<String, Object> checkDateInHashMap(HashMap<String, Object> map) {

        Gson gson = new Gson();
        gson.toJson(map);
        //OFHelper.v("OneFlow", "OneFlow date object before[" + gson.toJson(map) + "]");
       // System.out.println("OneFlow date object before[" + gson.toJson(map) + "]");

        for (String key : map.keySet()) {
            if (map.get(key) instanceof Date || map.get(key) instanceof java.sql.Date) {
                Date dt = (Date) map.get(key);
                map.put(key, dt.getTime() / 1000);
            }
        }
       // OFHelper.v("OneFlow", "OneFlow date object after[" + gson.toJson(map) + "]");
       // System.out.println("OneFlow date object after[" + gson.toJson(map) + "]");
        return map;
    }
    public static String getAlphaHexColor(String color,int per){
        String mainColor = "",hex="";
        String returnColor = "";
        //v("Helper", "OneFlow colors alpha in["+color+"]");
        if(color.length()>0) {
            mainColor = color.substring(color.length() - 6);

            hex = Integer.toHexString(getAlphaNumber(per)).toUpperCase();

            //v("Helper", "OneFlow colors alpha in["+mainColor+"]alpha["+getAlphaNumber(per)+"]["+hex+"]");

            returnColor = "#"+hex+mainColor.toUpperCase();
        }else{
            returnColor = "NA";
        }
        //v("Helper", "OneFlow colors alpha out["+returnColor+"]");
        return returnColor;
    }
    public static String handlerColor(String color){
        String colorNew ="";
       // v("Helper", "OneFlow colors transparancy in[" + color + "]");
        try {

            String tranparancy = "";

            if(color.startsWith("#")){
                if(color.length()>7){
                    tranparancy = color.substring(7,8);
                }
            }else{
                if(color.length()>6){
                    tranparancy = color.substring(6, 8);
                }
            }

            String tempColor;
            if (!color.startsWith("#")) {
                tempColor = color.substring(0, 6);
            } else {
                tempColor = color.substring(1, 7);
            }

            colorNew = "#"+tranparancy+tempColor;
           /* if (!color.startsWith("#")) {
                colorNew = "#" +colorNew;//color;
            }*/
           // v("Helper", "OneFlow colors transparancy out [" + tranparancy + "]tempColor[" + tempColor + "]colorNew[" + colorNew + "]");
        } catch (Exception ex) {
            //styleColor=""+getResources().getColor(R.color.colorPrimaryDark);
        }



        return colorNew;
    }

    public static int getAlphaNumber(int percentage){
         return Math.round((255*percentage)/100);
    }
    public static int manipulateColor(int color, float factor) {
        factor = 1.0f - factor;
       /* int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));*/
        return ColorUtils.blendARGB(color, Color.WHITE, factor);
    }


    public static int lighten(int color, double fraction) {

        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }
    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }
}

