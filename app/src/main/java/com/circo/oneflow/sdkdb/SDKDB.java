package com.circo.oneflow.sdkdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.circo.oneflow.model.events.RecordEventDAO;
import com.circo.oneflow.model.events.RecordEventsTab;
import com.circo.oneflow.model.loguser.LogDAO;
import com.circo.oneflow.model.survey.SurveyUserInput;
import com.circo.oneflow.sdkdb.convertes.DataConverterDevice;
import com.circo.oneflow.sdkdb.convertes.DataConverterLocation;
import com.circo.oneflow.model.adduser.AddUserResultResponse;
import com.circo.oneflow.sdkdb.adduser.UserDAO;
import com.circo.oneflow.sdkdb.convertes.MapConverter;
import com.circo.oneflow.sdkdb.convertes.SurveyUserResponseChildConverter;
import com.circo.oneflow.sdkdb.survey.SubmittedSurveyDAO;
import com.circo.oneflow.sdkdb.survey.SubmittedSurveysTab;
import com.circo.oneflow.utils.Constants;

@Database(entities = {RecordEventsTab.class, AddUserResultResponse.class, SubmittedSurveysTab.class, SurveyUserInput.class},version = 1)
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