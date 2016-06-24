package com.ugosmoothie.ugovendingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.ugosmoothie.ugovendingapp.Data.*;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;


/**
 * Created by Michelle on 3/14/2016
 */
public class SmoothieSelectionFragment extends Fragment {

    private int green_machine = 0;
    private int tropical_paradise = 1;
    private int berry_licious = 2;

    public int getGreen_machine(){
        return green_machine;
    }

    public int getTropical_paradise() {
        return tropical_paradise;
    }

    public int getBerry_licious() {
        return berry_licious;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.smoothie_selection_view, container, false);
        final Button lang =  (Button) rootView.findViewById(R.id.lingual_tag);
        final Button info = (Button) rootView.findViewById((R.id.info_tag));
        RelativeLayout smoothie_g = (RelativeLayout) rootView.findViewById(R.id.smoothie_g);
        RelativeLayout smoothie_t = (RelativeLayout) rootView.findViewById(R.id.smoothie_t);
        RelativeLayout smoothie_b = (RelativeLayout) rootView.findViewById(R.id.smoothie_b);

        //final LayoutInflater finalInflater = inflater;

        info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
/*                View info_view = finalInflater.inflate(R.layout.info_popup, container, false);
                PopupWindow info_window = new PopupWindow(info_view, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                info_window.setContentView(info_view);*/
                ((PurchaseSmoothie) getActivity()).Infopopup();
            }

        });

        lang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).ToggleLanguage();
            }

        });

        //Choosing Smoothie
        smoothie_g.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSmoothie(getGreen_machine());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);
                //((PurchaseSmoothie)getActivity()).refresh_curr_frag();
            }
        });
        smoothie_t.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSmoothie(getTropical_paradise());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);
                //((PurchaseSmoothie)getActivity()).refresh_curr_frag();
            }
        });
        smoothie_b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSmoothie(getBerry_licious());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);
                //((PurchaseSmoothie)getActivity()).refresh_curr_frag();
            }
        });


        return rootView;
    }
}

