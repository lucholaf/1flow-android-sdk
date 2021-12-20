package com.oneflow.analytics.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OFGenericResponse<T>{// extends BaseResponse{


    @SerializedName("result")
    private T result ;

    @SerializedName("success")
    private int success;

    @SerializedName("message")
    private String message;



    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        if(result == null){
            return (T)new String("fake");
        }
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
