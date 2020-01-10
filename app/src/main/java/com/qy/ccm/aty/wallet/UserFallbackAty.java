package com.qy.ccm.aty.wallet;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.util.TreeMap;

import butterknife.BindView;

public class UserFallbackAty extends BaseAty implements HttpRequestCallback<Object> {

    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nll_wallet_market;

    @BindView(R.id.user_nice_name_id)
    EditText user_nice_name_id;

    @BindView(R.id.user_mobile)
    EditText user_mobile;

    @BindView(R.id.ques_type_id)
    Spinner ques_type_id;

    @BindView(R.id.ques_content_id)
    EditText ques_content_id;

    @BindView(R.id.botton_id)
    Button botton_id;

    private String name;
    private String mobile;
    private String type1;
    private String text;


    @Override
    public int initView() {
        return R.layout.aty_user_fallback;
    }

    @Override
    public void setListener() {
        botton_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (StringUtils.isEmpty(user_nice_name_id.getText().toString())) {

                    Utils.Toast("用户昵称不能为空");
                    return;
                }

                if (StringUtils.isEmpty(user_mobile.getText().toString())) {

                    Utils.Toast("联系方式不能为空");
                    return;
                }

                if (StringUtils.isEmpty(ques_content_id.getText().toString())) {

                    Utils.Toast("详细描述不能为空");
                    return;
                }
                name = user_nice_name_id.getText().toString();
                type1 = ques_type_id.getSelectedItem().toString();
                mobile = user_mobile.getText().toString();
                text = ques_content_id.getText().toString();

                requestData(0);

            }
        });
    }

    @Override
    public void fillData() {

        showStatusBar(true);
        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
        nll_wallet_market.setTitle("用户反馈");
    }

    private void requestData(int type) {
        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;

//        公告
        if (type == 0) {
            map = new TreeMap<String, Object>();
//            {
//                "name": "defwe",
//                    "mobie":"fe2e",
//                    "type": "1",
//                    "text": "1111"
//            }

//            map.put("name", name);
//            map.put("mobie", mobile);
//            map.put("type", type1);
//            map.put("text", text);
            HttpUtils.getHttpUtils().executeGet(this, "addFeedback?name=" + name + "&mobie=" + mobile + "&type=" + type1 + "&text=" + text + "", type, this);
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
                    dialogUtils.dismissProgressDialog();
                    JSONObject jsonObject1 = JSONObject.parseObject(result);

                    Utils.Toast("提交成功");
                    user_nice_name_id.setText("");
                    user_mobile.setText("");
                    ques_content_id.setText("");

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
