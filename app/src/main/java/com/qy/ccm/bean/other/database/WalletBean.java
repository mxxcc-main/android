package com.qy.ccm.bean.other.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/5/8-11:47 AM
 *
 * @author
 */
public class WalletBean extends LitePalSupport implements Serializable {

    /**
     * The user id
     */
    @Column(defaultValue = "unknown")
    private String uid;

    private String coin;

    /**
     * 钱包名称
     */
    private String walletName;

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

    /**
     * the coin img url
     */
    private String iconUrl;

    /**
     * 与手机号关联
     */
    private String mobileMapping;


    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
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

    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getMobileMapping() {
        return mobileMapping;
    }

    public void setMobileMapping(String mobileMapping) {
        this.mobileMapping = mobileMapping;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
}
