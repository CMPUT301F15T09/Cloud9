package com.example.yunita.tradiogc;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * This class sets up the accuracy of the search list view
 * while doing a partial search.
 */
// taken from http://stackoverflow.com/questions/5730609/is-it-possible-to-slowdown-reaction-of-edittext-listener
// (C) 2015 user1338795
public abstract class DelayedTextWatcher implements TextWatcher {

    private long delayTime;
    private WaitTask lastWaitTask;

    public DelayedTextWatcher(long delayTime) {
        super();
        this.delayTime = delayTime;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (lastWaitTask != null) {
            lastWaitTask.cancel(true);
        }
        lastWaitTask = new WaitTask();
        lastWaitTask.execute(s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public abstract void afterTextChangedDelayed(Editable s);

    private class WaitTask extends AsyncTask<Editable, Void, Editable> {

        @Override
        protected Editable doInBackground(Editable... params) {
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(Editable result) {
            super.onPostExecute(result);
            afterTextChangedDelayed(result);
        }
    }
}