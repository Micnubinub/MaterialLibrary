package com.micnubinub.materiallibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {
    public Context context;
    MaterialRippleView materialRippleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new TmpAdapter());


    }

    private class TmpAdapter extends BaseAdapter {

        public TmpAdapter() {

        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getView(i);
            return view;
        }

        private View getView(int i) {
            View view = null;

            switch (i) {
                case 0:
                    view = new MaterialTwoLineText(context);
                    ((MaterialTwoLineText) view).setPrimaryText(String.format("Primary %d", i));
                    ((MaterialTwoLineText) view).setSecondaryText(String.format("Primary %d", i));
                    break;

                case 1:
                    view = new MaterialTwoLineTextAvatar(context);
                    ((MaterialTwoLineTextAvatar) view).setPrimaryText(String.format("Primary %d", i));
                    ((MaterialTwoLineTextAvatar) view).setSecondaryText(String.format("Primarysfasf as fas fas fas fa sf asf asf asf asfa sfaf asfsaf asf sf %d", i));
                    ((MaterialTwoLineTextAvatar) view).setIcon(getResources().getDrawable(R.drawable.back));
                    break;

                case 2:
                    view = new MaterialTwoLineTextAvatarWithIcon(context);
                    ((MaterialTwoLineTextAvatarWithIcon) view).setPrimaryText(String.format("Primary %d", i));
                    ((MaterialTwoLineTextAvatarWithIcon) view).setSecondaryText(String.format("Primary as asf as fasf asfsafasfasfasfas fasf as fa sf asf asf sa %d", i));
                    ((MaterialTwoLineTextAvatarWithIcon) view).setLeftIcon(getResources().getDrawable(R.drawable.barcode));
                    ((MaterialTwoLineTextAvatarWithIcon) view).setRightIcon(getResources().getDrawable(R.drawable.cloud_download));
                    break;

                case 3:
                    view = new MaterialTwoLineTextIcon(context);
                    ((MaterialTwoLineTextIcon) view).setPrimaryText(String.format("Primary %d", i));
                    ((MaterialTwoLineTextIcon) view).setSecondaryText(String.format("Prim  safasfa sf asfa sfasfas  fasfasfsaf   asf asf asf asf ary %d", i));
                    ((MaterialTwoLineTextIcon) view).setIcon(getResources().getDrawable(R.drawable.block));
                    break;

                case 4:
                    view = new MaterialThreeLineText(context);
                    ((MaterialThreeLineText) view).setPrimaryText(String.format("Primary %d", i));
                    ((MaterialThreeLineText) view).setSecondaryText(String.format("Primary s dfsfmam dm am dmaw m amwdmawm dma wmd amw ma m cms md mwe v %d", i));

                    break;

                case 5:
                    view = new MaterialThreeLineTextIcon(context);
                    ((MaterialThreeLineTextIcon) view).setPrimaryText(String.format("Primary %d", i));
                    ((MaterialThreeLineTextIcon) view).setSecondaryText(String.format("Primary s dfsfmam dm am dmaw m amwdmawm dma wmd amw ma m cms md mwe v %d", i));
                    ((MaterialThreeLineTextIcon) view).setIcon(getResources().getDrawable(R.drawable.comment));


                    break;

                case 6:
                    view = new MaterialThreeLineTextAvatar(context);
                    ((MaterialThreeLineTextAvatar) view).setPrimaryText(String.format("Primary %d", i));
                    ((MaterialThreeLineTextAvatar) view).setSecondaryText(String.format("Primary s dfsfmam dm am dmaw m amwdmawm dma wmd amw ma m cms md mwe v %d", i));
                    ((MaterialThreeLineTextAvatar) view).setIcon(getResources().getDrawable(R.drawable.comment));

                    break;

                case 7:
                    view = new MaterialThreeLineTextAvatarWithIcon(context);
                    ((MaterialThreeLineTextAvatarWithIcon) view).setPrimaryText(String.format("Primary %d", i));
                    ((MaterialThreeLineTextAvatarWithIcon) view).setSecondaryText(String.format("Primary s dfsfmam dm am dmaw m amwdmawm dma wmd amw ma m cms md mwe v %d", i));
                    ((MaterialThreeLineTextAvatarWithIcon) view).setLeftIcon(getResources().getDrawable(R.drawable.blank_page));
                    ((MaterialThreeLineTextAvatarWithIcon) view).setRightIcon(getResources().getDrawable(R.drawable.check_mark));

                    break;


            }


            return view;
        }
    }
}
