package com.ugosmoothie.ugovendingapp.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;

import static android.app.PendingIntent.getActivity;

/**
 * Created by SachinApiah on 7/4/2016.
 */
public class InfoFragment extends Fragment {
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.info_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width * (9 / 10), height * (9 / 10));


    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.info_popup, container, false);
       // rootView.setAnimation(AnimationUtils.loadAnimation(this, R.animator.popupanim));

/*        try{
            inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflater.inflate(R.layout.info_popup, container, false);
            rootView.setAnimation(AnimationUtils.loadAnimation(this, R.animator.popupanim));

        }*/



        final Button close =  (Button) rootView.findViewById(R.id.close_tag);

        rootView.getBackground().setAlpha(50);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(0);
            }
        });
        return rootView;
    }
}
