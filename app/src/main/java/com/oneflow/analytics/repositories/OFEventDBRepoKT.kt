package com.oneflow.analytics.repositories

import android.content.Context
import com.oneflow.analytics.model.events.OFRecordEventsTabKT
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
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context,scope)
            val ret = OFRecordEventsTabKT()
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


        /*object : AsyncTask<String?, Int?, Int?>() {
            protected fun doInBackground(vararg strings: String): Int? {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                val ret = OFRecordEventsTab()
                ret.eventName = eventName
                ret.dataMap = data
                ret.value = value.toString()
                ret.time = Calendar.getInstance().timeInMillis / 1000
                ret.synced = 0
                ret.createdOn = Calendar.getInstance().timeInMillis
                sdkdb.eventDAO().insertAll(ret)
                return null
            }

            override fun onPostExecute(integer: Int?) {
                super.onPostExecute(integer)
                mrh.onResponseReceived(type, 1, 0L, eventName, null, null)
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }

    fun deleteEvents(context: Context, ids: Array<Int?>?, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("EventDBRepoKT.DeleteEvents", "OneFlow reached at delete method")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKKOTDB.getDatabase(context,scope)
            val deleted = sdkdb.eventDAO()?.deleteSyncedEvents(ids)
            mrh.onResponseReceived(type, deleted, 0L, "", null, null)
        }


        /*bject : AsyncTask<String?, Int?, Int?>() {
            protected fun doInBackground(vararg strings: String): Int? {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                return sdkdb.eventDAO().deleteSyncedEvents(ids)
            }

            override fun onPostExecute(integer: Int?) {
                super.onPostExecute(integer)
                mrh.onResponseReceived(type, integer, 0L, "", null, null)
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }


    fun fetchEvents(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
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


       /* object : AsyncTask<String?, Int?, List<OFRecordEventsTab?>?>() {
            protected fun doInBackground(vararg strings: String): List<OFRecordEventsTab?>? {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                OFHelper.v("EventDBRepo", "OneFlow fetching events from db 0")
                return sdkdb.eventDAO().getAllUnsyncedEvents()
            }

            override fun onPostExecute(OFRecordEventsTabs: List<OFRecordEventsTab?>?) {
                super.onPostExecute(OFRecordEventsTabs)
                mrh.onResponseReceived(type, OFRecordEventsTabs, 0L, "", null, null)
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }


    fun fetchEventsBeforeSurvey(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("EventDBRepoKT.fetchEventsBeforeSurvey", "OneFlow reached at fetchEventsBeforeSurvey method")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val beforeSurveyEvent = sdkdb.eventDAO()?.getEventBeforeSurvey3Sec(Calendar.getInstance().timeInMillis - 3000) // -3000 added for before 3sec logic
            mrh.onResponseReceived(type, beforeSurveyEvent, 0L, "", null, null)
        }

        //String[] beforeSurveyEvent = new String[1];
       /* object : AsyncTask<String?, Int?, Array<String?>?>() {
            protected fun doInBackground(vararg strings: String): Array<String?>? {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                OFHelper.v("EventDBRepo", "OneFlow fetching events from db 0")
                val beforeSurveyEvent: Array<String?> = sdkdb.eventDAO().getEventBeforeSurvey3Sec(Calendar.getInstance().timeInMillis - 3000) // -3000 added for before 3sec logic
                OFHelper.v("EventDBRepo", "OneFlow fetching events from db [" + Arrays.asList(*beforeSurveyEvent) + "]length[" + beforeSurveyEvent.size + "]")
                return beforeSurveyEvent
            }

            override fun onPostExecute(strings: Array<String?>?) {
                super.onPostExecute(strings)

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }


}