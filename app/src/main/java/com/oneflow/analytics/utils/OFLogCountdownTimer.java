package com.oneflow.analytics.utils;

import android.content.Context;
import android.os.CountDownTimer;

import com.oneflow.analytics.model.adduser.OFAddUserResultResponse;
import com.oneflow.analytics.model.loguser.OFLogUserRequest;
import com.oneflow.analytics.model.loguser.OFLogUserResponse;
import com.oneflow.analytics.repositories.OFLogUserDBRepoKT;
import com.oneflow.analytics.repositories.OFLogUserRepo;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;

public class OFLogCountdownTimer extends CountDownTimer implements OFMyResponseHandlerOneFlow{

    Context mContext;
    static OFLogCountdownTimer cdt;
    static int count = 0;
    public static synchronized OFLogCountdownTimer getInstance(Context context, Long duration, Long interval) {
        if (cdt == null) {
            cdt = new OFLogCountdownTimer(context, duration, interval);
        }
        return cdt;
    }
    private OFLogCountdownTimer(Context context, Long duration, Long interval) {
        super(duration, interval);
        this.mContext = context;
       // OFHelper.v("OFLogCountdownTimer", "OneFlow LogApi Constructor called");
    }
    @Override
    public void onTick(long millisUntilFinished) {
       // OFHelper.v("OFLogCountdownTimer", "OneFlow LogApi onTick called ");
        OFLogUserRequest lur = OFOneFlowSHP.getInstance(mContext).getLogUserRequest();
        if (lur != null) {
            OFLogUserRepo.logUser(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), lur, this, OFConstants.ApiHitType.logUser);
        }
    }

    @Override
    public void onFinish() {
       // OFHelper.v("OFLogCountdownTimer", "OneFlow LogApi onFinish called ");
    }


    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved, Object obj2, Object obj3 ) {
        switch (hitType) {
            case logUser:
                if (obj != null) {

                    OFLogUserResponse logUserResponse = (OFLogUserResponse) obj;
                    if (logUserResponse != null) {
                        // replacing current session id and user analytical id
                        OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(mContext);
                        ofs.storeValue(OFConstants.SHP_LOG_USER_KEY, reserved);//ofs.getLogUserRequest().getSystem_id()); // system id stored for sending next app launch
                        ofs.clearLogUserRequest();
                        OFAddUserResultResponse aurr = ofs.getUserDetails();
                        //setting up new user analytical id

                        aurr.setAnalytic_user_id(logUserResponse.getAnalytic_user_id());
                        ofs.setUserDetails(aurr);
                        //ofs.storeValue(OFConstants.SESSIONDETAIL_IDSHP, logUserResponse.getSessionId());

                        //storing this to support multi user survey
                        ofs.storeValue(OFConstants.USERUNIQUEIDSHP, reserved);

                        // mrh.onResponseReceived(hitType,null,0);
                       // OFHelper.v("OneFlow", "OneFlow Log record inserted...");

                        //Updating old submitted surveys with logged user id.
                        new OFLogUserDBRepoKT().updateSurveyUserId(mContext, this, reserved, OFConstants.ApiHitType.updateSurveyIds);
                        //new OFMyDBAsyncTask(mContext,this,OFConstants.ApiHitType.updateSurveyIds,false).execute(reserved);
                        cdt.cancel();
                    } else {
                        if (OFConstants.MODE.equalsIgnoreCase("dev")) {
                            OFHelper.makeText(mContext, reserved, 1);
                        }
                    }
                } else {
                   // OFHelper.e("OneFlow", "OneFlow LogApi failed logUser");
                   // OFLogCountdownTimer.getInstance(mContext, 3000l, 1000l).start();
                }

                break;
            default:

        }
    }
}
