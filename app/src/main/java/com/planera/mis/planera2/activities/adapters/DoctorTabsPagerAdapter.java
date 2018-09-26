package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.fragments.BrandsFragment;
import com.planera.mis.planera2.activities.fragments.GiftFragment;
import com.planera.mis.planera2.activities.fragments.SampleFragment;
import com.planera.mis.planera2.activities.utils.AppConstants;

public class DoctorTabsPagerAdapter extends FragmentPagerAdapter {
    public static final int BRANDS_FRAGMENT = 0;
    public static final int GIFT_FRAGMENT = 1;
    public static final int SAMPLE_FRAGMENT = 2;
    private Context mContext;


    public DoctorTabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == BRANDS_FRAGMENT) {
            return new BrandsFragment();
        }
        if(position == GIFT_FRAGMENT){
            return GiftFragment.newInstance(null, null);
        }
        else{
            return new SampleFragment();
        }

    }

    @Override
    public int getCount() {
      return AppConstants.FRAGMENT_COUNT_FOR_DOCTOR;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case BRANDS_FRAGMENT:
                return mContext.getString(R.string.brands_fragment);
            case GIFT_FRAGMENT:
                return mContext.getString(R.string.gift_fragment);
            case SAMPLE_FRAGMENT:
                return mContext.getString(R.string.sample_fargemt);
            default:
                return null;
        }

    }


}
