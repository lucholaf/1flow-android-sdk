package com.oneflow.analytics.model.survey

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.oneflow.analytics.sdkdb.convertes.OFSurveyUserResponseChildConverter
import java.util.*

@Entity(tableName = "SurveyUserInput")
class OFSurveyUserInputKT {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    @SerializedName("_id")
    var id = 0

    @ColumnInfo(name = "analytic_user_id")
    @SerializedName("analytic_user_id")
    var analytic_user_id: String? = null

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    var user_id: String? = null

    @ColumnInfo(name = "session_id")
    @SerializedName("session_id")
    var session_id: String? = null

    @ColumnInfo(name = "survey_id")
    @SerializedName("survey_id")
    var survey_id: String? = null

    @ColumnInfo(name = "os")
    @SerializedName("os")
    var os: String? = null

    @ColumnInfo(name = "mode")
    @SerializedName("mode")
    var mode: String? = null

    @ColumnInfo(name = "trigger_event")
    @SerializedName("trigger_event")
    var trigger_event: String? = null

    @TypeConverters(OFSurveyUserResponseChildConverter::class)
    @ColumnInfo(name = "answers")
    @SerializedName("answers")
    var answers: ArrayList<OFSurveyUserResponseChild>? = null

    @ColumnInfo(name = "synced")
    @SerializedName("synced")
    var synced = false

    @ColumnInfo(name = "created_on")
    @SerializedName("created_on")
    var createdOn: Long? = null

    @ColumnInfo(name = "tot_duration")
    @SerializedName("tot_duration")
    var totDuration: Int? = null




}