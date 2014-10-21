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
            return 15;
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
                    ((MaterialTwoLineText) view).setPrimaryText(String.format("Primary %d", i));
                    break;

                case 1:

                    break;

                case 2:


                    break;

                case 3:


                    break;

                case 4:


                    break;

                case 5:


                    break;

                case 6:


                    break;


            }


            return view;
        }
    }
}
