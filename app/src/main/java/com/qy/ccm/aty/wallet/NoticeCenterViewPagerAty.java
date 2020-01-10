package com.qy.ccm.aty.wallet;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.utils.FontUtil;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.view.TabPageIndicator;
import com.qy.ccm.view.group.NavigationLucencyLayout;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NoticeCenterViewPagerAty extends BaseAty {

    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nll_wallet_market;

    @BindView(R.id.viewpager_indic)
    TabPageIndicator viewpager_indic;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private NoticeOneFrag noticeOneFrag = new NoticeOneFrag();
    private NoticeTwoFrag noticeTwoFrag = new NoticeTwoFrag();

    private List<Fragment> son_fragment_list = new ArrayList<>();

    @Override
    public int initView() {
        return R.layout.aty_notice_viewpager;
    }

    @Override
    public void setListener() {

        son_fragment_list.add(noticeOneFrag);
        son_fragment_list.add(noticeTwoFrag);

        viewpager.setOffscreenPageLimit(2);                                                       //设置ViewPager的具体Page数目。

        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);                                    // 设置adapter
        viewpager_indic.setViewPager(viewpager);                                     // 绑定indicator


//        =============================
        viewpager_indic.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_WEIGHT_NOEXPAND_SAME);// 设置模式，一定要先设置模式

        viewpager_indic.setDividerPadding(6); //设置
        viewpager_indic.setIndicatorColor(R.color.color_0D8BCA); // 设置底部导航线的颜色
//        viewpager_indic.setTextColorSelected(R.color.color_1F3F59);// 设置tab标题选中的颜色
        viewpager_indic.setIndicatorColorResource(R.color.color_0D8BCA);// 设置tab标题选中的颜色
//        viewpager_indic.textColor = R.color.color_828EA1;// 设置tab标题未被选中的颜色
//        viewpager_indic.textSize = DisplayUtil.sp2px(activity, 16f);// 设置字体大小
        viewpager_indic.setIndicatorHeight(FontUtil.sp2px(this, 2f));
        viewpager_indic.setDividerColorResource(R.color.color_00ffffff);
        viewpager_indic.setUnderlineColor(Color.parseColor("#00FFFFFF"));
    }

    @Override
    public void fillData() {
        showStatusBar(true);
        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
        nll_wallet_market.setTitle("通知中心");
    }


    public class BasePagerAdapter extends FragmentStatePagerAdapter {
        String[] titles = {"转账通知", "系统消息"};

        private BasePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return son_fragment_list.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
