package com.oneflow.tryskysdk.sdkdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneflow.tryskysdk.R;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;

import java.lang.reflect.Type;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class OneFlowSHP {
    String keyName = "one_flow_temp.db";
    SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private static String key = "";
    private static String iv = "";
    Gson gson;

    public OneFlowSHP(Context context) {

        pref = context.getSharedPreferences(keyName, 0); // 0 - for private mode
        editor = pref.edit();
        //Resources res = context.getResources();
        /*key = res.getString(R.string.encrypt1);
        iv = res.getString(R.string.encrypt2);*/
        gson = new Gson();
    }

    private static final String characterEncoding = "UTF-8";
    private static final String cipherTransformation = "AES/CBC/PKCS7Padding";
    private static final String aesEncryptionAlgorithm = "AES";

    private static String encryptString(String s) {
        try {
            byte[] plainTextbytes = s.getBytes(characterEncoding);
            return Base64.encodeToString(encrypt(plainTextbytes, hexStringToByteArray(key), hexStringToByteArray(iv)), Base64.DEFAULT);
        } catch (Exception e) {
            Helper.e("OneFlow", e.getMessage());
        }
        return s;
    }

    public void storeValue(String key, Object value) {
        SharedPreferences.Editor editor = pref.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof String) {
            // editor.putString(key, (String) value != null && ((String)value).length() > 0 ? encryptString((String)value) : (String)value);
            Helper.v(this.getClass().getName(), "OneAxis token[" + (value) + "]");
            editor.putString(key, (String) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        editor.commit();
    }





    /*public ArrayList<LeaveRecords> getApplyLeaveApiData(String key) {
        String json = pref.getString(key, null);
        Type type = new TypeToken<ArrayList<LeaveRecords>>() {
        }.getType();
        return gson.fromJson(json, type);
    }*/






		/*public UserDataReimbursement retrieveUserReimbursementData(String key){
			String json = pref.getString(key, null);
			UserDataReimbursement obj = gson.fromJson(json, UserDataReimbursement.class);
			return obj;
		}*/




    public AddUserResultResponse getUserDetails() {
        String json = pref.getString(Constants.USERDETAILSHP, null);
        Helper.v("json", "[" + json + "]");
        AddUserResultResponse obj =
                gson.fromJson(json, AddUserResultResponse.class);
        return obj;
    }
    public void setUserDetails(AddUserResultResponse arr) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        String json = gson.toJson(arr);
        Helper.v("json", "[" + json + "]");
        prefsEditor.putString(Constants.USERDETAILSHP, json);
        prefsEditor.apply();
    }
    public void setSurveyList(ArrayList<GetSurveyListResponse> list) {
        SharedPreferences.Editor editor = pref.edit();
        String json = gson.toJson(list);
        editor.putString(Constants.SURVEYLISTSHP, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<GetSurveyListResponse> getSurveyList() {
        String json = pref.getString(Constants.SURVEYLISTSHP, null);
        Type type = new TypeToken<ArrayList<GetSurveyListResponse>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Save and get ArrayList in SharedPreference
     */

    /*public void saveReimbursementHistoryDataArrayList(ArrayList<CapHubInnerData> list, String key) {
        SharedPreferences.Editor editor = pref.edit();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<CapHubInnerData> getReimbursementHistoryDataArrayList(String key) {
        String json = pref.getString(key, null);
        Type type = new TypeToken<ArrayList<CapHubInnerData>>() {
        }.getType();
        return gson.fromJson(json, type);
    }*/


    private static byte[] encrypt(byte[] plainText, byte[] key, byte[] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        plainText = cipher.doFinal(plainText);
        return plainText;
    }

    private static String decryptString(String encryptedText) {
        try {
            byte[] cipheredBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            return new String(decrypt(cipheredBytes, hexStringToByteArray(key), hexStringToByteArray(iv)), characterEncoding);
        } catch (Exception e) {
            Helper.e("HRMSSHP", e.getMessage());
        }
        return "";
    }

    private static byte[] decrypt(byte[] cipherText, byte[] key, byte[] initialVector) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
        cipherText = cipher.doFinal(cipherText);
        return cipherText;
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        String byted = "";
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
            byted += data[i / 2] + ",";
        }
        return data;
    }

   /* public void clearPreference() {
        String token = getStringValue(Constants.shpFCMToken);
        editor.clear();
        editor.commit();
        keepFCMToken(token);
    }*/

    /*public void keepFCMToken(String token) {
        storeValue(Constants.shpFCMToken, token);
    }*/

    /**
     * Will return sharedpreference value of shared key if not found will return NA
     *
     * @param key
     * @return String
     */
    public String getStringValue(String key) {
        //return decryptString(pref.getString(key, "NA"));
        return pref.getString(key, "NA");
    }

    public long getLongValue(String key) {
        return pref.getLong(key, 0);
    }

    public int getIntegerValue(String key) {
        return pref.getInt(key, 0);
    }

    public float getFloatValue(String key) {
        return pref.getFloat(key, 0f);
    }

    public boolean getBooleanValue(String key) {
        return pref.getBoolean(key, false);
    }



}
