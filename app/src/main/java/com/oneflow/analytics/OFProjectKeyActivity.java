package com.oneflow.analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oneflow.analytics.customwidgets.OFCustomEditText;
import com.oneflow.analytics.utils.OFHelper;

public class OFProjectKeyActivity extends AppCompatActivity {

    OFCustomEditText projectKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_key);
        //String projectKy = "oneflow_sandbox_oV+xY+hArzT2i4lMP69YZnRBLK1a/qmYW16MboVc208IVjiNKPfHRIylm0rVFgEubtaRuhKMTdlTt5TEuP+8Pw=="; //AmitRepeatTest
        String projectKy = "oneflow_sandbox_hPz4Tfti7FgaKJ+yTdDGgf+OTNdW2czSmdAFMJL40tGbCqDWfswx+2Zy47zGdcax6zwdQRaYJugbfKglb2SLFA=="; //FakeProject
        //String projectKy = "oneflow_prod_6rAmoIlFw2c8/rMAPoD2TLCfWTM2Zc6aHyQKRfDofwNrgPdJOUX2HSEOxDMUf+8VJwLWnKIaARfj/i3Ktk1EHA=="; //Thao project
        projectKey = (OFCustomEditText)findViewById(R.id.project_key);
        projectKey.setText(projectKy);
    }
    public void registerProject(View v){

        switch(v.getId()){
            case R.id.register_project:
                if(OFHelper.validateString(projectKey.getText().toString()).equalsIgnoreCase("NA")){
                    OFHelper.makeText(OFProjectKeyActivity.this,"Please enter project key",1);
                    projectKey.requestFocus();
                }else{
                    startActivity(new Intent(OFProjectKeyActivity.this,OFFirstActivity.class).putExtra("pk",projectKey.getText().toString()));
                }
                break;
        }

    }
}