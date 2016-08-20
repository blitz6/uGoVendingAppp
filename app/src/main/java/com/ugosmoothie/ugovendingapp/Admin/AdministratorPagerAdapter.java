package com.ugosmoothie.ugovendingapp.Admin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ugosmoothie.ugovendingapp.Admin.Fragments.LogExportFragment;
import com.ugosmoothie.ugovendingapp.Admin.Fragments.MachineTestingFragment;
import com.ugosmoothie.ugovendingapp.Fragments.LiquidSelectionFragment;
import com.ugosmoothie.ugovendingapp.Fragments.PaymentFragment;
import com.ugosmoothie.ugovendingapp.Fragments.SmoothieSelectionFragment;
import com.ugosmoothie.ugovendingapp.Fragments.SummaryFragment;
import com.ugosmoothie.ugovendingapp.Fragments.SupplementSelectionFragment;
import com.ugosmoothie.ugovendingapp.Fragments.ThankyouFragment;

/**
 * Created by Michelle on 8/20/2016.
 */
public class AdministratorPagerAdapter extends FragmentStatePagerAdapter {

    public AdministratorPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new LogExportFragment();
        } else if (position == 1) {
            return new MachineTestingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}

