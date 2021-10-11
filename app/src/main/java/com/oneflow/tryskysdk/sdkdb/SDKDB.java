package com.oneflow.tryskysdk.sdkdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.oneflow.tryskysdk.SDKBaseActivity;
import com.oneflow.tryskysdk.model.events.RecordEventDAO;
import com.oneflow.tryskysdk.model.events.RecordEventsTab;
import com.oneflow.tryskysdk.sdkdb.convertes.DataConverterDevice;
import com.oneflow.tryskysdk.sdkdb.convertes.DataConverterLocation;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.sdkdb.adduser.UserDAO;
import com.oneflow.tryskysdk.sdkdb.convertes.MapConverter;
import com.oneflow.tryskysdk.sdkdb.survey.SubmittedSurveyDAO;
import com.oneflow.tryskysdk.sdkdb.survey.SubmittedSurveysTab;
import com.oneflow.tryskysdk.utils.Constants;

@Database(entities = {RecordEventsTab.class, AddUserResultResponse.class, SubmittedSurveysTab.class},version = 1)
@TypeConverters({MapConverter.class, DataConverterLocation.class, DataConverterDevice.class})
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
}