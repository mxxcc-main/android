package com.qy.ccm.aty.my;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.SuperUtilKt;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.system.PermissionUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qy.ccm.retrofit.Constants.BASE_URL_ONE;

/**
 * Description:交易页面
 * Data：2019/5/9-8:01 PM
 */
public class InviteCodeAty extends BaseAty {
    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nllWalletMarket;

    @BindView(R.id.ll_id)
    LinearLayout ll_id;

    @BindView(R.id.invite_code)
    TextView invite_code;

    @BindView(R.id.img_core)
    ImageView img_core;

    Bitmap bit;

    @Override
    public int initView() {
        return R.layout.user_invite;
    }

    @Override
    public void setListener() {
        ll_id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                snap();

                return true;
            }
        });
    }

    @OnClick({R.id.invite_code_copy, R.id.download_link, R.id.invite_paper})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite_code_copy:

                StringUtils.isCopy(invite_code.getText().toString());
                break;
            case R.id.download_link:
//                TODO 下载链接
                StringUtils.isCopy(BASE_URL_ONE + "downloads/wToken.html");
                break;
            case R.id.invite_paper:
                snap();
                break;
            default:
                break;
        }
    }

    @Override
    public void fillData() {

        StatusBarUtil.setStatusColor(this, true, true, Color.parseColor("#00000000"));
        nllWalletMarket.setTitle("邀请码");
        invite_code.setText(Config.getInviteCode());
        bit = CodeUtils.createImage(Config.getInviteCode(), 400, 400, null);
//        二维码生成
        img_core.setImageBitmap(bit);

    }

    public void snap() {

        Bitmap bb = SuperUtilKt.ext_saveScreenCropSnap(ll_id);
        if (PermissionUtils.permissionJudge(this, PermissionUtils.WRITE_EXTERNAL_STORAGE)) {
            Utils.saveBmp2Gallery(this.getApplicationContext(), bb, "wToken_" + System.currentTimeMillis() + ".png");
        } else {
            rxPermissions
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        // Always true pre-M
                        if (granted) {
                            Utils.saveBmp2Gallery(this.getApplicationContext(), bb, "wToken_" + System.currentTimeMillis() + ".png");
                        } else {
                            // Oups permission denied
                        }
                    });
        }

//          //TODO 然后，将图片保存在本地。
//          val share_image_file_path = ll_id.ext_saveScreenCropSnap().ext_saveToLocalDirectory(this)         //保存在本地。
//
//        Utils.Toast("邀请图片已保存在相册");//提醒用户

    }

}
