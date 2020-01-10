package com.qy.ccm.bean.other.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qy.ccm.adapter.wallet.WalletTokenAdapter;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/5/9-5:02 PM
 *
 * @author
 */
public class WalletToken extends AbstractExpandableItem<WalletTokenState> implements MultiItemEntity, Serializable {

    /**
     * The user id
     */
    private String uid;

    /**
     * Name of tokens
     */
    private String tokenName;

    /**
     * Chinese name of token currency
     */
    private String tokenNameZhCN;

    /**
     * The address of the token
     */
    private String tokenAddress;

    /**
     * The balance of tokens
     */
    private String amount;

    /**
     * The price of tokens
     */
    private String price;

    /**
     * The total price of tokens
     */
    private String sumPrice;

    /**
     * The market value of the token
     */
    private double marketCap;

    /**
     * Currency price rise (24H)
     */
    private String increaseRate;

    /**
     * Currency price rise (24H)
     */
    private String increasePrice;

    /**
     * Whether to add local wallet
     */
    private boolean localWalletCreated;

    /**
     * The private key to the wallet
     */
    private String privateKey;

    /**
     * The mnemonic word
     */
    private String theMnemonicWord;

    /**
     * The contract address of the token
     */
    private String contractAddress;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenNameZhCN() {
        return tokenNameZhCN;
    }

    public void setTokenNameZhCN(String tokenNameZhCN) {
        this.tokenNameZhCN = tokenNameZhCN;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public String getIncreaseRate() {
        return increaseRate;
    }

    public void setIncreaseRate(String increaseRate) {
        this.increaseRate = increaseRate;
    }

    public String getIncreasePrice() {
        return increasePrice;
    }

    public void setIncreasePrice(String increasePrice) {
        this.increasePrice = increasePrice;
    }

    public boolean isLocalWalletCreated() {
        return localWalletCreated;
    }

    public void setLocalWalletCreated(boolean localWalletCreated) {
        this.localWalletCreated = localWalletCreated;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getTheMnemonicWord() {
        return theMnemonicWord;
    }

    public void setTheMnemonicWord(String theMnemonicWord) {
        this.theMnemonicWord = theMnemonicWord;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    @Override
    public int getItemType() {
        return WalletTokenAdapter.TOKEN;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
