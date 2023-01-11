package com.oneflow.analytics.repositories

import android.content.Context
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse
import com.oneflow.analytics.model.survey.OFSurveyUserInputKT
import com.oneflow.analytics.sdkdb.OFSDKKOTDB
import com.oneflow.analytics.utils.OFConstants.ApiHitType
import com.oneflow.analytics.utils.OFHelper
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OFLogUserDBRepoKT {

    fun insertUserInputs(context: Context, sui: OFSurveyUserInputKT, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("LogDBRepo.insertUserInputs", "OneFlow reached at insertUserInput method [" + sui.user_id + "]")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            sdkdb.logDAOKT()?.insertUserInput(sui)
            mrh.onResponseReceived(type, sui, 0L, "", null, null)
        }


      /*  object : AsyncTask<String?, Int?, Int?>() {
            protected fun doInBackground(vararg strings: String): Int? {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                sdkdb.logDAO().insertUserInput(sui)
                return null
            }

            override fun onPostExecute(integer: Int?) {
                super.onPostExecute(integer)
                mrh.onResponseReceived(type, sui, 0L, "")
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }

    fun deleteSentSurveyFromDB(context: Context, ids: Array<Int?>?, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("LogDBRepo.DeleteUserInput", "OneFlow reached at delete method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val deleteCount = sdkdb.logDAOKT()?.deleteSurvey(ids)
            mrh.onResponseReceived(type, deleteCount, 0L, "", null, null)
        }

       /* object : AsyncTask<String?, Int?, Int?>() {
            protected fun doInBackground(vararg strings: String): Int? {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                return sdkdb.logDAO().deleteSurvey(ids)
            }

            override fun onPostExecute(integer: Int?) {
                super.onPostExecute(integer)
                mrh.onResponseReceived(type, integer, 0L, "")
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }

    /*public static void fetchSurveyInputList(Context context, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("LogDBRepo.fetchSurveyInputList","OneFlow reached at fetchSurveyEvents method");
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                SDKDB sdkdb = SDKDB.getInstance(context);
                Helper.v("fetchSurveyInputList","OneFlow fetching events list from db 0");
                ArrayList<SurveyUserInput> sui = sdkdb.logDAO().getOfflineUserInputList();
                Helper.v("fetchSurveyInputList","OneFlow fetching events list from db 1");
                mrh.onResponseReceived(type,sui,0l);
            }
        };
        thread.start();

    }*/
    fun fetchSurveyInput(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("LogDBRepo.fetchSurveyInputList", "OneFlow reached at fetchSurveyEvents method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val sui = sdkdb.logDAOKT()?.getOfflineUserInput()
            mrh.onResponseReceived(type, sui, 0L, "", null, null)
        }



       /* object : AsyncTask<String?, Int?, OFSurveyUserInput?>() {
            protected fun doInBackground(vararg strings: String): OFSurveyUserInput? {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                OFHelper.v("fetchSurveyInputList", "OneFlow fetching events list from db 0")
                return sdkdb.logDAO().getOfflineUserInput()
            }

            override fun onPostExecute(sui: OFSurveyUserInput?) {
                super.onPostExecute(sui)
                mrh.onResponseReceived(type, sui, 0L, "")
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }

    // update record once data is sent to server
    fun updateSurveyInput(context: Context, mrh: OFMyResponseHandlerOneFlow?, type: ApiHitType?, syncNew: Boolean?, id: Int?) {
        OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow reached at updateSurveyInput method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            sdkdb.logDAOKT()?.updateUserInput(syncNew, id)
        }

       /* object : AsyncTask<String?, Int?, Int?>() {
            protected fun doInBackground(vararg strings: String): Int? {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow fetching events list from db 0")
                return sdkdb.logDAO().updateUserInput(syncNew, id)
            }

            override fun onPostExecute(sui: Int?) {
                super.onPostExecute(sui)
                //mrh.onResponseReceived(type, sui, 0l);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }

    // update user id for surveys before log
    fun updateSurveyUserId(context: Context, mrh: OFMyResponseHandlerOneFlow, userId: String?, hitType: ApiHitType?) {
        OFHelper.v("LogDBRepo.updateSurveyUserId", "OneFlow updating empty user id")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val inserted = sdkdb.logDAOKT()?.updateUserID(userId)
            mrh.onResponseReceived(hitType, inserted, 0L, "",null,null)
        }


        /*object : AsyncTask<String?, Int?, Int?>() {
            protected fun doInBackground(vararg strings: String): Int? {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                val inserted: Int = sdkdb.logDAO().updateUserID(userId)
                OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow updating empty user id 0[$inserted]")
                return inserted
            }

            override fun onPostExecute(sui: Int?) {
                super.onPostExecute(sui)
                OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow user Id updated")
                //passing because need to call survey
                mrh.onResponseReceived(hitType, sui, 0L, "")
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }

    fun findSurveyForID(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?, gslr: OFGetSurveyListResponse?, id: String, userId: String, eventName: String?) {
        OFHelper.v("LogDBRepo.findSurveyForID", "OneFlow reached at updateSurveyInput method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val surveyUserInput = sdkdb.logDAOKT()?.getSurveyForID(id, userId)
            var ret:Long? = 0L
            ret = if (surveyUserInput != null) {
                surveyUserInput.createdOn
            } else {
                0L
            }
            OFHelper.e("LogDBRepo.updateSurveyUserId","OneFlow find Survey for id["+ret+"]eventName["+eventName+"]")
            mrh.onResponseReceived(type, gslr, 0L, "",ret,eventName)
        }

        /*object : AsyncTask<String?, Int?, Long>() {
            protected fun doInBackground(vararg strings: String): Long {
                val sdkdb: OFSDKDB = OFSDKDB.getInstance(context)
                OFHelper.v("LogDBRepo.findSurveyForID", "OneFlow finding survey for [$id] userid[$userId]")
                val surveyUserInput: OFSurveyUserInput = sdkdb.logDAO().getSurveyForID(id, userId)
                *//* OFHelper.v("LogDBRepo.findSurveyForID loop","OneFlow list size["+surveyUserInput.size()+"]");
                for(OFSurveyUserInput sui:surveyUserInput){
                    OFHelper.v("LogDBRepo.findSurveyForID loop", "OneFlow finding survey for ["+sui.getCreatedOn()+"] date["+OFHelper.formatedDate(sui.getCreatedOn(), "dd-MM-yy hh:mm:ss")+"]");
                }*//*return if (surveyUserInput != null) {
                    surveyUserInput.createdOn
                } else {
                    0L
                }
            }

            override fun onPostExecute(sui: Long) {
                super.onPostExecute(sui)
                OFHelper.v("LogDBRepo.findSurveyForID", "OneFlow returning created_On[$sui]")
                if (sui > 0) {
                    OFHelper.v("LogDBRepo.findSurveyForID", "OneFlow returning created_On readable[" + OFHelper.formatedDate(sui, "dd-MM-yy hh:mm:ss") + "]")
                }
                mrh.onResponseReceived(type, gslr, sui, eventName)
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
    }

    fun findLastSubmittedSurveyID(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?, eventName: String?){
        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val surveyUserInputKT = sdkdb.logDAOKT()?.getLastSyncedSurveyId()

            mrh.onResponseReceived(type, surveyUserInputKT, 0L, eventName,null,null)
        }
    }

}