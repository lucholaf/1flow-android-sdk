package com.oneflow.analytics.sdkdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.oneflow.analytics.model.events.OFRecordEventDAO;
import com.oneflow.analytics.model.events.OFRecordEventsTab;
import com.oneflow.analytics.model.loguser.OFLogDAO;
import com.oneflow.analytics.model.survey.OFSurveyUserInput;
import com.oneflow.analytics.sdkdb.convertes.OFDataConverterDevice;
import com.oneflow.analytics.sdkdb.convertes.OFDataConverterLocation;
import com.oneflow.analytics.model.adduser.OFAddUserResultResponse;
import com.oneflow.analytics.sdkdb.adduser.OFUserDAO;
import com.oneflow.analytics.sdkdb.convertes.OFMapConverter;
import com.oneflow.analytics.sdkdb.convertes.OFSurveyUserResponseChildConverter;
import com.oneflow.analytics.sdkdb.survey.OFSubmittedSurveyDAO;
import com.oneflow.analytics.sdkdb.survey.OFSubmittedSurveysTab;
import com.oneflow.analytics.utils.OFConstants;

@Database(entities = {OFRecordEventsTab.class, OFAddUserResultResponse.class, OFSubmittedSurveysTab.class, OFSurveyUserInput.class},version = 4)
@TypeConverters({OFMapConverter.class, OFDataConverterLocation.class, OFDataConverterDevice.class, OFSurveyUserResponseChildConverter.class})
public abstract class OFSDKDB extends RoomDatabase {

    private static volatile OFSDKDB sdkdb;

    //TODO convert this to private ASAP
    public OFSDKDB() {

    }

    private static OFSDKDB initialize(final Context context) {

        return Room.databaseBuilder(context, OFSDKDB.class, OFConstants.DBNAME).fallbackToDestructiveMigration().build();
    }

    public static synchronized OFSDKDB getInstance(Context context) {
        if (sdkdb == null) {
            sdkdb = initialize(context);
        }
        return sdkdb;
    }

    public abstract OFRecordEventDAO eventDAO();

    public abstract OFSubmittedSurveyDAO submittedSurveyDAO();

    public abstract OFUserDAO userDAO();

    public abstract OFLogDAO logDAO();
}