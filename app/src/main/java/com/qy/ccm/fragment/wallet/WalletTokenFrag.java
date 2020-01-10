package com.qy.ccm.fragment.wallet;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qy.ccm.R;
import com.qy.ccm.adapter.wallet.WalletTokenAdapter;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.bean.other.database.WalletCoinBean;
import com.qy.ccm.bean.other.entity.WalletToken;
import com.qy.ccm.bean.other.entity.WalletTokenState;
import com.qy.ccm.config.Config;
import com.qy.ccm.fragment.base.BaseFrag;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.database.WalletCoinUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description:代币页面
 * Data：2019/5/9-10:55 AM
 */
public class WalletTokenFrag extends BaseFrag {
    @BindView(R.id.rv_wallet_token)
    RecyclerView rvWalletToken;
    @BindView(R.id.ll_include_gbt)
    LinearLayout llIncludeGbt;

    private String pagerType;
    private WalletTokenAdapter walletTokenAdapter;
    List<MultiItemEntity> tokenEntityList;

    @Override
    public int initView() {
        return R.layout.frag_wallet_token;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        rvWalletToken.setVisibility(View.VISIBLE);
        if ("GBT".equals(pagerType)) {
            rvWalletToken.setVisibility(View.GONE);
            llIncludeGbt.setVisibility(View.VISIBLE);
        } else {
            initTokeData();
            rvWalletToken.setVisibility(View.VISIBLE);
            llIncludeGbt.setVisibility(View.GONE);
        }

    }

    private void initTokeData() {
        tokenEntityList = new ArrayList<>();

        List<WalletCoinBean> walletList = WalletCoinUtils.getCoinList(pagerType,Config.getMobleMapping(),2);
        if (walletList == null || walletList.isEmpty()) {
            return;
        }
        for (WalletCoinBean walletCoinBean : walletList) {
            String coinAddress = walletCoinBean.getCoinAddress();
            List<WalletBean> walletBeans = DatabaseWalletUtils.getWalletCoinList(coinAddress, pagerType,Config.getMobleMapping());
            if (walletBeans == null || walletBeans.isEmpty()) {
                return;
            }
            WalletTokenState walletTokenEntity = new WalletTokenState("钱包1", coinAddress);
            for (int i = 0; i < walletBeans.size(); i++) {
                WalletBean walletBean = walletBeans.get(i);
                WalletToken walletToken = new WalletToken();
                walletToken.setTokenName(walletBean.getTokenName());
                walletToken.setAmount(walletBean.getAmount());
                walletToken.setPrice(walletBean.getPrice());
                walletToken.setTokenAddress(walletBean.getTokenAddress());
                walletToken.setPrivateKey(walletBean.getPrivateKey());
                walletToken.setContractAddress(walletBean.getContractAddress());
                walletToken.setTheMnemonicWord(walletBean.getTheMnemonicWord());
                walletToken.setAmount(walletBean.getAmount());
                walletToken.setTheMnemonicWord(walletBean.getTheMnemonicWord());
                walletTokenEntity.addSubItem(walletToken);

            }
            tokenEntityList.add(walletTokenEntity);
        }
        initAdapter();

    }

    private void initAdapter() {
        walletTokenAdapter = new WalletTokenAdapter(getActivity(), tokenEntityList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvWalletToken.setLayoutManager(manager);
        rvWalletToken.setAdapter(walletTokenAdapter);
//        walletTokenAdapter.expandAll();


    }

    public void setTabText(String str) {
        this.pagerType = str;
    }

    public void notifyData(String coinName) {
        if ("GBT".equals(coinName)) {
            return;
        }

        if (pagerType.equals(coinName)) {
            initTokeData();
        }
    }
}
