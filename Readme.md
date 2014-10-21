##Credit : Lindelwe Michael Ncube (micnubinub : https://github.com/Micnubinub/)

##Credit : Robin Chutaux (traex : https://github.com/traex)

#How to use:

##Material Switch:

###In XML:
```xml
<com.micnubinub.materiallibrary.MaterialSwitch
    android:id="@+id/material_switch"
    android:layout_width="fill_parent"
    android:layout_height="60dp"
    app:text="Material Switch" />
```
###in Java:
```java
MaterialSwitch materialSwitch = (MaterialSwitch) findViewById(R.id.material_switch);
materialSwitch.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
    @Override
    public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
        //Do your stuff here
    }
});
```

##Material RadioGroup:

###in XML:
```xml
<com.micnubinub.materiallibrary.MaterialRadioGroup
    android:id="@+id/material_radio_group"
    android:layout_width="fill_parent"
    android:layout_height="wrap_content">

    <com.micnubinub.materiallibrary.MaterialRadioButton
        android:layout_width="fill_parent"
        android:layout_height="55dp"
        app:text="Radio Button1" />

    <com.micnubinub.materiallibrary.MaterialRadioButton
        android:layout_width="fill_parent"
        android:layout_height="55dp"
        app:text="Radio Button2" />

    <com.micnubinub.materiallibrary.MaterialRadioButton
        android:layout_width="fill_parent"
        android:layout_height="55dp"
        app:text="Radio Button2" />

</com.micnubinub.materiallibrary.MaterialRadioGroup>
```

###in Java:
```java
MaterialRadioGroup materialRadioGroup = (MaterialRadioGroup) findViewById(R.id.material_radio_group);
materialRadioGroup.setOnSelectionChanged(new MaterialRadioGroup.OnSelectionChangedListener() {
    @Override
    public void onSelectionChanged(MaterialRadioButton radioButton, int selectedChild) {
        //Do your stuff here
    }
});
```
