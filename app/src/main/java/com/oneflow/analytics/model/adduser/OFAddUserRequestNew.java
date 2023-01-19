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

package com.oneflow.analytics.model.adduser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

public class OFAddUserRequestNew extends OFBaseModel {
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("context")
    private AddUserContext addUserContext;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public AddUserContext getAddUserContext() {
        return addUserContext;
    }

    public void setAddUserContext(AddUserContext addUserContext) {
        this.addUserContext = addUserContext;
    }
}
