package com.oneflow.tryskysdk.utils;

import android.os.AsyncTask;
import com.oneflow.tryskysdk.model.CommanResponse;

public class BGRunner extends AsyncTask<String,Integer, CommanResponse<T>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected CommanResponse<T> doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(CommanResponse<T> tCommanResponse) {
        super.onPostExecute(tCommanResponse);

    }
}
