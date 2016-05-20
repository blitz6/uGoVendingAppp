package com.ugosmoothie.ugovendingapp.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.ugosmoothie.ugovendingapp.Data.*;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;

import java.util.Locale;


/**
 * Created by Michelle on 3/14/2016.
 */
public class SmoothieSelectionFragment extends Fragment {

    private int green_machine = 1;
    private int tropical_paradise = 2;
    private int berry_licious = 3;
    private Boolean lang_french = true;

    Locale myLocale;

    public int getGreen_machine(){
        return green_machine;
    }

    public int getTropical_paradise() {
        return tropical_paradise;
    }

    public int getBerry_licious() {
        return berry_licious;
    }

    public Boolean getLang_french(){
        return lang_french;
    }
    public void setLang_french(Boolean lang_french) {
        this.lang_french = lang_french;
    }

    //Set Language in Locale
    private void setLocal(String language)
    {
        myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

/*        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config,
        getActivity().getBaseContext().getResources().getDisplayMetrics());*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.smoothie_selection_view, container, false);
        final Button lang =  (Button) rootView.findViewById(R.id.lingual_tag);
        RelativeLayout smoothie_g = (RelativeLayout) rootView.findViewById(R.id.smoothie_g);
        RelativeLayout smoothie_t = (RelativeLayout) rootView.findViewById(R.id.smoothie_t);
        RelativeLayout smoothie_b = (RelativeLayout) rootView.findViewById(R.id.smoothie_b);

        //Language Change
        setLocal("fr");
        setLang_french(true);
        lang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getLang_french()) {
                    setLocal("en");
                    setLang_french(false);
                }
                else{
                    setLocal("fr");
                    setLang_french(true);
                }
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


            /*checkDefaults();

            List<Smoothie> smoothies = Smoothie.listAll(Smoothie.class);

            final SmoothieArrayAdapter smoothieAdapter = new SmoothieArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, smoothies);

            listview.setAdapter(smoothieAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CurrentSelection.getInstance().setCurrentSmoothie(Smoothie.findById(Smoothie.class, id));
                    ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);
                }
            });

            return rootView;
        }*/

/*    private void checkDefaults() {
        if (Smoothie.listAll(Smoothie.class).size() == 0) {
            Smoothie greenMachine = new Smoothie("Green Machine", 0l, 0l, 5.00f);
            greenMachine.save();

            Smoothie berryLicious = new Smoothie("Berry-Licious", 0l, 0l, 5.00f);
            berryLicious.save();

            Smoothie tropicalParadise = new Smoothie("Tropical Paradise", 0l, 0l, 5.00f);
            tropicalParadise.save();
        }
    }*/
        return rootView;
    }
}

