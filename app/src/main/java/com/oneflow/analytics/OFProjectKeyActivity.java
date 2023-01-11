package com.oneflow.analytics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.oneflow.analytics.customwidgets.OFCustomEditText;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.utils.OFHelper;

public class OFProjectKeyActivity extends AppCompatActivity {

    OFCustomEditText projectKey;
    OFCustomTextView registerProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_key);
        registerProject = (OFCustomTextView) findViewById(R.id.register_project);
        registerProject.setOnClickListener(listener);
        //String projectKy = "oneflow_prod_lVyNe+aHpwN3sC9MCjgvakChZopOXRNcQHsiRf+D9uMMrFhSawcw+3kIGWZSrqG+r2coTBkYSFPlKkgu3bISPA=="; //rohan's project
        String projectKy = "oneflow_sandbox_oV+xY+hArzT2i4lMP69YZnRBLK1a/qmYW16MboVc208IVjiNKPfHRIylm0rVFgEubtaRuhKMTdlTt5TEuP+8Pw=="; //AmitRepeatTest
        //String projectKy = "oneflow_sandbox_jYPADflsoaQX2PxbP62NUrqOq+KJOXiwbvT8cGvwgRRU7Y9S37/ZUMvYIAXJg4Y4SVK6kaNCqqLdbcsxqpIobg=="; //16Nov
        //String projectKy = "oneflow_sandbox_rO2j1pVVsisXXMNdhLIBeDOk+wrWzLmPztsYoMTOtb+OYs6qND6EsVENHv3IeNjS/Y9pP6rq6s3BzVknNJZMKA=="; //DuplicateEventName
        //String projectKy = "oneflow_sandbox_NSR9CSPdNygqtxXVU39VBKCozzqk3whBUEQ5/PwGh0cXDGp59m3oD99fkvUeHJQZ7u9P8z2pmjqLk4iR25jSSg=="; //flow1
        //String projectKy = "oneflow_prod_YMslXVT1uFOldcBl5kuupFSuLY1yaWkg1lC9lnsZ9jYDvB1KQdRyp4w34VOvMZwlUZ5efuXUWAV5JEizYPzfwA=="; //AmitTestingProject
        //String projectKy = "oneflow_sandbox_hPz4Tfti7FgaKJ+yTdDGgf+OTNdW2czSmdAFMJL40tGbCqDWfswx+2Zy47zGdcax6zwdQRaYJugbfKglb2SLFA=="; //FakeProject
        //String projectKy = "oneflow_prod_uzID3coYT8QigCkryZaRLrEMyhLJw+pICsgcfd5ZKKjoZB7XyOODueR0ZMbquVPQukvS25Ebh4cjm82Knsrw+Q=="; //Global Throttling Project
        //String projectKy = "oneflow_prod_6rAmoIlFw2c8/rMAPoD2TLCfWTM2Zc6aHyQKRfDofwNrgPdJOUX2HSEOxDMUf+8VJwLWnKIaARfj/i3Ktk1EHA=="; //Thao project
        projectKey = (OFCustomEditText) findViewById(R.id.project_key);
        projectKey.setText(projectKy);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (OFHelper.validateString(projectKey.getText().toString()).equalsIgnoreCase("NA")) {
                OFHelper.makeText(OFProjectKeyActivity.this, "Please enter project key", 1);
                projectKey.requestFocus();
            } else {
                startActivity(new Intent(OFProjectKeyActivity.this, OFFirstActivity.class).putExtra("pk", projectKey.getText().toString()));
            }
        }
    };

}