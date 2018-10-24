package com.planera.mis.planera2.activities.adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.fragments.PODFragment;
import com.planera.mis.planera2.activities.utils.AppConstants;

public class ChemistTabsPagerAdapter extends FragmentPagerAdapter {
    public static final int POD_FRAGMENT = 0;
    private Context mContext;
    private Bundle bundle;


    public ChemistTabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public ChemistTabsPagerAdapter(FragmentManager fm, Context context, Bundle bundle) {
        super(fm);
        mContext = context;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == POD_FRAGMENT) {
            PODFragment podFragment = new PODFragment();
            podFragment.setArguments(bundle);
            return podFragment;
        }
        else{
            return null;
        }
    }

    @Override
    public int getCount() {
        return AppConstants.FRAGMENT_COUNT_FOR_CHEMIST;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case POD_FRAGMENT:
                return mContext.getString(R.string.POB_fragment);
            default:
                return null;
        }

    }


}
