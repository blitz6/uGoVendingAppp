package com.ugosmoothie.ugovendingapp.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.ugosmoothie.ugovendingapp.Fragments.InfoFragment;
import com.ugosmoothie.ugovendingapp.R;


/**
 * Created by Michelle on 3/14/2016
 */
public class SmoothieSelectionFragment extends Fragment {

    private int green_machine = 0;
    private int tropical_paradise = 1;
    private int berry_licious = 2;
    private PopupWindow popupWindow;

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.smoothie_selection_view, container, false);
        final Button lang =  (Button) rootView.findViewById(R.id.lingual_tag);
        final Button info = (Button) rootView.findViewById((R.id.info_tag));
        RelativeLayout smoothie_g = (RelativeLayout) rootView.findViewById(R.id.smoothie_g);
        RelativeLayout smoothie_t = (RelativeLayout) rootView.findViewById(R.id.smoothie_t);
        RelativeLayout smoothie_b = (RelativeLayout) rootView.findViewById(R.id.smoothie_b);
        final LinearLayout smoothie_page = (LinearLayout) rootView.findViewById(R.id.smoothie);
        final ViewGroup container_pop = (ViewGroup) inflater.inflate(R.layout.info_popup, null);

        info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Dialog dialog = new Dialog(SmoothieSelectionFragment.this);
                dialog.setContentView(R.layout.info_popup);
                dialog.setCancelable(true);

                //set up button
                final Button close =  (Button) rootView.findViewById(R.id.close_tag);
                close.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(0);
                    }
                });
                //now that the dialog is set up, it's time to show it
                dialog.show();*/
/*

                InfoFragment start_dialog = new InfoFragment(finalInflater);
                start_dialog.show();
*/

                //((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);


/*                popupWindow = new PopupWindow(container, 400, 400, true);
                popupWindow.showAtLocation(smoothie_page, Gravity.NO_GRAVITY, 500, 500);
                //InfoFragment info  = new InfoFragment();
                container_pop.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return true;
                    }

                });*/
                //info.onCreate(savedInstanceState);
               Intent intent = new Intent(getActivity(), InfoFragment.class);

                startActivity(intent);
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
            }
        });
        smoothie_t.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSmoothie(getTropical_paradise());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);
            }
        });
        smoothie_b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSmoothie(getBerry_licious());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);
            }
        });


        return rootView;
    }
}

