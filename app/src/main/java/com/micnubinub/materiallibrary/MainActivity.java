package com.micnubinub.materiallibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;


public class MainActivity extends Activity {
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

    }


}
