package com.micnubinub.materiallibrary;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {
    MaterialRippleView materialRippleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  materialRippleView = (MaterialRippleView) findViewById(R.id.ripple);
    }

}
