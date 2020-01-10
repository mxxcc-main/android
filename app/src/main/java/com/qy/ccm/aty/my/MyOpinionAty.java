package com.qy.ccm.aty.my;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import butterknife.BindView;

/**
 * Description:意见反馈
 * Data：2019/5/17-2:39 PM
 *
 * @author
 */
public class MyOpinionAty extends BaseAty {
    @BindView(R.id.nv_my_opinion)
    NavigationLucencyLayout nvMyOpinion;

    @Override
    public int initView() {
        return R.layout.aty_my_opinion;
    }

    @Override
    public void setListener() {
        nvMyOpinion.setTitle(getString(R.string.about_opinion));
    }

    @Override
    public void fillData() {

    }
}
