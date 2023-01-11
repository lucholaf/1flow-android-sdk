package com.oneflow.analytics.sdkdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.oneflow.analytics.model.adduser.OFAddUserResultResponse
import com.oneflow.analytics.model.events.OFRecordEventDAOKT
import com.oneflow.analytics.model.events.OFRecordEventsTabKT
import com.oneflow.analytics.model.loguser.OFLogDAOKT
import com.oneflow.analytics.model.survey.OFSurveyUserInputKT
import com.oneflow.analytics.sdkdb.adduser.OFUserDAO
import com.oneflow.analytics.sdkdb.convertes.OFDataConverterDevice
import com.oneflow.analytics.sdkdb.convertes.OFDataConverterLocation
import com.oneflow.analytics.sdkdb.convertes.OFMapConverter
import com.oneflow.analytics.sdkdb.convertes.OFSurveyUserResponseChildConverter
import com.oneflow.analytics.sdkdb.survey.OFSubmittedSurveyDAO
import com.oneflow.analytics.sdkdb.survey.OFSubmittedSurveysTab
import kotlinx.coroutines.CoroutineScope


@Database(entities = [OFRecordEventsTabKT::class, OFAddUserResultResponse::class, OFSubmittedSurveysTab::class, OFSurveyUserInputKT::class], version = 27)
@TypeConverters(OFMapConverter::class, OFDataConverterLocation::class, OFDataConverterDevice::class, OFSurveyUserResponseChildConverter::class)
public abstract class OFSDKKOTDB: RoomDatabase()
{

    abstract fun eventDAO(): OFRecordEventDAOKT?

    abstract fun submittedSurveyDAO(): OFSubmittedSurveyDAO?

    abstract fun userDAO(): OFUserDAO?

    abstract fun logDAOKT(): OFLogDAOKT?


    companion object {

        @Volatile
        private var INSTANCE : OFSDKKOTDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope):OFSDKKOTDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        OFSDKKOTDB::class.java,
                        "ofsdkkotdb_database"
                ).build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }
    /*private class OFSDKKOTDBCallback(val scope: CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { ofSDKKOTDB ->
                scope.launch {
                    // if you want to populate database
                    // when RoomDatabase is created
                    // populate here

                }
            }
        }
    }*/
}