package com.oneflow.analytics.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.ProgressBar;

import com.oneflow.analytics.BuildConfig;
import com.oneflow.analytics.R;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.model.survey.OFSurveyUserInput;
import com.oneflow.analytics.sdkdb.OFSDKDB;

import java.util.Calendar;


public class OFMyDBAsyncTask extends android.os.AsyncTask<Object, Integer, Object[]> {
    String url;
    //private SpotsDialog dialog;
    private Dialog dialog;
    boolean isBackground = true;
    OFMyResponseHandlerOneFlow mrh;

    Context context;

    boolean showLoader = true;
    String method;
    private static final String TAG = "HRMSAsyncTask";


    public boolean threadCanceled = false;
    ProgressBar pBar;

    public OFMyDBAsyncTask(Context context, OFMyResponseHandlerOneFlow mrh, OFConstants.ApiHitType action, boolean showLoader) {
        this.context = context;
        this.mrh = mrh;
        db = OFSDKDB.getInstance(context);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_bar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));//
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.v(tag, "Aastha cancel called");
                OFMyDBAsyncTask.this.cancel(true);
                threadCanceled = true;
            }
        });
        this.action = action;

        this.showLoader = showLoader;

    }

    public OFMyDBAsyncTask(Context context, OFMyResponseHandlerOneFlow mrh, OFConstants.ApiHitType action) {
        this.context = context;
        this.mrh = mrh;
        db = OFSDKDB.getInstance(context);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_bar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));//
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.v(tag, "OneFlow cancel called");
                OFMyDBAsyncTask.this.cancel(true);
                threadCanceled = true;
            }
        });
        this.action = action;


    }


    public OFMyDBAsyncTask(Context context) {
        this.context = context;
//        dialog = new SpotsDialog(context);
    }


    OFSDKDB db;
    OFConstants.ApiHitType action;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (dialog != null) {
                if (showLoader) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            OFHelper.e(tag, e.getMessage());
        }

    }

    boolean setHeader = false;

    @Override
    protected Object[] doInBackground(Object... requestJSON) {

        OFHelper.v(tag, "OneFlow reached at do in background[" + action + "]");
        Object[] object = new Object[3];
        try {

            if (action == OFConstants.ApiHitType.lastSubmittedSurvey) {
                object[0] = db.logDAO().getLastSyncedSurveyId();
            } else if (action == OFConstants.ApiHitType.fetchSurveysFromDB) {
                object[0] = db.logDAO().getOfflineUserInput();
            }else if (action == OFConstants.ApiHitType.fetchEventsBeforSurveyFetched) {
                String[] beforeSurveyEvent = db.eventDAO().getEventBeforeSurvey3Sec(Calendar.getInstance().getTimeInMillis() - 3000);
                object[0] = beforeSurveyEvent;
            }else if (action == OFConstants.ApiHitType.deleteEventsFromDB) {

                Integer[] ids = (Integer[])requestJSON[0];
                object[0] = db.logDAO().deleteSurvey(ids);

            }else if (action == OFConstants.ApiHitType.updateSubmittedSurveyLocally) {
                // update record once data is sent to server
                Boolean syncNew = (Boolean)requestJSON[0] ;
                Integer id = (Integer) requestJSON[1];
                object[0] = db.logDAO().updateUserInput(syncNew,id);
            }else if (action == OFConstants.ApiHitType.updateSurveyIds) {
                //update user id for surveys before log
                String userId = (String)requestJSON[0];
                object[0] = db.logDAO().updateUserID(userId);
                OFHelper.v(tag,"OneFlow inserted ["+object[0]+"]");
            } else if (action == OFConstants.ApiHitType.insertSurveyInDB) {

                OFSurveyUserInput sur = (OFSurveyUserInput) requestJSON[0];
                db.logDAO().insertUserInput(sur);
                object[0] = sur;
                OFHelper.v(tag,"OneFlow Response handler["+mrh+"]");
            } else if (action == OFConstants.ApiHitType.fetchSubmittedSurvey) {

                OFGetSurveyListResponse gslr = (OFGetSurveyListResponse) requestJSON[0];
                String userId = (String) requestJSON[1];
                object[0] = gslr;

                OFSurveyUserInput surveyUserInput = db.logDAO().getSurveyForID(gslr.get_id(), userId);

                if (surveyUserInput != null) {
                    object[1] = surveyUserInput.getCreatedOn();
                } else {
                    object[1] = 0l;
                }
                object[2] = requestJSON[2]; //fired event name
            }

        } catch (Exception ex) {
            OFHelper.e(TAG, ex.getMessage());
            OFHelper.e(this.getClass().getName(), "OneFlow exception [" + ex.getMessage() + "]");
            if (BuildConfig.DEBUG) {
                ex.printStackTrace();
            }
        }
        return object;
    }


    /*public ArrayList<ServiceData> getAllAvailableServices() {
        ArrayList<ServiceData> sdList = new ArrayList<>();
        ArrayList<AasthaCategory> catList = new ArrayList<AasthaCategory>(db.getAasthaCategoryDAO().getAllAvailableCategory());
        ServiceData sd = null;
        for (AasthaCategory ac : catList) {
            sd = new ServiceData();
            sd.setId(ac.getId());
            sd.setName(ac.getName());

            ArrayList<SubCategory> subCategoryArrayList = new ArrayList<SubCategory>(db.getAasthaSubCategoryDAO().getAllSubCategoryFromParentId(ac.getId()));
            sd.setSubcat(subCategoryArrayList);
            sdList.add(sd);

        }
        return sdList;
    }*/


    String tag = this.getClass().getName();


    @Override
    protected void onPostExecute(Object[] s) {
        super.onPostExecute(s);
        OFHelper.v(tag, "OneFlow from class s[" + s + "]");
        //db.close();
        if (showLoader) {
            try {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
            } catch (Exception ex) {
                OFHelper.e(tag, "OneFlow he he he");
            }
        }
        try {
            // if (s != null) {
            mrh.onResponseReceived(action, s[0], 0l, "", s[1], s[2]);
            /*} else {
               // mrh.onErrorReceived("Something went wrong at db", action);
            }*/
        } catch (Exception e) {
            OFHelper.e(TAG, "OneFlow Async onPost Error " + e.getMessage());
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
