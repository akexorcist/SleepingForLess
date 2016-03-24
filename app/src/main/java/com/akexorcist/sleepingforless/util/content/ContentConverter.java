package com.akexorcist.sleepingforless.util.content;

import android.os.AsyncTask;
import android.util.Log;

import com.akexorcist.sleepingforless.bus.BusProvider;
import com.akexorcist.sleepingforless.view.post.model.BasePost;

import java.util.List;

/**
 * Created by Akexorcist on 3/24/2016 AD.
 */
public class ContentConverter {
    private static ContentConverter contentConverter;

    public static ContentConverter getInstance() {
        if (contentConverter == null) {
            contentConverter = new ContentConverter();
        }
        return contentConverter;
    }

    public void convert(String text) {
        new ConverterTask().execute(text);
    }

    private class ConverterTask extends AsyncTask<String, Void, List<BasePost>> {

        @Override
        protected List<BasePost> doInBackground(String... params) {
            return ContentUtility.getInstance().convertPost(params[0]);
        }

        @Override
        protected void onPostExecute(List<BasePost> basePostList) {
            super.onPostExecute(basePostList);
            BusProvider.getInstance().post(new ContentResult(basePostList));
        }
    }


}
