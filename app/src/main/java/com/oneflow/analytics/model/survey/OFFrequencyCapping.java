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

public class OFFrequencyCapping extends OFBaseModel {
    @SerializedName("enabled")
    private Boolean enabled;
    @SerializedName("frequency_capping_input_value")
    private String frequency_capping_input_value;
    @SerializedName("frequency_capping_select_value")
    private String frequency_capping_select_value;
    @SerializedName("time_per_user")
    private String time_per_user;
    @SerializedName("_id")
    private String _id;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFrequency_capping_input_value() {
        return frequency_capping_input_value;
    }

    public void setFrequency_capping_input_value(String frequency_capping_input_value) {
        this.frequency_capping_input_value = frequency_capping_input_value;
    }

    public String getFrequency_capping_select_value() {
        return frequency_capping_select_value;
    }

    public void setFrequency_capping_select_value(String frequency_capping_select_value) {
        this.frequency_capping_select_value = frequency_capping_select_value;
    }

    public String getTime_per_user() {
        return time_per_user;
    }

    public void setTime_per_user(String time_per_user) {
        this.time_per_user = time_per_user;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
