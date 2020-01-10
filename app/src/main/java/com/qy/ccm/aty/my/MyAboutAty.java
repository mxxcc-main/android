package com.qy.ccm.aty.my;

import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.util.TreeMap;

import butterknife.BindView;

/**
 * Description:关于我们
 * Data：2019/5/17-1:57 PM
 */
public class MyAboutAty extends BaseAty implements HttpRequestCallback<Object> {
    @BindView(R.id.nl_my_about)
    NavigationLucencyLayout nlMyAbout;

    @BindView(R.id.content_id)
    TextView content_idl;

    @Override
    public int initView() {
        return R.layout.about_us;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        nlMyAbout.setTitle(getString(R.string.about_me));
        requestData(0);

    }


    private void requestData(int type) {

        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;
        if (type == 0) {
            map = new TreeMap<>();
            map.put("systemType", 3);
            HttpUtils.getHttpUtils().executeGet(this, map, "getSystemHelpByType", type, this);
        }
    }

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {

        dialogUtils.dismissProgressDialog();
        try {
            if (!TextUtils.isEmpty(result)) {
                if (type == 0) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    jsonObject = jsonObject.getJSONObject("data");
                    content_idl.setText(Html.fromHtml(jsonObject.getString("content")));
                    dialogUtils.dismissProgressDialog();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接口请求失败
     */
    @Override
    public void onRequestFail(String value, String failCode, int type) {

        dialogUtils.dismissProgressDialog();
        if (!"null".equals(value)) {
            Utils.Toast(value);
        } else {
            Utils.Toast(failCode);
        }
    }

}
