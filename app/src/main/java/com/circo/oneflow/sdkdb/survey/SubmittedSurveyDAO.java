package com.circo.oneflow.sdkdb.survey;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SubmittedSurveyDAO {

    @Insert
    void insertSubmittedSurvey(SubmittedSurveysTab sst);

    @Query("Select * from SubmittedSurveyTab where survey_id = :id")
    SubmittedSurveysTab findSurveyById(String id);

    @Query("Select * from SubmittedSurveyTab ")
    List<SubmittedSurveysTab> findSurveyAllSurvey();


}
