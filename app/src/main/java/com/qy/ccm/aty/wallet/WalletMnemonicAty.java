package com.qy.ccm.aty.wallet;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qy.ccm.R;
import com.qy.ccm.adapter.wallet.MnemonicAdapter;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.function.safety.AES256;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;

/**
 * Description: Mnemonic
 * Data：2019/5/6-2:56 PM
 */
public class WalletMnemonicAty extends BaseAty {
    @BindView(R.id.nll_wallet_mnemonic)
    NavigationLucencyLayout nllWalletMnemonic;

    @BindView(R.id.rv_mnemonic)
    RecyclerView rvMnemonic;
    private String mnemonic;
    private String coin;
    private String tradePass;

    private List<WalletBean> walletBeans;

    @OnClick({R.id.tv_mnemonic_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mnemonic_next:
//                暂时注释
                Bundle bundle = new Bundle();
                bundle.putString("mnemonic", mnemonic);
                bundle.putString("coin", coin);
                myStartActivity(VerificationMnemonicWordAty.class, bundle);
//                finish();
//                StringUtils.isCopy(mnemonic);

                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.aty_wallet_mnemonic;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {


        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
        nllWalletMnemonic.setTitle("助记词导出");
        tradePass = getIntent().getStringExtra("tradePwd");

        walletBeans =     walletBeans = (List<WalletBean>) getIntent().getSerializableExtra("walletBeans");

        mnemonic = (AES256.SHA256Decrypt(walletBeans.get(0).getTheMnemonicWord(), tradePass));
//        mnemonic = sb.toString();
        List<String> mnemonicList = Arrays.asList(mnemonic.split(" "));
        initAdapter(mnemonicList);

    }

    private void initAdapter(List<String> mnemoniclist) {
        MnemonicAdapter marketListAdapter = new MnemonicAdapter(R.layout.item_mnemonic, this, mnemoniclist);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvMnemonic.setLayoutManager(manager);
        rvMnemonic.setAdapter(marketListAdapter);
    }
}
