package com.oneflow.analytics.model.loguser;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.oneflow.analytics.model.survey.SurveyUserInput;

@Dao
public interface LogDAO {

    @Insert
    public void insertUserInput(SurveyUserInput sur);

    @Query("select * from SurveyUserInput LIMIT 1")
    SurveyUserInput getOfflineUserInput();

    /*@Query("select * from SurveyUserInput")
    ArrayList<SurveyUserInput> getOfflineUserInputList();*/

    @Query("Delete from SurveyUserInput where _id in (:idList)")
    Integer deleteSurvey(Integer[] idList);



}
