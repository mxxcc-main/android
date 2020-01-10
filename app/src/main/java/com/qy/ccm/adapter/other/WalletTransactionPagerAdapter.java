package com.qy.ccm.adapter.other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qy.ccm.fragment.wallet.WalletTransactionFrag;

import java.util.List;

/**
 * Description:
 * Data：2019/5/7-8:16 PM
 *
 * @author
 */
public class WalletTransactionPagerAdapter extends FragmentPagerAdapter {


    List<Fragment> datas;

    public void setData(List<Fragment> datas) {
        this.datas = datas;
    }

    public WalletTransactionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }
}
