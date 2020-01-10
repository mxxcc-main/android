package com.qy.ccm.utils.blockchain.btc;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.database.WalletCoinUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.function.safety.AES256;
import com.qy.ccm.utils.http.Http;
import com.qy.ccm.utils.http.OnSuccessAndFaultListener;
import com.qy.ccm.utils.http.OnSuccessAndFaultSub;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.litepal.LitePal;
import org.spongycastle.util.encoders.Hex;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccm.web3j.crypto.Hash;

import static ccm.web3j.crypto.MnemonicUtils.generateSeed;
import static com.qy.ccm.constants.UrlConstants.BTC_BASE_API;


/**
 * Description:BitCoin utils
 * Data：2018/12/9-7:25 PM
 */
public class BtcUtils {
    public List<UTXO> utxos;
    public boolean isUtxos;
    //    主节点
    public NetworkParameters networkParameters = MainNetParams.get();
//测试节点
//    public NetworkParameters networkParameters = TestNet3Params.get();
    /**
     * 1
     */
    public Long fastestFee;

    /**
     * 2
     */
    public Long halfHourFee;

    /**
     * 3
     */
    public Long hourFee;
    private String confirmationStr;


    public byte[] concatenate(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        @SuppressWarnings("unchecked")
        byte[] c = (byte[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public String createBtc(String mnemonics, String password, String coin, String mobileMapping, String walletName) {

        byte[] seed = generateSeed(mnemonics, null);
        //3. 生成根私钥 root private key 树顶点的master key ；bip32
        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        // 4. 由根私钥生成 第一个HD 钱包
        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
        // 5. 定义父路径 H则是加强  bip44
        List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/0H/0H/0");
        // 6. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个 （m/44'/0'/0'/0/0）
        DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
        byte[] privateKeyByte = child.getPrivKeyBytes();
        Address address = child.toAddress(networkParameters);
        String btcAddress = address.toBase58();
        byte[] prefix = new byte[]{(byte) 0x80};
        byte[] suffix = new byte[]{(byte) 0x01};

        byte[] rk = concatenate(prefix, privateKeyByte);
        byte[] newrk01 = concatenate(rk, suffix);

        byte[] hash = Hash.sha256(Hash.sha256(newrk01));
        byte[] checksum = Arrays.copyOfRange(hash, 0, 4);
        byte[] newrk = concatenate(newrk01, checksum);

        String btcPrivateKey = Base58.encode(newrk);
        if (StringUtils.isEmpty(password)) {
            return btcAddress;
        }

        String encodeMnemonics = AES256.SHA256Encode(mnemonics, password);
        String encodePrivateKey = AES256.SHA256Encode(btcPrivateKey, password);

        List<WalletBean> getWalletList = getLocalBTC("BTC");
        if (getWalletList != null && getWalletList.size() > 0) {
            //已有数据修改
            DatabaseWalletUtils.updateLocalCoinJson_BTC_ETH("btc.png", "BTC", "BTC", btcAddress, "", encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "BTC");
        } else {
            if ("BTC".equals(coin)) {
                WalletCoinUtils.setCoinData("BTC", btcAddress, Config.getMobleMapping());
                DatabaseWalletUtils.setLocalCoinJson("btc.png", "BTC", "BTC", btcAddress, "", encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "BTC");
            }
        }

        return btcAddress;
    }
//    暂时注释， 后面可能直接不用
//public String createBtc(String mnemonics, String password, String coin) {
//        byte[] seed = generateSeed(mnemonics, null);
//        //3. 生成根私钥 root private key 树顶点的master key ；bip32
//        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
//        // 4. 由根私钥生成 第一个HD 钱包
//        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
//        // 5. 定义父路径 H则是加强  bip44
//        List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/0H/0H/0");
//        // 6. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个 （m/44'/0'/0'/0/0）
//        DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
//        byte[] privateKeyByte = child.getPrivKeyBytes();
//        Address address = child.toAddress(networkParameters);
//        String btcAddress = address.toBase58();
//        byte[] prefix = new byte[]{(byte) 0x80};
//        byte[] suffix = new byte[]{(byte) 0x01};
//
//        byte[] rk = concatenate(prefix, privateKeyByte);
//        byte[] newrk01 = concatenate(rk, suffix);
//
//        byte[] hash = Hash.sha256(Hash.sha256(newrk01));
//        byte[] checksum = Arrays.copyOfRange(hash, 0, 4);
//        byte[] newrk = concatenate(newrk01, checksum);
//
//        String btcPrivateKey = Base58.encode(newrk);
//        if (StringUtils.isEmpty(password)) {
//            return btcAddress;
//        }
//
//        String encodeMnemonics = AES256.SHA256Encode(mnemonics, password);
//        String encodePrivateKey = AES256.SHA256Encode(btcPrivateKey, password);
//        if ("BTC".equals(coin)) {
//            DatabaseWalletUtils.setLocalCoinJson("BTC", "BTC", btcAddress, "", encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping);
//            WalletCoinUtils.setCoinData("BTC", btcAddress);
//        } else if ("USDT".equals(coin)) {
//            DatabaseWalletUtils.setLocalCoinJson("USDT", "USDT", btcAddress, "", encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping);
//            WalletCoinUtils.setCoinData("USDT", btcAddress);
//        } else if ("all".equals(coin)) {
//            DatabaseWalletUtils.setLocalCoinJson("BTC", "BTC", btcAddress, "", encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping);
//            DatabaseWalletUtils.setLocalCoinJson("USDT", "USDT", btcAddress, "", encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping);
//            WalletCoinUtils.setCoinData("BTC", btcAddress);
//            WalletCoinUtils.setCoinData("USDT", btcAddress);
//        }
//        return btcAddress;
//    }

    /**
     * Local created wallet list
     */
    public static List<WalletBean> getLocalBTC(String Coin) {
        List<WalletBean> localCoinBeansBeans = LitePal.where("tokenNameZhCN=?", Coin).find(WalletBean.class);
        if (localCoinBeansBeans.size() > 0) {
            return localCoinBeansBeans;
        } else {
            return null;
        }
    }

    public String inputPrivite(String privateK, String password, String coin, String mobileMapping, String walletName) {


        ECKey ecKey;
        try {
            ecKey = DumpedPrivateKey.fromBase58(networkParameters, privateK).getKey();
        } catch (AddressFormatException e) {
            return null;
        }
        ECKey.fromPrivate(ecKey.getPrivKey(), true).getPublicKeyAsHex();
        Address address = ecKey.toAddress(networkParameters);
        String btcAddress = address.toBase58();

        if (StringUtils.isEmpty(password)) {
            return btcAddress;
        }
        //公钥
        String encodePrivateKey = AES256.SHA256Encode(privateK, password);
        if ("BTC".equals(coin)) {
            WalletCoinUtils.setCoinData("BTC", btcAddress, Config.getMobleMapping());
            DatabaseWalletUtils.setLocalCoinJson("btc.png", "BTC", "BTC", btcAddress, "", "", encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "BTC");
        } else if ("ETH".equals(coin)) {
            WalletCoinUtils.setCoinData("ETH", btcAddress, Config.getMobleMapping());
            DatabaseWalletUtils.setLocalCoinJson("eth.png", "ETH", "ETH", btcAddress, "", "", encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "ETH");
        }
//        else {
//            WalletCoinUtils.setCoinData("USDT", btcAddress, Config.getMobleMapping());
//            DatabaseWalletUtils.setLocalCoinJson("USDT", "USDT", btcAddress, "", "", encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping);
//        }
        return btcAddress;
    }


    public String sign(String fromAddress, String toAddress, String privateKey, long amount, long fee, List<UTXO> utxos) throws Exception {
        Transaction transaction = new Transaction(networkParameters);

        //找零地址
        String changeAddress = fromAddress;
        Long changeAmount = 0L;
        Long utxoAmount = 0L;
        List<UTXO> needUtxos = new ArrayList<UTXO>();
        //获取未消费列表
        if (utxos == null || utxos.size() == 0) {
            throw new Exception("未消费列表为空");
        }
        //遍历未花费列表，组装合适的item
        for (UTXO utxo : utxos) {
            if (utxoAmount >= (amount + fee)) {
                break;
            } else {
                needUtxos.add(utxo);
                utxoAmount += utxo.getValue().value;
            }
        }
        Date date = new Date();
        transaction.setUpdateTime(date);
        transaction.addOutput(Coin.valueOf(amount), Address.fromBase58(networkParameters, toAddress));
        //消费列表总金额 - 已经转账的金额 - 手续费 就等于需要返回给自己的金额了
        changeAmount = utxoAmount - (amount + fee);
        //余额判断
        if (changeAmount < 0) {
            throw new Exception("utxo余额不足");
        }
        //输出-转给自己(找零)
        if (changeAmount > 0) {
            transaction.addOutput(Coin.valueOf(changeAmount), Address.fromBase58(networkParameters, changeAddress));
        }
        //输入未消费列表项
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(networkParameters, privateKey);
        ECKey ecKey = dumpedPrivateKey.getKey();
        for (UTXO utxo : needUtxos) {
            TransactionOutPoint outPoint = new TransactionOutPoint(networkParameters, utxo.getIndex(), utxo.getHash());
            transaction.addSignedInput(outPoint, utxo.getScript(), ecKey, Transaction.SigHash.ALL, true);
        }
        PeerGroup peerGroup = new PeerGroup(networkParameters);
        byte[] bytes = transaction.bitcoinSerialize();
        String hash = Hex.toHexString(transaction.bitcoinSerialize());

        return hash;
    }

    /**
     * Usdt offline signature
     */
    public String omniSign(String fromAddress, String toAddress, String privateKey, Long amount, Long fee, Integer propertyid, List<UTXO> utxos1) throws Exception {
        Transaction tran = new Transaction(networkParameters);
        if (utxos == null || utxos.size() == 0) {
            throw new Exception("utxo为空");
        }
        Long miniBtc = 546L;
        tran.addOutput(Coin.valueOf(miniBtc), Address.fromBase58(networkParameters, toAddress));

        String usdtHex = "6a146f6d6e69" + String.format("%016x", propertyid) + String.format("%016x", amount);
        tran.addOutput(Coin.valueOf(0L), new Script(Utils.HEX.decode(usdtHex)));

        //如果有找零就添加找零
        String changeAddress = fromAddress;
        Long changeAmount = 0L;
        Long utxoAmount = 0L;
        List<UTXO> needUtxo = new ArrayList<>();
        for (UTXO utxo : utxos) {
            if (utxoAmount > (fee + miniBtc)) {
                break;
            } else {
                needUtxo.add(utxo);
                utxoAmount += utxo.getValue().value;
            }
        }
        changeAmount = utxoAmount - (fee + miniBtc);
        //余额判断
        if (changeAmount < 0) {
            throw new Exception("utxo余额不足");
        }
        if (changeAmount > 0) {
            tran.addOutput(Coin.valueOf(changeAmount), Address.fromBase58(networkParameters, changeAddress));
        }

        //先添加未签名的输入，也就是utxo
        for (UTXO utxo : needUtxo) {
            tran.addInput(utxo.getHash(), utxo.getIndex(), utxo.getScript()).setSequenceNumber(TransactionInput.NO_SEQUENCE - 2);
        }
        //下面就是签名
        for (int i = 0; i < needUtxo.size(); i++) {
            ECKey ecKey = DumpedPrivateKey.fromBase58(networkParameters, privateKey).getKey();
            TransactionInput transactionInput = tran.getInput(i);
            Script scriptPubKey = ScriptBuilder.createOutputScript(Address.fromBase58(networkParameters, fromAddress));
            Sha256Hash hash = tran.hashForSignature(i, scriptPubKey, Transaction.SigHash.ALL, false);
            ECKey.ECDSASignature ecSig = ecKey.sign(hash);
            TransactionSignature txSig = new TransactionSignature(ecSig, Transaction.SigHash.ALL, false);
            transactionInput.setScriptSig(ScriptBuilder.createInputScript(txSig, ecKey));
        }

        //这是签名之后的原始交易，直接去广播就行了
        String signedHex = Hex.toHexString(tran.bitcoinSerialize());
        //这是交易的hash
        String txHash = Hex.toHexString(Utils.reverseBytes(Sha256Hash.hash(Sha256Hash.hash(tran.bitcoinSerialize()))));
        return signedHex;
    }

    public void sendTransaction(String hash) {

        String url = BTC_BASE_API+"pushtx";
        Map map = new HashMap();
        map.put("tx", hash);
        Http.postData(url, map, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result, String url) {
            }

            @Override
            public void onFault(String errorMsg, String url) {
                onHttpFault(errorMsg);
            }

            @Override
            public void onException(String errorMsg, String url) {
            }
        }, url));

    }

    public Long getFee(long amount, Long feeRate, List<UTXO> utxos) {
        BigDecimal amountBig = btcFormat(new BigInteger(String.valueOf(amount)));
        //获取费率
        Long utxoAmount = 0L;
        Long fee = 0L;
        Long utxoSize = 0L;
        for (UTXO us : utxos) {
            utxoSize++;
            if (utxoAmount >= (amount + fee)) {
                break;
            } else {
                utxoAmount += us.getValue().value;
            }
        }
        fee = (utxoSize * 148 + 34 * 2 + 10) * feeRate;
        BigDecimal feeBig = btcFormat(new BigInteger(String.valueOf(fee)));
        return fee;
    }

    public List<UTXO> getUsdtList(String address) {
        String url = BTC_BASE_API+"unspent?active=" + address;
        utxos = Lists.newArrayList();
        isUtxos = false;
        Http.getData(url, null, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result, String url) {
                try {
                    //TODO;联网
                    if (org.apache.commons.lang3.StringUtils.equals("No free outputs to spend", result)) {
                    }
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONArray unspentOutputs = jsonObject.getJSONArray("unspent_outputs");
                    List<Map> outputs = JSONObject.parseArray(unspentOutputs.toJSONString(), Map.class);
                    if (outputs == null || outputs.size() == 0) {
                        com.qy.ccm.utils.Utils.Toast("交易异常，余额不足");
                    }
                    for (int i = 0; i < outputs.size(); i++) {
                        Map outputsMap = outputs.get(i);
                        String tx_hash = outputsMap.get("tx_hash").toString();
                        String tx_hash_big_endian = outputsMap.get("tx_hash_big_endian").toString();
                        String tx_index = outputsMap.get("tx_index").toString();
                        String tx_output_n = outputsMap.get("tx_output_n").toString();
                        String script = outputsMap.get("script").toString();
                        String value = outputsMap.get("value").toString();
                        String value_hex = outputsMap.get("value_hex").toString();
                        String confirmations = outputsMap.get("confirmations").toString();
                        UTXO utxo = new UTXO(Sha256Hash.wrap(tx_hash_big_endian), Long.valueOf(tx_output_n), Coin.valueOf(Long.valueOf(value)),
                                0, false, new Script(Hex.decode(script)));
                        utxos.add(utxo);
                    }
                    isUtxos = true;

                } catch (Exception e) {
                    Log.e("==========", "失败getUsdtList:" + url);
                }
            }

            @Override
            public void onFault(String errorMsg, String url) {
                onHttpFault(errorMsg);

            }

            @Override
            public void onException(String errorMsg, String url) {
            }
        }, url));
        return null;
    }

    private void onHttpFault(String errorMsg) {
        Log.e("iozhaq:http:", errorMsg);
    }

    public static int getTransactionList(String hash) {
        Sha256Hash sha256Hash = Sha256Hash.wrap(hash);
        TransactionConfidence confidence = new TransactionConfidence(sha256Hash);
        if (confidence.getDepthInBlocks() < 6) {
            return confidence.getDepthInBlocks();
        }

        return confidence.getDepthInBlocks();
    }


    public static BigDecimal btcFormat(BigInteger value) {
        BigDecimal bd1 = new BigDecimal(value);
        BigDecimal bd2 = new BigDecimal("100000000");
        return bd1.divide(bd2);
    }

    public static BigDecimal btcFormat(BigDecimal value) {
        BigDecimal bd2 = new BigDecimal("100000000");
        return value.divide(bd2);
    }

    public static BigDecimal btcFormat(String value) {
        BigDecimal valueBig = StringUtils.bigDecimal(value);
        BigDecimal bd2 = new BigDecimal("100000000");
        return valueBig.divide(bd2);
    }


    /**
     * Bitcoins Converted Satoshi to Bitcoins
     */
    public static BigInteger btcFormatSat(BigDecimal value) {
        BigDecimal bd2 = new BigDecimal("100000000");
        BigDecimal bd1 = bd2.multiply(value);
        return bd1.toBigInteger();
    }


}
