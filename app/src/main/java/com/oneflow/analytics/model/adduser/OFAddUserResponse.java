package com.oneflow.analytics.model.adduser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseResponse;

public class OFAddUserResponse extends OFBaseResponse {

    @SerializedName("result")
    @Expose
    private OFAddUserResultResponse result;

    public OFAddUserResultResponse getResult() {
        return result;
    }

    public void setResult(OFAddUserResultResponse result) {
        this.result = result;
    }
}
