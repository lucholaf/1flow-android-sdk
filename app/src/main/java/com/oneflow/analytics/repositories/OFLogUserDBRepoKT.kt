package com.oneflow.analytics.repositories

import android.content.Context
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse
import com.oneflow.analytics.model.survey.OFSurveyUserInput
import com.oneflow.analytics.model.survey.OFSurveyUserInputKT
import com.oneflow.analytics.sdkdb.OFSDKDB
import com.oneflow.analytics.sdkdb.OFSDKKOTDB
import com.oneflow.analytics.utils.OFConstants.ApiHitType
import com.oneflow.analytics.utils.OFHelper
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OFLogUserDBRepoKT {

    /*fun insertUserInputs(context: Context, sui: OFSurveyUserInputKT, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("OFLogUserDBRepoKT.insertUserInputs", "OneFlow SurveyInput reached at insertUserInput method [" + sui.user_id + "]")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            var inserted = sdkdb.logDAOKT()?.insertUserInput(sui)
            OFHelper.v("OFLogUserDBRepoKT", "OneFlow SurveyInput inserted["+inserted+"]")
            mrh.onResponseReceived(type, sui, inserted, "", null, null)
        }


    }*/
    fun insertUserInputs(context: Context, sui: OFSurveyUserInput, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("OFLogUserDBRepoKT.insertUserInputs", "OneFlow SurveyInput reached at insertUserInput method [" + sui.user_id + "]")

        val job = Job()

        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            var inserted = sdkdb.logDAO().insertUserInput(sui)
            OFHelper.v("OFLogUserDBRepoKT", "OneFlow SurveyInput inserted["+inserted+"]")
            mrh.onResponseReceived(type, sui, inserted, "", null, null)
        }


    }

    /*fun deleteSentSurveyFromDB(context: Context, ids: Array<Int?>?, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("LogDBRepo.DeleteUserInput", "OneFlow reached at delete method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val deleteCount = sdkdb.logDAOKT()?.deleteSurvey(ids)
            OFHelper.v("LogDBRepo.DeleteUserInput", "OneFlow reached at delete method count["+deleteCount+"]")
            mrh.onResponseReceived(type, deleteCount, 0L, "", null, null)
        }

    }*/
    fun deleteSentSurveyFromDB(context: Context, ids: Array<Int?>?, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("LogDBRepo.DeleteUserInput", "OneFlow reached at delete method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            val deleteCount = sdkdb.logDAO().deleteSurvey(ids)
            OFHelper.v("LogDBRepo.DeleteUserInput", "OneFlow reached at delete method count["+deleteCount+"]")
            mrh.onResponseReceived(type, deleteCount, 0L, "", null, null)
        }

    }


    /*fun fetchSurveyInput(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("LogDBRepo.fetchSurveyInputList", "OneFlow reached at fetchSurveyEvents method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val sui = sdkdb.logDAOKT()?.getOfflineUserInput()
            mrh.onResponseReceived(type, sui, 0L, "", null, null)
        }

    }*/
    fun fetchSurveyInput(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?) {
        OFHelper.v("LogDBRepo.fetchSurveyInputList", "OneFlow reached at fetchSurveyEvents method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            val sui = sdkdb.logDAO().getOfflineUserInput()
            mrh.onResponseReceived(type, sui, 0L, "", null, null)
        }

    }

    // update record once data is sent to server
    /*fun updateSurveyInput(context: Context, mrh: OFMyResponseHandlerOneFlow?, type: ApiHitType?, syncNew: Boolean?, id: Int?) {
        OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow reached at updateSurveyInput method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            var updated = sdkdb.logDAOKT()?.updateUserInput(syncNew, id)

            OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow update called["+updated+"]id["+id+"]synced["+syncNew+"]")

        }

    }*/
    fun updateSurveyInput(context: Context, mrh: OFMyResponseHandlerOneFlow?, type: ApiHitType?, syncNew: Boolean?, id: String?) {
        OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow reached at updateSurveyInput method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            var updated = sdkdb.logDAO()?.updateUserInput(syncNew, id)

            OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow update called["+updated+"]id["+id+"]synced["+syncNew+"]")

            if(mrh!=null){
                mrh.onResponseReceived(type,updated,0L,"","","")
            }

        }

    }

    // update user id for surveys before log
   /* fun updateSurveyUserId(context: Context, mrh: OFMyResponseHandlerOneFlow, userId: String?, hitType: ApiHitType?) {
        OFHelper.v("LogDBRepo.updateSurveyUserId", "OneFlow updating empty user id")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKKOTDB.getDatabase(context, scope)
            val inserted = sdkdb.logDAOKT()?.updateUserID(userId)
            mrh.onResponseReceived(hitType, inserted, 0L, "", null, null)
        }

    }*/
    fun updateSurveyUserId(context: Context, mrh: OFMyResponseHandlerOneFlow, userId: String?, hitType: ApiHitType?) {
        OFHelper.v("LogDBRepo.updateSurveyUserId", "OneFlow updating empty user id")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            val inserted = sdkdb.logDAO()?.updateUserID(userId)
            mrh.onResponseReceived(hitType, inserted, 0L, "", null, null)
        }

    }

    /*fun findSurveyForID(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?, gslr: OFGetSurveyListResponse?, id: String, userId: String, eventName: String?) {
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
            OFHelper.e("LogDBRepo.updateSurveyUserId", "OneFlow find Survey for id[" + ret + "]eventName[" + eventName + "]")
            mrh.onResponseReceived(type, gslr, 0L, "", ret, eventName)
        }


    } */
    fun findSurveyForID(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?, gslr: OFGetSurveyListResponse?, id: String, userId: String, eventName: String?) {
        OFHelper.v("LogDBRepo.findSurveyForID", "OneFlow reached at updateSurveyInput method")

        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            val surveyUserInput = sdkdb.logDAO()?.getSurveyForID(id, userId)
            var ret:Long? = 0L
            ret = if (surveyUserInput != null) {
                surveyUserInput.createdOn
            } else {
                0L
            }
            OFHelper.e("LogDBRepo.updateSurveyUserId", "OneFlow find Survey for id[" + ret + "]eventName[" + eventName + "]")
            mrh.onResponseReceived(type, gslr, 0L, "", ret, eventName)
        }


    }

    /*fun findLastSubmittedSurveyID(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?, eventName: String?){
        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb: OFSDKKOTDB = OFSDKKOTDB.getDatabase(context, scope)
            val surveyUserInputKT = sdkdb.logDAOKT()?.getLastSyncedSurveyId()

            val ofSurveyUserInput = surveyUserInputKT as List<OFSurveyUserInputKT>
            OFHelper.v("OFLogUserDBRepoKT", "OneFlow fetching Last submitted survey[" + ofSurveyUserInput.size +"]")
            OFHelper.v("OFLogUserDBRepoKT", "OneFlow fetching Last submitted survey[" + ofSurveyUserInput.get(0).createdOn +"]")
            OFHelper.v("OFLogUserDBRepoKT", "OneFlow fetching Last submitted survey[" + ofSurveyUserInput.get(0).synced +"]")
            mrh.onResponseReceived(type, surveyUserInputKT.get(0), 0L, eventName, null, null)
        }
    }*/
    fun findLastSubmittedSurveyID(context: Context, mrh: OFMyResponseHandlerOneFlow, type: ApiHitType?, eventName: String?){
        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            val sdkdb = OFSDKDB.getInstance(context)
            val surveyUserInput = sdkdb.logDAO()?.getLastSyncedSurveyId()

            val ofSurveyUserInput = surveyUserInput as OFSurveyUserInput

            OFHelper.v("OFLogUserDBRepoKT", "OneFlow fetching Last submitted survey[" + ofSurveyUserInput.createdOn +"]")
            OFHelper.v("OFLogUserDBRepoKT", "OneFlow fetching Last submitted survey[" + ofSurveyUserInput.synced +"]")
            mrh.onResponseReceived(type, surveyUserInput, 0L, eventName, null, null)
        }
    }

}