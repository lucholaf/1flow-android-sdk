package com.oneflow.analytics.repositories

import android.content.Context
import com.oneflow.analytics.model.events.OFRecordEventsTab
import com.oneflow.analytics.model.events.OFRecordEventsTabKT
import com.oneflow.analytics.sdkdb.OFSDKDB
import com.oneflow.analytics.sdkdb.OFSDKKOTDB
import com.oneflow.analytics.utils.OFConstants.ApiHitType
import com.oneflow.analytics.utils.OFHelper
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class OFEventDBRepoKT {
    fun insertEvents(context: Context, eventName: String?, data: HashMap<String?, Any?>?, value: Int, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("EventDBRepoKT.insertEvents", "OneFlow reached at insertEvent method")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            //val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context,scope)
            val sdkdb = OFSDKDB.getInstance(context)
            val ret = OFRecordEventsTab()
            ret.eventName = eventName
            ret.dataMap = data
            ret.value = value.toString()
            ret.time = Calendar.getInstance().timeInMillis / 1000
            ret.synced = 0
            ret.createdOn = Calendar.getInstance().timeInMillis
            val inserted = sdkdb.eventDAO()?.insertAll(ret)
            OFHelper.v("EventDBRepoKT.insertEvents", "OneFlow inserting events["+eventName+"]["+inserted+"]");
            mrh.onResponseReceived(type,inserted,0L,eventName,null,null)
        }



    }

    /*fun deleteEvents(context: Context, ids: Array<Int?>?, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("EventDBRepoKT.DeleteEvents", "OneFlow reached at delete method")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKKOTDB.getDatabase(context,scope)
            val deleted = sdkdb.eventDAO()?.deleteSyncedEvents(ids)
            mrh.onResponseReceived(type, deleted, 0L, "", null, null)
        }
    }*/

    fun deleteEvents(context: Context, ids: Array<Int?>?, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("EventDBRepoKT.DeleteEvents", "OneFlow reached at delete method")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            val deleted = sdkdb.eventDAO()?.deleteSyncedEvents(ids)
            mrh.onResponseReceived(type, deleted, 0L, "", null, null)
        }
    }


    /*fun fetchEvents(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("EventDBRepoKT.fetchEvents", "OneFlow reached at fetchEvents method")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context,scope)
            OFHelper.v("EventDBRepoKT", "OneFlow fetching events from db ")
            val recordEvents = sdkdb.eventDAO()?.getAllUnsyncedEvents()
            OFHelper.v("EventDBRepoKT", "OneFlow fetching events from db ["+recordEvents?.size+"]")
            mrh.onResponseReceived(type, recordEvents, 0L, "", null, null)
        }


    }*/
    fun fetchEvents(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("EventDBRepoKT.fetchEvents", "OneFlow reached at fetchEvents method")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            OFHelper.v("EventDBRepoKT", "OneFlow fetching events from db ")
            val recordEvents = sdkdb.eventDAO().getAllUnsyncedEvents()
            OFHelper.v("EventDBRepoKT", "OneFlow fetching events from db ["+recordEvents?.size+"]")
            mrh.onResponseReceived(type, recordEvents, 0L, "", null, null)
        }


    }


    /*fun fetchEventsBeforeSurvey(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("EventDBRepoKT.fetchEventsBeforeSurvey", "OneFlow reached at fetchEventsBeforeSurvey method")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val beforeSurveyEvent = sdkdb.eventDAO()?.getEventBeforeSurvey3Sec(Calendar.getInstance().timeInMillis - 3000) // -3000 added for before 3sec logic
            mrh.onResponseReceived(type, beforeSurveyEvent, 0L, "", null, null)
        }

    }*/
    fun fetchEventsBeforeSurvey(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("EventDBRepoKT.fetchEventsBeforeSurvey", "OneFlow reached at fetchEventsBeforeSurvey method")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            val beforeSurveyEvent = sdkdb.eventDAO()?.getEventBeforeSurvey3Sec(Calendar.getInstance().timeInMillis - 3000) // -3000 added for before 3sec logic
            mrh.onResponseReceived(type, beforeSurveyEvent, 0L, "", null, null)
        }

    }


}