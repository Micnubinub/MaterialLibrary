package com.micnubinub.materiallibrary;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        ll.addView(new DeathScreen(this), new ViewGroup.LayoutParams(dpToPixels(250), dpToPixels(250)));
     /* //  ll.addView(materialButton());
       // ll.addView(materialCheckBox());
       // ll.addView(materialSwitch());
      //  ll.addView(materialSeekBar());
       // ll.addView(materialRadioGroup());
        ll.addView(materialSingleLineTextIcon());
        ll.addView(materialSingleLineTextAvatar());
        ll.addView(materialSingleLineTextAvatarWithIcon());
        ll.addView(materialTwoLineText());
        ll.addView(materialTwoLineTextIcon());
        ll.addView(materialTwoLineTextAvatar());
        ll.addView(materialTwoLineTextAvatarWithIcon());
        ll.addView(materialThreeLineText());
        ll.addView(materialThreeLineTextIcon());
        ll.addView(materialThreeLineTextAvatar());
        ll.addView(materialThreeLineTextAvatarWithIcon());
*/
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private final View materialButton() {
        final MaterialButton button = new MaterialButton(this);
        button.setText("Material Button");
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return button;
    }


    private final View materialCheckBox() {
        final MaterialCheckBox materialCheckBox = new MaterialCheckBox(this);
        materialCheckBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(56)));
        materialCheckBox.setText("Material Check Box");
        materialCheckBox.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialCheckBox materialCheckBox, boolean isChecked) {
                Toast.makeText(getApplicationContext(), String.format("Checkbox : %s",
                        isChecked ? "checked" : "not checked"), Toast.LENGTH_SHORT).show();
            }
        });

        return materialCheckBox;
    }

    private final View materialRadioGroup() {
        final MaterialRadioGroup materialRadioGroup = new MaterialRadioGroup(this);
        materialRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(56));

        final MaterialRadioButton materialRadioButton1 = new MaterialRadioButton(this);
        materialRadioButton1.setLayoutParams(params);
        materialRadioButton1.setText("Radio Button 1");

        final MaterialRadioButton materialRadioButton2 = new MaterialRadioButton(this);
        materialRadioButton2.setLayoutParams(params);
        materialRadioButton2.setText("Radio Button 2");

        final MaterialRadioButton materialRadioButton3 = new MaterialRadioButton(this);
        materialRadioButton3.setLayoutParams(params);
        materialRadioButton3.setText("Radio Button 3");

        materialRadioGroup.addView(materialRadioButton1);
        materialRadioGroup.addView(materialRadioButton2);
        materialRadioGroup.addView(materialRadioButton3);

        return materialRadioGroup;
    }

    private final View materialSeekBar() {
        final MaterialSeekBar materialSeekBar = new MaterialSeekBar(this);
        materialSeekBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(25)));
        materialSeekBar.setMax(20);
        materialSeekBar.setOnProgressChangedListener(new MaterialSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int max, int progress) {
                Toast.makeText(getApplicationContext(), String.format("Progress : %d of %d",
                        progress, max), Toast.LENGTH_SHORT).show();
            }
        });

        return materialSeekBar;
    }

    private final View materialSingleLineTextAvatar() {
        final MaterialSingleLineTextAvatar materialSingleLineTextAvatar = new MaterialSingleLineTextAvatar(this);
        materialSingleLineTextAvatar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(56)));
        materialSingleLineTextAvatar.setText("Material Single line text with avatar");
        materialSingleLineTextAvatar.setIcon(getResources().getDrawable(R.drawable.test));

        return materialSingleLineTextAvatar;
    }

    private final View materialSingleLineTextIcon() {
        final MaterialSingleLineTextIcon materialSingleLineTextIcon = new MaterialSingleLineTextIcon(this);
        materialSingleLineTextIcon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(56)));
        materialSingleLineTextIcon.setText("Material Single line text with Icon");
        materialSingleLineTextIcon.setIcon(getResources().getDrawable(R.drawable.test));

        return materialSingleLineTextIcon;
    }

    private final View materialSingleLineTextAvatarWithIcon() {
        final MaterialSingleLineTextAvatarWithIcon materialSingleLineTextAvatarWithIcon = new MaterialSingleLineTextAvatarWithIcon(this);
        materialSingleLineTextAvatarWithIcon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(56)));
        materialSingleLineTextAvatarWithIcon.setText("Material Single line text with avatar");
        materialSingleLineTextAvatarWithIcon.setLeftIcon(getResources().getDrawable(R.drawable.test));
        materialSingleLineTextAvatarWithIcon.setRightIcon(getResources().getDrawable(R.drawable.block));

        return materialSingleLineTextAvatarWithIcon;
    }

    private final View materialTwoLineText() {
        final MaterialTwoLineText materialTwoLineText = new MaterialTwoLineText(this);
        materialTwoLineText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(72)));
        materialTwoLineText.setPrimaryText("Primary Text");
        materialTwoLineText.setSecondaryText("Material Two line text with no icon ");

        return materialTwoLineText;
    }


    private final View materialTwoLineTextIcon() {
        final MaterialTwoLineTextIcon materialTwoLineTextIcon = new MaterialTwoLineTextIcon(this);
        materialTwoLineTextIcon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(72)));
        materialTwoLineTextIcon.setPrimaryText("Primary Text");
        materialTwoLineTextIcon.setSecondaryText("Material Two line text with Icon");
        materialTwoLineTextIcon.setIcon(getResources().getDrawable(R.drawable.test));

        return materialTwoLineTextIcon;
    }

    private final View materialTwoLineTextAvatar() {
        final MaterialTwoLineTextAvatar materialTwoLineTextAvatar = new MaterialTwoLineTextAvatar(this);
        materialTwoLineTextAvatar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(72)));
        materialTwoLineTextAvatar.setPrimaryText("Primary text");
        materialTwoLineTextAvatar.setSecondaryText("Material Two line text with avatar");
        materialTwoLineTextAvatar.setIcon(getResources().getDrawable(R.drawable.test));

        return materialTwoLineTextAvatar;
    }

    private final View materialTwoLineTextAvatarWithIcon() {
        final MaterialTwoLineTextAvatarWithIcon materialTwoLineTextAvatarWithIcon = new MaterialTwoLineTextAvatarWithIcon(this);
        materialTwoLineTextAvatarWithIcon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(72)));
        materialTwoLineTextAvatarWithIcon.setPrimaryText("Primary text");
        materialTwoLineTextAvatarWithIcon.setSecondaryText("Material Two line text with avatar");
        materialTwoLineTextAvatarWithIcon.setLeftIcon(getResources().getDrawable(R.drawable.test));
        materialTwoLineTextAvatarWithIcon.setRightIcon(getResources().getDrawable(R.drawable.block));

        return materialTwoLineTextAvatarWithIcon;
    }

    private final View materialThreeLineTextIcon() {
        final MaterialThreeLineTextIcon materialThreeLineTextIcon = new MaterialThreeLineTextIcon(this);
        materialThreeLineTextIcon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(72)));
        materialThreeLineTextIcon.setPrimaryText("Primary Text");
        materialThreeLineTextIcon.setSecondaryText("Material Three line text with Icon test, making sure it spans over two lines");
        materialThreeLineTextIcon.setIcon(getResources().getDrawable(R.drawable.test));

        return materialThreeLineTextIcon;
    }

    private final View materialThreeLineTextAvatar() {
        final MaterialThreeLineTextAvatar materialThreeLineTextAvatar = new MaterialThreeLineTextAvatar(this);
        materialThreeLineTextAvatar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(72)));
        materialThreeLineTextAvatar.setPrimaryText("Primary text");
        materialThreeLineTextAvatar.setSecondaryText("Material Three line text with Avatar test, making sure it spans over two lines");
        materialThreeLineTextAvatar.setIcon(getResources().getDrawable(R.drawable.test));

        return materialThreeLineTextAvatar;
    }

    private final View materialThreeLineTextAvatarWithIcon() {
        final MaterialThreeLineTextAvatarWithIcon materialThreeLineTextAvatarWithIcon = new MaterialThreeLineTextAvatarWithIcon(this);
        materialThreeLineTextAvatarWithIcon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(72)));
        materialThreeLineTextAvatarWithIcon.setPrimaryText("Primary text");
        materialThreeLineTextAvatarWithIcon.setSecondaryText("Material Three line text with Avatar and Icon test, making sure it spans over two lines");
        materialThreeLineTextAvatarWithIcon.setLeftIcon(getResources().getDrawable(R.drawable.test));
        materialThreeLineTextAvatarWithIcon.setRightIcon(getResources().getDrawable(R.drawable.block));

        return materialThreeLineTextAvatarWithIcon;
    }

    private final View materialThreeLineText() {
        final MaterialThreeLineText materialThreeLineText = new MaterialThreeLineText(this);
        materialThreeLineText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(72)));
        materialThreeLineText.setPrimaryText("Primary Text");
        materialThreeLineText.setSecondaryText("Material Three line text with no icon test, making sure it spans over two lines");
        return materialThreeLineText;
    }

    private final View materialSwitch() {
        final MaterialSwitch materialSwitch = new MaterialSwitch(this);
        materialSwitch.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPixels(55)));
        materialSwitch.setText("Material Check Box");
        materialSwitch.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                Toast.makeText(getApplicationContext(), String.format("Switch : %s",
                        isChecked ? "checked" : "not checked"), Toast.LENGTH_SHORT).show();
            }
        });

        return materialSwitch;
    }


}
