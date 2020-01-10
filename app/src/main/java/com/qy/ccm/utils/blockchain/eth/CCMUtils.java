package com.qy.ccm.utils.blockchain.eth;


import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.qy.ccm.config.Config;
import com.qy.ccm.config.EthConfig;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.database.WalletCoinUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.function.safety.AES256;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ccm.web3j.abi.FunctionEncoder;
import ccm.web3j.abi.FunctionReturnDecoder;
import ccm.web3j.abi.TypeReference;
import ccm.web3j.abi.datatypes.Address;
import ccm.web3j.abi.datatypes.Function;
import ccm.web3j.abi.datatypes.Type;
import ccm.web3j.abi.datatypes.generated.Uint256;
import ccm.web3j.abi.datatypes.generated.Uint8;
import ccm.web3j.crypto.CipherException;
import ccm.web3j.crypto.Credentials;
import ccm.web3j.crypto.ECKeyPair;
import ccm.web3j.crypto.Keys;
import ccm.web3j.crypto.RawTransaction;
import ccm.web3j.crypto.TransactionEncoder;
import ccm.web3j.crypto.Wallet;
import ccm.web3j.crypto.WalletFile;
import ccm.web3j.protocol.Web3j;
import ccm.web3j.protocol.core.DefaultBlockParameterName;
import ccm.web3j.protocol.core.methods.request.Transaction;
import ccm.web3j.protocol.core.methods.response.EthCall;
import ccm.web3j.protocol.core.methods.response.EthEstimateGas;
import ccm.web3j.protocol.core.methods.response.EthGetTransactionCount;
import ccm.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import ccm.web3j.protocol.core.methods.response.EthSendTransaction;
import ccm.web3j.protocol.core.methods.response.TransactionReceipt;
import ccm.web3j.tx.Transfer;
import ccm.web3j.utils.Convert;
import ccm.web3j.utils.Numeric;

import static ccm.web3j.crypto.MnemonicUtils.generateSeed;

/**
 * Description:Ethereum utils
 * Data：2018/12/5-8:35 PM
 */
public class CCMUtils {
    private Web3j web3j;

    public CCMUtils() {
        web3j = Web3JClient.getClient();
    }

    /**
     * Create a wallet
     */
    public String createWallet(String mnemonics, String password, String coin, String mobileMapping, String walletName) throws CipherException {
        //2.生成种子
        byte[] seed = generateSeed(mnemonics, null);
        //3. 生成根私钥 root private key 树顶点的master key ；bip32
        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        // 4. 由根私钥生成 第一个HD 钱包
        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
        // 5. 定义父路径 H则是加强 imtoken中的eth钱包进过测试发现使用的是此方式生成 bip44
        List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/60H/0H/0");
        // 6. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个 （m/44'/60'/0'/0/0）
        DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
        byte[] privateKeyByte = child.getPrivKeyBytes();
        //I don't have 0x in front
        String privateKey = child.getPrivateKeyAsHex();
        ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
        //I don't have 0x in front
        String privateKeyYes = Numeric.toHexStringNoPrefix(keyPair.getPrivateKey());
        //The front zone 0 x
        String privateKeyNo1 = Numeric.toHexStringWithPrefix(keyPair.getPrivateKey());
        //The front zone 0 x
        String privateKeyNo2 = Numeric.toHexStringWithPrefixSafe(keyPair.getPrivateKey());
        String publicKey = Numeric.toHexStringNoPrefix(keyPair.getPublicKey());
        WalletFile walletFile = Wallet.createLight("", keyPair);
        String ethAddress = Keys.toChecksumAddress(walletFile.getAddress());
        Gson gson = new Gson();
        String keystore = gson.toJson(walletFile);
        if (StringUtils.isEmpty(password)) {
            return ethAddress;
        }
        tokenList(ethAddress, mnemonics, privateKey, password, mobileMapping, walletName);

        return ethAddress;
    }


    /**
     * Import a private key
     */
    public String importPrivate(String privateKey, String password, String mobileMapping, String walletName) throws CipherException {
        Credentials credentials;
        try {
            credentials = Credentials.create(privateKey);
        } catch (NumberFormatException e) {
            Utils.Toast("私钥地址不合法");
            return null;
        }

        ECKeyPair ecKeyPair = credentials.getEcKeyPair();
        String address = credentials.getAddress();
        String ethAddress = Keys.toChecksumAddress(address);
        if (StringUtils.isEmpty(password)) {
            return address;
        }

        tokenList(ethAddress, "", privateKey, password, mobileMapping, walletName);
        return address;
    }

    private void tokenList(String ethAddress, String mnemonics, String privateKey, String password, String mobileMapping, String walletName) {
        String encodeMnemonics = "";
        if (StringUtils.isNotEmpty(mnemonics)) {
            encodeMnemonics = AES256.SHA256Encode(mnemonics, password);
        }
        String encodePrivateKey = AES256.SHA256Encode(privateKey, password);

        //CCM
        WalletCoinUtils.setCoinData("CCM", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/ccm.png", "CCM", "CCM", ethAddress, "", encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //SAR
        WalletCoinUtils.setCoinData("SAR", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAR.png", "SAR", "SAR", ethAddress, EthConfig.SAR_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //SAR-ASI
        WalletCoinUtils.setCoinData("SAR-ASI", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAR-ASI.png", "SAR-ASI", "SAR-ASI", ethAddress, EthConfig.SAR_ASI_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
//        //CCM-ISR
        WalletCoinUtils.setCoinData("CCM-ISR", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/CCM-ISR.png", "CCM-ISR", "CCM-ISR", ethAddress, EthConfig.CCM_ISR_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
//        //SAR-SAN
        WalletCoinUtils.setCoinData("SAR-SAN", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAR-SAN.png", "SAR-SAN", "SAR-SAN", ethAddress, EthConfig.SAR_SAN_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
//        //HKDD
        WalletCoinUtils.setCoinData("HKDD", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/HKDD.png", "HKDD", "HKDD", ethAddress, EthConfig.HKDD_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
//        //SAR-S, mobileMapping
        WalletCoinUtils.setCoinData("SAR-S", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAR-S.png", "SAR-S", "SAR-S", ethAddress, EthConfig.SAR_S_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
//        //ISR-S, mobileMapping
        WalletCoinUtils.setCoinData("ISR-S", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/ISR-S.png", "ISR-S", "ISR-S", ethAddress, EthConfig.ISR_S_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
//        //CCM-S, mobileMapping
        WalletCoinUtils.setCoinData("CCM-S", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/CCM-S.png", "CCM-S", "CCM-S", ethAddress, EthConfig.CCM_S_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
//        //SAR-ES, mobileMapping
        WalletCoinUtils.setCoinData("SAR-ES", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAR-ES.png", "SAR-ES", "SAR-ES", ethAddress, EthConfig.SAR_ES_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
//        //HKDD-S, mobileMapping
        WalletCoinUtils.setCoinData("HKDD-S", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/HKDD-S.png", "HKDD-S", "HKDD-S", ethAddress, EthConfig.HKDD_S_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
//  add  new   =========================================================================
        //        //SAN-0301, mobileMapping
        WalletCoinUtils.setCoinData("SAN-0301", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-0301", "SAN-0301", ethAddress, EthConfig.SAN_0301_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //        //SAN-0306, mobileMapping
        WalletCoinUtils.setCoinData("SAN-0306", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-0306", "SAN-0306", ethAddress, EthConfig.SAN_0306_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //        //SAN-0502, mobileMapping
        WalletCoinUtils.setCoinData("SAN-0502", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-0502", "SAN-0502", ethAddress, EthConfig.SAN_0502_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //        //SAN-3105, mobileMapping
        WalletCoinUtils.setCoinData("SAN-3105", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-3105", "SAN-3105", ethAddress, EthConfig.SAN_3105_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //        //SAN-3301, mobileMapping
        WalletCoinUtils.setCoinData("SAN-3301", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-3301", "SAN-3301", ethAddress, EthConfig.SAN_3301_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //        //SAN-2101, mobileMapping
        WalletCoinUtils.setCoinData("SAN-2101", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-2101", "SAN-2101", ethAddress, EthConfig.SAN_2101_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //        //SAN-2105, mobileMapping
        WalletCoinUtils.setCoinData("SAN-2105", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-2105", "SAN-2105", ethAddress, EthConfig.SAN_2105_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //        //SAN-2406, mobileMapping
        WalletCoinUtils.setCoinData("SAN-2406", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-2406", "SAN-2406", ethAddress, EthConfig.SAN_2406_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        //        //SAN-410101, mobileMapping
        WalletCoinUtils.setCoinData("SAN-410101", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-410101", "SAN-410101", ethAddress, EthConfig.SAN_410101_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");

        WalletCoinUtils.setCoinData("SAN-2906", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-2906", "SAN-2906", ethAddress, EthConfig.SAN_2906_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");

        WalletCoinUtils.setCoinData("SAN-3201", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-3201", "SAN-3201", ethAddress, EthConfig.SAN_3201_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");

        WalletCoinUtils.setCoinData("SAN-1606", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-1606", "SAN-1606", ethAddress, EthConfig.SAN_1606_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");

        WalletCoinUtils.setCoinData("SAN-2901", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-2901", "SAN-2901", ethAddress, EthConfig.SAN_2901_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");

        WalletCoinUtils.setCoinData("SAN-410101S", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-410101S", "SAN-410101S", ethAddress, EthConfig.SAN_410101S_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        WalletCoinUtils.setCoinData("SAN-1106", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-1106", "SAN-1106", ethAddress, EthConfig.SAN_1106_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        WalletCoinUtils.setCoinData("SAN-1104", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-1104", "SAN-1104", ethAddress, EthConfig.SAN_1104_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        WalletCoinUtils.setCoinData("SAN-1802", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-1802", "SAN-1802", ethAddress, EthConfig.SAN_1802_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");
        WalletCoinUtils.setCoinData("SAN-410102", ethAddress, Config.getMobleMapping());
        DatabaseWalletUtils.setLocalCoinJson("https://raw.githubusercontent.com/iozhaq/image/master/SAN.png", "SAN-410102", "SAN-410102", ethAddress, EthConfig.SAN_410102_CONTRACT, encodeMnemonics, encodePrivateKey, BigDecimal.ZERO, BigDecimal.ZERO, true, mobileMapping, walletName, "CCM");


    }


    /**
     * Query eth balance
     */
    public BigDecimal getBalance(String address) {
//        address="0x13ca687c1b3d7e35ea4738d21b8f91c707aba0ef";
        BigInteger ethGetBalance = null;
        try {
            ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ethGetBalance != null) {
            return formatEth(ethGetBalance);
        }


        return new BigDecimal(0);
    }

    /**
     * Check the balance of erc20 token
     */
    public BigDecimal getTokenBalance(String fromAddress, String contractAddress) {
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);
        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);
        EthCall ethCall = null;
        BigInteger balanceValue = BigInteger.ZERO;

        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ethCall != null && StringUtils.isNotEmpty(ethCall.getValue())) {
            String ethValue = ethCall.getValue();
            List<Type> results = FunctionReturnDecoder.decode(ethValue, function.getOutputParameters());
            balanceValue = (BigInteger) results.get(0).getValue();
        }

        //        查询合约精度
        int coinTokenDecmial = getTokenDecimals(contractAddress);

        return new BigDecimal(balanceValue).divide(BigDecimal.TEN.pow(coinTokenDecmial));
    }


    /**
     * Ethereum transfer
     *
     * @param privateKey Private key
     * @param toAddress  The target address
     * @param amount     Contract address
     * @throws ExecutionException   ExecutionException
     * @throws InterruptedException InterruptedException
     */
    public String transfer(String privateKey, String toAddress, String amount, BigInteger GAS_PRICE) throws ExecutionException, InterruptedException {
        Credentials credentials = Credentials.create(privateKey);
        Convert.Unit unit = Convert.Unit.ETHER;
        BigDecimal weiValue = Convert.toWei(amount, unit);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Wei value provided: " + amount + " " + unit.toString()
                            + " = " + weiValue + " Wei");
        }

        //nonce
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        //RawTransaction
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, GAS_PRICE, Transfer.GAS_LIMIT, toAddress, weiValue.toBigIntegerExact());
        BigInteger gas = GAS_PRICE.multiply(Transfer.GAS_LIMIT);
        BigDecimal ethAmount = formatEth(gas);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, (byte) 10, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String transactionHash = ethSendTransaction.getTransactionHash();
        if (ethSendTransaction.getError() != null && ethSendTransaction.getError().getCode() != 1) {
            return ethSendTransaction.getError().getMessage();
        }
        return transactionHash;
    }

    /**
     * Ethereum token transfer
     *
     * @param privateKey      Private key
     * @param toAddress       The target address
     * @param amount          Transfer credits
     * @param contractAddress Contract address
     * @throws ExecutionException ExecutionException
     */
    public String tokenTransfer(String privateKey, String toAddress, String amount, String contractAddress) {
        Credentials credentials = Credentials.create(privateKey);

//        获取合约代币精度
//        Convert.Unit unit = Convert.Unit.ETHER;
//        Convert.Unit unit = Convert.Unit.ETHER;
        int unit = getTokenDecimals(contractAddress);

        Uint256 tokenValue = new Uint256(new BigDecimal(amount).multiply(BigDecimal.TEN.pow(unit)).toBigInteger());

        //nonce
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            List<Type> inputParameters = Arrays.asList(new Address(toAddress), tokenValue);
            List<TypeReference<?>> outputParameters = Arrays.asList(new TypeReference<Type>() {
            });
            Function function = new Function(
                    "transfer",
                    inputParameters,
                    outputParameters);

            String encodedFunction = FunctionEncoder.encode(function);

            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce,
                    Convert.toWei("18", Convert.Unit.GWEI).toBigInteger(),
                    Convert.toWei("100000", Convert.Unit.WEI).toBigInteger(),
                    contractAddress,
                    encodedFunction
            );
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, (byte) 10, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            Log.e("=============", "transferHexValue:" + hexValue);

            EthSendTransaction ethSendTransaction = null;
            try {
                ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String transactionHash = ethSendTransaction.getTransactionHash();
            if (ethSendTransaction.getError() != null && ethSendTransaction.getError().getCode() != 1) {
                Log.e("=============", "errorData:" + ethSendTransaction.getError().getData());
                Log.e("=============", "errorMsg:" + ethSendTransaction.getError().getMessage());
                Log.e("=============", "errorCode:" + ethSendTransaction.getError().getCode());
                return ethSendTransaction.getError().getMessage();
            }
            return transactionHash;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }


    /**
     * Capture gas consumption
     */
    public BigInteger getTransactionGasLimit(String fromAddress, String toAddress, BigInteger gasPrice, String privateKey, BigInteger value) throws ExecutionException, InterruptedException {

        Credentials credentials = Credentials.create(privateKey);
        BigInteger nonce = null;
        try {
            nonce = getNone(credentials);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Transaction transaction = Transaction.createEtherTransaction(fromAddress, nonce, gasPrice, null, toAddress, value);
        EthEstimateGas ethEstimateGas = null;
        try {
            ethEstimateGas = web3j.ethEstimateGas(transaction).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ethEstimateGas.getAmountUsed();
    }


    /**
     * Capture gas consumption
     */
    public static BigInteger getTransactionGasLimit(Web3j web3j, Transaction transaction) throws ExecutionException, InterruptedException {

        EthEstimateGas ethEstimateGas = null;
        try {
            ethEstimateGas = web3j.ethEstimateGas(transaction).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ethEstimateGas.hasError()) {
            throw new RuntimeException(ethEstimateGas.getError().getMessage());
        }
        return ethEstimateGas.getAmountUsed();

    }


    /**
     * 查询代币精度
     *
     * @param contractAddress
     * @return
     * @throws Exception
     */
    public int getTokenDecimals(String contractAddress) {
        String methodName = "decimals";
        String fromAddr = "0x0000000000000000000000000000000000000000";
        int decimal = 0;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        String ethCall = null;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send().getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Type> results = FunctionReturnDecoder.decode(ethCall, function.getOutputParameters());
        if (!results.isEmpty()) {
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        }
        return decimal;
    }

    /**
     * Transaction status
     */
    public String getTransactionStatus(String hash) {
        EthGetTransactionReceipt txReceipt = null;
        try {
            txReceipt = web3j.ethGetTransactionReceipt(hash).send();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (txReceipt == null) {
            return "Optional.empty";
        }
        java.util.Optional<TransactionReceipt> receipt = txReceipt.getTransactionReceipt();


        /**
         * status:
         * Optional.empty:Chain processing
         * 0x0：Precessing on the chain failed
         * 0x1：Successful processing on chain
         */
        String status;
        if ("Optional.empty".contains(receipt.toString())) {
            status = "Optional.empty";
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                status = receipt.get().getStatus();

                return status;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                boolean present = receipt.isPresent();
            }
        }
        return "";
    }

    /**
     * return none
     */
    private BigInteger getNone(Credentials credentials) throws ExecutionException, InterruptedException, IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        return ethGetTransactionCount.getTransactionCount();
    }

    public static BigDecimal formatEth(BigInteger value) {
        BigDecimal bd1 = new BigDecimal(value);
        BigDecimal bd2 = new BigDecimal(1_000_000_000_000_000_000L);
        return bd1.divide(bd2);
    }

//    public static BigDecimal formatEthByToken(BigInteger value, String constractAddress) {
//        BigDecimal bd1 = new BigDecimal(value);
//        //        查询合约精度
//        getTokenDecimals(constractAddress);
//
//        BigDecimal bd2 = new BigDecimal(1_000_000_000_000_000_000L);
//        return bd1.divide(bd2);
//    }

    public static BigDecimal formatEth(BigDecimal bd1) {
        BigDecimal bd2 = new BigDecimal(1_000_000_000_000_000_000L);
        return bd1.divide(bd2);
    }


    public static BigDecimal formatWei(BigDecimal value) {
        BigDecimal bd2 = new BigDecimal(1_000_000_000L);
        return value.multiply(bd2);
    }
//    public static String getBalanceOf2(String walletAddress) {
//        Web3j web3j = Web3j.build(new HttpService(ip));
//        BigDecimal fromWei = new BigDecimal("0.00");
//        try {
//            EthGetBalance ethGetBalance = web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
//            BigInteger balance = ethGetBalance.getBalance();
//            System.out.println("balance is :===========>" + balance);
//            fromWei = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
//            System.out.println("ethbalance is :===========>" + fromWei.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return fromWei.toString();
//    }
    /**
     * 查询代币余额
     */
//    @SuppressWarnings("rawtypes")
//    public static String getTokenBalance(String fromAddress, String contractAddress) {
//        try {
//            int decimals = getTokenDecimals(contractAddress);
//            String methodName = "balanceOf";
//            List<Type> inputParameters = new ArrayList<>();
//            List<TypeReference<?>> outputParameters = new ArrayList<>();
//            Address address = new Address(fromAddress);
//            inputParameters.add(address);
//
//            TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
//            };
//            outputParameters.add(typeReference);
//            Function function = new Function(methodName, inputParameters, outputParameters);
//            String data = FunctionEncoder.encode(function);
//            Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);
//
//            BigInteger balanceValue = BigInteger.ZERO;
//            web3j = Web3j.build(new HttpService(network));
//            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
//            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//            if (CollectionUtils.isNotEmpty(results)) {
//                balanceValue = (BigInteger) results.get(0).getValue();
//            }
//            logger.info("代币余额为: " + new BigDecimal(balanceValue).divide(BigDecimal.TEN.pow(decimals)).toString());
//            return new BigDecimal(balanceValue).divide(BigDecimal.TEN.pow(decimals)).toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "0";
//    }
}
