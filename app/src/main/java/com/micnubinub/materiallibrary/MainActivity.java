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
        MaterialRadioGroup materialRadioGroup = (MaterialRadioGroup) findViewById(R.id.material_radio_group);
        materialRadioGroup.setOnSelectionChanged(new MaterialRadioGroup.OnSelectionChangedListener() {
            @Override
            public void onSelectionChanged(MaterialRadioButton radioButton, int selectedChild) {
                //Do your stuff here
            }
        });
    }

}
