package com.oneflow.tryskysdk.model;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;

public class CommanResponse<T> extends BaseResponse{

    @SerializedName("result")
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
