package com.oneflow.analytics.model.loguser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.oneflow.analytics.model.survey.OFSurveyUserInputKT

@Dao
interface OFLogDAOKT {
    @Insert
    fun insertUserInput(sur: OFSurveyUserInputKT?): Long

    @Query("select * from SurveyUserInput LIMIT 1")
    fun getOfflineUserInput(): OFSurveyUserInputKT?

    @Query("select * from SurveyUserInput where survey_id = :surveyId and user_id = :userId order by created_on desc limit 1")
    fun getSurveyForID(surveyId: String?, userId: String?): OFSurveyUserInputKT?

    @Query("select * from SurveyUserInput where synced = 1 order by created_on desc limit 1")
    fun getLastSyncedSurveyId(): OFSurveyUserInputKT?

    @Query("update SurveyUserInput set synced = :syncNew where _id = :id")
    fun updateUserInput(syncNew: Boolean?, id: Int?): Int

    @Query("update SurveyUserInput set user_id = (:userId) where user_id = 'NA'")
    fun updateUserID(userId: String?): Int

    /*@Query("select * from SurveyUserInput")
    ArrayList<SurveyUserInput> getOfflineUserInputList();*/

    @Query("Delete from SurveyUserInput where _id in (:idList)")
    fun deleteSurvey(idList: Array<Int?>?): Int?
}