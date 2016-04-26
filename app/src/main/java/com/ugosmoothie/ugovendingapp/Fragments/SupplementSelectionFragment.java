package com.ugosmoothie.ugovendingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orm.SugarRecord;
import com.ugosmoothie.ugovendingapp.Adapters.SmoothieArrayAdapter;
import com.ugosmoothie.ugovendingapp.Adapters.SupplementArrayAdapter;
import com.ugosmoothie.ugovendingapp.Data.*;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michelle on 3/14/2016.
 */
public class SupplementSelectionFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.supplement_selection_view, container, false);

        final ListView listview = (ListView) rootView.findViewById(R.id.supplementListView);

        checkDefaults();

        List<Supplement> supplements = Supplement.listAll(Supplement.class);

        final SupplementArrayAdapter supplementsAdapter = new SupplementArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, supplements);

        listview.setAdapter(supplementsAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentSelection.getInstance().setCurrentSupplement(Supplement.findById(Supplement.class, id));
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(3);
            }
        });

        return rootView;
    }

    private void checkDefaults() {
        if (Supplement.listAll(Supplement.class).size() == 0) {
            Supplement groundFlaxSeed = new Supplement("Ground Flax Seed", 1.00f);
            groundFlaxSeed.save();

            Supplement pumpkinSeedProtein = new Supplement("Pumpkin Seed Protein", 1.00f);
            pumpkinSeedProtein.save();

            Supplement hempProtein = new Supplement("Hemp Protein", 1.00f);
            hempProtein.save();

            Supplement groundAlmonds = new Supplement("Ground Almonds", 1.00f);
            groundAlmonds.save();

            Supplement none = new Supplement("None", 5.00f);
            none.save();
        }
    }

}
