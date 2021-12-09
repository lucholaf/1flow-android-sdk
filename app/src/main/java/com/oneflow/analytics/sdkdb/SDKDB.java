package com.oneflow.analytics.sdkdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.oneflow.analytics.model.events.RecordEventDAO;
import com.oneflow.analytics.model.events.RecordEventsTab;
import com.oneflow.analytics.model.loguser.LogDAO;
import com.oneflow.analytics.model.survey.SurveyUserInput;
import com.oneflow.analytics.sdkdb.convertes.DataConverterDevice;
import com.oneflow.analytics.sdkdb.convertes.DataConverterLocation;
import com.oneflow.analytics.model.adduser.AddUserResultResponse;
import com.oneflow.analytics.sdkdb.adduser.UserDAO;
import com.oneflow.analytics.sdkdb.convertes.MapConverter;
import com.oneflow.analytics.sdkdb.convertes.SurveyUserResponseChildConverter;
import com.oneflow.analytics.sdkdb.survey.SubmittedSurveyDAO;
import com.oneflow.analytics.sdkdb.survey.SubmittedSurveysTab;
import com.oneflow.analytics.utils.Constants;

@Database(entities = {RecordEventsTab.class, AddUserResultResponse.class, SubmittedSurveysTab.class, SurveyUserInput.class},version = 2)
@TypeConverters({MapConverter.class, DataConverterLocation.class, DataConverterDevice.class, SurveyUserResponseChildConverter.class})
public abstract class SDKDB extends RoomDatabase {

    private static volatile SDKDB sdkdb;

    //TODO convert this to private ASAP
    public SDKDB() {

    }

    private static SDKDB initialize(final Context context) {

        return Room.databaseBuilder(context, SDKDB.class, Constants.DBNAME).build();
    }

    public static synchronized SDKDB getInstance(Context context) {
        if (sdkdb == null) {
            sdkdb = initialize(context);
        }
        return sdkdb;
    }

    public abstract RecordEventDAO eventDAO();

    public abstract SubmittedSurveyDAO submittedSurveyDAO();

    public abstract UserDAO userDAO();

    public abstract LogDAO logDAO();
}