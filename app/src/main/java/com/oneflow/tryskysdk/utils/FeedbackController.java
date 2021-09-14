package com.oneflow.tryskysdk.utils;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.oneflow.tryskysdk.model.adduser.AddUserRequest;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.model.createsession.CreateSessionRequest;
import com.oneflow.tryskysdk.repositories.AddUserRepo;
import com.oneflow.tryskysdk.repositories.CreateSession;
import com.oneflow.tryskysdk.repositories.CurrentLocation;
import com.oneflow.tryskysdk.repositories.Survey;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.sdkdb.SDKDB;

public class FeedbackController extends AppCompatActivity{

   static SDKDB sdkdb;


    public static void configure(){

    }
    public static void registerUser(Context context, AddUserRequest aur){
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        AddUserRepo.addUser(aur,context);
    }
    public static void createSession(Context context,CreateSessionRequest csr){
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        CreateSession.createSession(csr,context);
    }
    public static void getSurvey(Context context){
        Survey.getSurvey(context);
    }
    public static void getLocation(Context context){
        CurrentLocation.getCurrentLocation(context);
    }


}
