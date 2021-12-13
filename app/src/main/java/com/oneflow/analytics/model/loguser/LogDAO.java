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

    @Query("select * from SurveyUserInput where survey_id = :surveyId and user_id = :userId")
    SurveyUserInput getSurveyForID(String surveyId,String userId);

    @Query("update SurveyUserInput set synced = :syncNew where _id = :id")
    int updateUserInput(Boolean syncNew,Integer id);

    @Query("update SurveyUserInput set user_id = (:userId) where user_id = 'NA'")
    int updateUserID(String userId);

    /*@Query("select * from SurveyUserInput")
    ArrayList<SurveyUserInput> getOfflineUserInputList();*/

    @Query("Delete from SurveyUserInput where _id in (:idList)")
    Integer deleteSurvey(Integer[] idList);



}
