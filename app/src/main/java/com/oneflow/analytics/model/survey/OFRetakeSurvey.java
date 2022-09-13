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

package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

public class OFRetakeSurvey extends OFBaseModel {
    @SerializedName("retake_input_value")
    private Long retake_input_value;
    @SerializedName("retake_select_value")
    private String retake_select_value;
    @SerializedName("_id")
    private String _id;

    public Long getRetake_input_value() {
        return retake_input_value;
    }

    public void setRetake_input_value(Long retake_input_value) {
        this.retake_input_value = retake_input_value;
    }

    public String getRetake_select_value() {
        return retake_select_value;
    }

    public void setRetake_select_value(String retake_select_value) {
        this.retake_select_value = retake_select_value;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
