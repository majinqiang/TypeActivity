package com.laoma.typeactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by majinqiang on 3/24/2017.
 */

public abstract class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onInitView(savedInstanceState);
    }

    protected abstract void onInitView(Bundle state);
}
