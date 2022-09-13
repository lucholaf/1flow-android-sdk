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

package com.oneflow.analytics.model.events;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OFEventUpdate implements Serializable {
    @SerializedName("n")
    private String n;
    @SerializedName("nModified")
    private String nModified;
    @SerializedName("electionId")
    private String electionId;
    @SerializedName("ok")
    private String ok;
    @SerializedName("operationTime")
    private String operationTime;

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getnModified() {
        return nModified;
    }

    public void setnModified(String nModified) {
        this.nModified = nModified;
    }

    

    public String getElectionId() {
        return electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }
}
