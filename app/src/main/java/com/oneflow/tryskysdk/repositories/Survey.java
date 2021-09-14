package com.oneflow.tryskysdk.repositories;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.oneflow.tryskysdk.SurveyActivity;
import com.oneflow.tryskysdk.model.ApiInterface;
import com.oneflow.tryskysdk.model.GenericResponse;
import com.oneflow.tryskysdk.model.RetroBaseService;
import com.oneflow.tryskysdk.model.createsession.CreateSessionResponse;
import com.oneflow.tryskysdk.model.location.LocationResponse;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Survey {

    static String tag = "Survey";

    public static void getSurvey(Context context){
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<ArrayList<GetSurveyListResponse>>> responseCall = null;

            responseCall = connectAPI.getSurvey();

            responseCall.enqueue(new Callback<GenericResponse<ArrayList<GetSurveyListResponse>>>() {
                @Override
                public void onResponse(Call<GenericResponse<ArrayList<GetSurveyListResponse>>> call, Response<GenericResponse<ArrayList<GetSurveyListResponse>>> response) {


                    Helper.v(tag, "OneFlow reached success["+response.isSuccessful()+"]");
                    Helper.v(tag, "OneFlow reached success raw["+response.raw()+"]");
                    Helper.v(tag, "OneFlow reached success errorBody["+response.errorBody()+"]");
                    Helper.v(tag, "OneFlow reached success message["+response.message()+"]");


                    if (response.isSuccessful()) {
                        Helper.v(tag,"OneFlow response["+response.body().toString()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");
                        Helper.v(tag,"OneFlow response message["+response.body().getMessage()+"]");
                        Helper.v(tag,"OneFlow response size["+response.body().getResult().size()+"]");
                        Helper.v(tag,"OneFlow response trigger name["+response.body().getResult().get(0).getTrigger_event_name()+"]");
                        Helper.v(tag,"OneFlow response event name["+response.body().getResult().get(0).getName()+"]");

                        new OneFlowSHP(context).setSurveyList(response.body().getResult(),Constants.SURVEYLISTSHP);
                        Intent intent = new Intent(context, SurveyActivity.class);
                        intent.putExtra("SurveyType","show_nps_prompt");
                        context.startActivity(intent);

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        Helper.v(tag,"OneFlow response 0["+response.body()+"]");
                        Helper.v(tag,"OneFlow response 1["+response.body().getMessage()+"]");
                        Helper.v(tag,"OneFlow response 2["+response.body().getSuccess()+"]");
                    }

                    String tempResposne = "[{\"name\": \"Testing Survey\",\"description\": \"Creating Testing Survey\",\"num_responses\": null,\"end_date\": null,\"live\": false,\"platforms\": [\"60dc7e1003c39e852866c865\"],\"deleted\": false,\"deleted_on\": null,\"schema_version\": 1,\"_id\": \"60eecba240d15574d4880d1c\",\"project_id\": \"60dc7c724ac73426ec521b3f\",\"style\": {\"display_mode\": \"dark\",\"font\": \"arial\",\"_id\": \"60eecba240d15574d4880d1d\",\"primary_color\": \"primary\",\"corner_radius\": 100},\"screens\": [{\"title\": \"Who is the creator of php\",\"message\": \"Please check one \",\"_id\": \"60eecba240d15574d4880d1e\",\"input\": {\"min_val\": null,\"max_val\": null,\"emoji\": null,\"stars\": null,\"emojis\": [],\"star_fill_color\": null,\"min_chars\": 10,\"max_chars\": 100,\"_id\": \"60eecba240d15574d4880d1f\",\"input_type\": \"text\",\"choices\": []},\"buttons\": [{\"button_type\": \"primary\",\"_id\": \"60eecba240d15574d4880d20\",\"action\": \"button_action_submit\",\"title\": \"Submit\"}]},{\"title\": \"Who is the creator of php\",\"message\": \"Please check one \",\"_id\": \"60eecba240d15574d4880d21\",\"input\": {\"min_val\": 10,\"max_val\": \"100\",\"emoji\": false,\"stars\": true,\"emojis\": [],\"star_fill_color\": \"#fff\",\"min_chars\": null,\"max_chars\": null,\"_id\": \"60eecba240d15574d4880d22\",\"input_type\": \"rating\",\"choices\": []},\"buttons\": [{\"button_type\": \"primary\",\"_id\": \"60eecba240d15574d4880d23\",\"action\": \"button_action_submit\",\"title\": \"Submit\"}]},{\"title\": \"Who is the creator of php\",\"message\": \"Please check one \",\"_id\": \"60eecba240d15574d4880d24\",\"input\": {\"min_val\": null,\"max_val\": null,\"emoji\": null,\"stars\": null,\"emojis\": [],\"star_fill_color\": null,\"min_chars\": null,\"max_chars\": null,\"_id\": \"60eecba240d15574d4880d25\",\"input_type\": \"mcq\",\"choices\": [{\"_id\": \"60eecba240d15574d4880d26\",\"title\": \"Who is the creator of php\"},{\"_id\": \"60eecba240d15574d4880d27\",\"title\": \"Who is the creator of c\"},{\"_id\": \"60eecba240d15574d4880d28\",\"title\": \"Who is the creator of java\"},{\"_id\": \"60eecba240d15574d4880d29\",\"title\": \"Who is the creator of node\"},{\"_id\": \"60eecba240d15574d4880d2a\",\"title\": \"Who is the creator of python\"}]},\"buttons\": [{\"button_type\": \"primary\",\"_id\": \"60eecba240d15574d4880d2b\",\"action\": \"button_action_submit\",\"title\": \"Submit\"}]}],\"trigger_event_name\": \"testing_event\",\"start_date\": 1626262434264,\"created_on\": 1626262434267,\"updated_on\": 1626262434284,\"__v\": 0}]";

                    ArrayList<GetSurveyListResponse> respObject = Helper.fromJsonToArrayList(tempResposne,GetSurveyListResponse.class) ;//gson.fromJson(tempResposne,GetSurveyListResponse.class);

                    Helper.v(tag,"OneFlow response 4["+respObject.size()+"]");


                }

                @Override
                public void onFailure(Call<GenericResponse<ArrayList<GetSurveyListResponse>>> call, Throwable t) {

                    Helper.e(tag,"OneFlow error["+t.toString()+"]");
                    Helper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }
    }
}
