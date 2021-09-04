package com.oneflow.tryskysdk.model.adduser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.BaseResponse;

public class AddUserResponse extends BaseResponse {

    @SerializedName("result")
    private AddUserResultResponse result;

    public AddUserResultResponse getResult() {
        return result;
    }

    public void setResult(AddUserResultResponse result) {
        this.result = result;
    }
}
