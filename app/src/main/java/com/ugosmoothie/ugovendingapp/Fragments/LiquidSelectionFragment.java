package com.ugosmoothie.ugovendingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ugosmoothie.ugovendingapp.Adapters.LiquidArrayAdapter;
import com.ugosmoothie.ugovendingapp.Adapters.SmoothieArrayAdapter;
import com.ugosmoothie.ugovendingapp.Data.CurrentSelection;
import com.ugosmoothie.ugovendingapp.Data.Liquid;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michelle on 3/14/2016.
 */
public class LiquidSelectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.liquid_selection_view, container, false);

        final ListView listview = (ListView) rootView.findViewById(R.id.liquidListView);

        checkDefaults();

        List<Liquid> liquids = Liquid.listAll(Liquid.class);

        final LiquidArrayAdapter liquidAdapter = new LiquidArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, liquids);

        listview.setAdapter(liquidAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentSelection.getInstance().setCurrentLiquid(Liquid.findById(Liquid.class, id));
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(2);
            }
        });

        return rootView;
    }

    private void checkDefaults() {
        if (Liquid.listAll(Liquid.class).size() == 0) {
            Liquid water = new Liquid("Water", 0.00f);
            water.save();
            Liquid coconutWater = new Liquid("Coconut Water", 1.00f);
            coconutWater.save();
            Liquid almondMilk = new Liquid("Almond Milk", 1.00f);
            almondMilk.save();
        }
    }
}