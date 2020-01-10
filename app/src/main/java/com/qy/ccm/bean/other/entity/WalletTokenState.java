package com.qy.ccm.bean.other.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qy.ccm.adapter.wallet.WalletTokenAdapter;

/**
 * Description:
 * Data：2019/5/9-5:01 PM
 *
 * @author
 */
public class WalletTokenState extends AbstractExpandableItem<WalletToken> implements MultiItemEntity {

    public String walletName;
    public String state;

    public WalletTokenState(String walletName, String state) {
        this.walletName = walletName;
        this.state = state;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int getItemType() {
        return WalletTokenAdapter.SITE;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
