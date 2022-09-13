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

package com.oneflow.analytics.sdkdb.survey;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OFSubmittedSurveyDAO {

    @Insert
    void insertSubmittedSurvey(OFSubmittedSurveysTab sst);

    @Query("Select * from SubmittedSurveyTab where survey_id = :id")
    OFSubmittedSurveysTab findSurveyById(String id);

    @Query("Select * from SubmittedSurveyTab")
    List<OFSubmittedSurveysTab> findSurveyAllSurvey();


}
