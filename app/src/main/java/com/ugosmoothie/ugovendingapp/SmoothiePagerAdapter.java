package com.ugosmoothie.ugovendingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ugosmoothie.ugovendingapp.Fragments.LiquidSelectionFragment;
import com.ugosmoothie.ugovendingapp.Fragments.PaymentFragment;
import com.ugosmoothie.ugovendingapp.Fragments.SmoothieSelectionFragment;
import com.ugosmoothie.ugovendingapp.Fragments.SummaryFragment;
import com.ugosmoothie.ugovendingapp.Fragments.SupplementSelectionFragment;

/**
 * Created by Michelle on 3/5/2016.
 */
public class SmoothiePagerAdapter extends FragmentStatePagerAdapter {

    public SmoothiePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new SmoothieSelectionFragment();
        } else if (position == 1) {
            return new LiquidSelectionFragment();
        } else if (position == 2) {
            return new SupplementSelectionFragment();
        } else if (position == 3) {
            return new SummaryFragment();
        } else if (position == 4) {
            return new PaymentFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

}
