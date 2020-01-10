package com.qy.ccm.model;

import android.util.Log;
import android.widget.TextView;

import com.qy.ccm.utils.blockchain.btc.BtcUtils;
import com.qy.ccm.utils.blockchain.eth.CCMUtils;
import com.qy.ccm.utils.blockchain.eth.ETHUtils;
import com.qy.ccm.utils.function.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 * Data：2019/5/11-11:56 AM
 *
 * @author
 */
public class FeeModel {

    /**
     * 本地转账
     */
    public String localTransafer(String tokenName, String privateKey, String toAddress, String amount, BigInteger GasPrice) throws ExecutionException, InterruptedException {
        CCMUtils ethUtils = new CCMUtils();
        String msg = null;
        switch (tokenName) {
            case "CCM":
                msg = ethUtils.transfer(privateKey, toAddress, amount, GasPrice);
                break;
            default:
                break;
        }
        return msg;
    }

    /**
     * @param tvInfoFee
     * @param tokenName
     * @param fee
     * @param tokenPrice
     */
    public void feeDisplay(TextView tvInfoFee, String tokenName, BigDecimal fee, BigDecimal tokenPrice) {
        Log.e("=========", "12121");
        //钱包
        BigDecimal targetFeeBig;
        switch (tokenName) {
            case "BTC":
            case "USDT":
                BigDecimal newFee = BtcUtils.btcFormat(fee);
                //token价格
                tvInfoFee.setText(StringUtils.bigDecimal8(newFee) + " BTC");

                break;
            case "ETH":
                targetFeeBig = ETHUtils.formatEth(fee.toBigIntegerExact());
                tvInfoFee.setText(StringUtils.bigDecimal8(targetFeeBig) + " ETH");
                break;
            case "CCM":
                targetFeeBig = CCMUtils.formatEth(fee.toBigIntegerExact());
                tvInfoFee.setText(StringUtils.bigDecimal8(targetFeeBig) + " CCM");
                break;
            default:
                Boolean ethTokenState = new WalletModel().token(tokenName);
                if (ethTokenState == true) {
                    BigDecimal tokenFeeBig = CCMUtils.formatEth(fee.toBigIntegerExact());
                    tvInfoFee.setText(StringUtils.bigDecimal8(tokenFeeBig) + " CCM");
                }

                break;

        }
    }

}
