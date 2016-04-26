package com.ugosmoothie.ugovendingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michelle on 3/5/2016.
 */
public class PurchaseSmoothieFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static class uGoFragment {
        private String description;
        private int resource;

        public uGoFragment(String description, int resource) {
            this.description = description;
            this.resource = resource;
        }

        public String getDescription() {
            return this.description;
        }

        public int getResource() {
            return this.resource;
        }
    }

    public static final Map<Integer, uGoFragment> uGoFragmentMap;
    static {
        Map<Integer, uGoFragment> aMap = new HashMap<Integer, uGoFragment>();
        aMap.put(1, new uGoFragment("SmoothieSelection", R.layout.smoothie_selection_view));
        aMap.put(2, new uGoFragment("LiquidSelection", R.layout.liquid_selection_view));
        aMap.put(3, new uGoFragment("SupplementSelection", R.layout.supplement_selection_view));
        aMap.put(4, new uGoFragment("Summary", R.layout.summary_view));
        aMap.put(5, new uGoFragment("Payment", R.layout.payment_view));
        aMap.put(6, new uGoFragment("Survey", R.layout.survey_view));
        uGoFragmentMap = Collections.unmodifiableMap(aMap);
    }

    public PurchaseSmoothieFragment() {

    }

    public static PurchaseSmoothieFragment newInstance(int sectionNumber) {
        PurchaseSmoothieFragment fragment = new PurchaseSmoothieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        int pageNumber = getArguments().getInt(ARG_SECTION_NUMBER);

        uGoFragment fragment = uGoFragmentMap.get(pageNumber);

        if (fragment != null) {
            rootView = inflater.inflate(fragment.getResource(), container, false);
        } else {
            rootView = inflater.inflate(uGoFragmentMap.get(0).getResource(), container, false);
        }

        return rootView;
    }

}
