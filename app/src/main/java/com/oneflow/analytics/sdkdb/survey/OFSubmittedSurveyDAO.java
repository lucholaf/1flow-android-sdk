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
