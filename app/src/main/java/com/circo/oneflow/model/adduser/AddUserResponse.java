package com.circo.oneflow.model.adduser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.circo.oneflow.model.BaseResponse;

public class AddUserResponse extends BaseResponse {

    @SerializedName("result")
    @Expose
    private AddUserResultResponse result;

    public AddUserResultResponse getResult() {
        return result;
    }

    public void setResult(AddUserResultResponse result) {
        this.result = result;
    }
}
