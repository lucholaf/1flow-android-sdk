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

package com.oneflow.analytics.model.loguser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

import java.util.HashMap;

public class OFLogUserRequest extends OFBaseModel {

    @SerializedName("parameters")
    private HashMap<String,String> parameters;
    @SerializedName("anonymous_user_id")
    private String anonymous_user_id;
    @SerializedName("system_id")
    private String system_id;
    @SerializedName("log_user")
    private Boolean log_user = true;
    @SerializedName("session_id")
    private String session_id;

    public Boolean getLog_user() {
        return log_user;
    }

    public void setLog_user(Boolean log_user) {
        this.log_user = log_user;
    }

    public HashMap<String,String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String,String> parameters) {
        this.parameters = parameters;
    }

    public String getAnonymous_user_id() {
        return anonymous_user_id;
    }

    public void setAnonymous_user_id(String anonymous_user_id) {
        this.anonymous_user_id = anonymous_user_id;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
