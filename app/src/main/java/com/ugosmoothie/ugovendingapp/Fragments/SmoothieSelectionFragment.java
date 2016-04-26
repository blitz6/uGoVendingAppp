package com.ugosmoothie.ugovendingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ugosmoothie.ugovendingapp.Adapters.LiquidArrayAdapter;
import com.ugosmoothie.ugovendingapp.Adapters.SmoothieArrayAdapter;
import com.ugosmoothie.ugovendingapp.Data.*;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michelle on 3/14/2016.
 */
public class SmoothieSelectionFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.smoothie_selection_view, container, false);

        final ListView listview = (ListView) rootView.findViewById(R.id.listview);

        checkDefaults();

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
    }

    private void checkDefaults() {
        if (Smoothie.listAll(Smoothie.class).size() == 0) {
            Smoothie greenMachine = new Smoothie("Green Machine", 0l, 0l, 5.00f);
            greenMachine.save();

            Smoothie berryLicious = new Smoothie("Berry-Licious", 0l, 0l, 5.00f);
            berryLicious.save();

            Smoothie tropicalParadise = new Smoothie("Tropical Paradise", 0l, 0l, 5.00f);
            tropicalParadise.save();
        }
    }
}
