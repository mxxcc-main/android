package com.qy.ccm.fragment.wallet;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.adapter.wallet.WalletTransactionAdapter;
import com.qy.ccm.bean.other.entity.WalletToken;
import com.qy.ccm.bean.wallet.RecordBean;
import com.qy.ccm.constants.UrlConstants;
import com.qy.ccm.fragment.base.BaseFrag;
import com.qy.ccm.model.WalletModel;
import com.qy.ccm.utils.function.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Description:交易记录收入
 * Data：2019/5/10-11:08 AM
 *
 * @author
 */
public class WalletTransaction2Frag extends BaseFrag {
    @BindView(R.id.tv_wallet_transaction)
    TextView tvWalletTransaction;
    @BindView(R.id.rv_wallet_transaction)
    RecyclerView rvWalletTransaction;
    List<RecordBean> listStr = new ArrayList<>();


    private String state;
    String tokenName;
    String address;
    String contractaddress;
    WalletToken walletToken;

    @Override
    public int initView() {
        return R.layout.frag_wallet_transaction;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        initData();
        getTransactionRecord();
    }

    private void initData() {
        tokenName = walletToken.getTokenName();
        address = walletToken.getTokenAddress();
        contractaddress = walletToken.getContractAddress();
    }

    private void getTransactionRecord() {
        dialogUtils.showProgressDialog();
        switch (tokenName) {
            case "CCM":
                ethHttp();
                break;
            default:
                Boolean ethTokenState = new WalletModel().token(tokenName);
                if (ethTokenState) {
                    ethTokenHttp();
                } else {
                    dialogUtils.dismissProgressDialog();
                }
                break;
        }
    }

    private void btcHttp() {
        String url = UrlConstants.BTC_RECORD_API + address;
        getHttp(url, null);
    }

    private void usdtHttp() {
        String url = UrlConstants.USDT_DETAILS;
        Map<String, String> map = new HashMap<>();
        map.put("addr", address);
        postHttp(url, map);

    }

    private void ethHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("module", "account");
        map.put("action", "txlist");
        map.put("address", address);
        map.put("startblock", "0");
        map.put("endblock", "99999999");
        map.put("sort", "desc");
        map.put("apikey", "YourApiKeyToken");
        getHttp(UrlConstants.ETH_RECORD_API, map);
    }

    private void ethTokenHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("module", "account");
        map.put("action", "tokentx");
        map.put("address", address);
        map.put("contractaddress", contractaddress);
        map.put("offset", "100");
        map.put("startblock", "0");
        map.put("endblock", "99999999");
        map.put("sort", "desc");
        map.put("apikey", "YourApiKeyToken");
        getHttp(UrlConstants.ETH_RECORD_API, map);

    }


    private void initNum(int size) {
        tvWalletTransaction.setText("共" + size + "笔");
    }

    private void addData(String amount, String time) {
        RecordBean recordBean = new RecordBean();
        recordBean.setAmunt(amount);
        String timeStr = TimeUtils.TimeStamp2Date(time, TimeUtils.FORMAT_TYPE_3);
        recordBean.setTime(timeStr);
        listStr.add(recordBean);
    }

    @Override
    public void onHttpFault(String errorMsg, String url) {
        super.onHttpFault(errorMsg, url);

    }

    private void initAdapter() {
        initNum(listStr.size());
        WalletTransactionAdapter transactionAdapter = new WalletTransactionAdapter(R.layout.item_wallet_transaction, listStr, tokenName);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvWalletTransaction.setLayoutManager(manager);
        rvWalletTransaction.setAdapter(transactionAdapter);

    }

    public void setData(String state, WalletToken walletToken) {
        this.state = state;
        this.walletToken = walletToken;
    }
}
