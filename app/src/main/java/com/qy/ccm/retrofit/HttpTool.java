package com.qy.ccm.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by zhoufan on 2016/6/22 0022.
 * 辅助类
 */

public class HttpTool {


    private static String signTopRequest(Map<String, Object> params, String signMethod) throws IOException {
        if (params.containsKey("clientId")) {
            String keyValue = String.valueOf(params.get("clientId"));
            String values = String.valueOf(params.get("clientSecret"));
            params.remove("clientId");
            params.remove("clientSecret");
            params.put(keyValue, values);
        }
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            Object value = params.get(key);
            if (value instanceof String) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty((String) value)) {
                    if (key.equals("clientId")) {

                    }
                    query.append(key).append("=").append(value).append("&");
                }
            } else if (value instanceof File) {
                String strVal = getFileContent((File) value);
                query.append(key).append(strVal);
                params.remove(key);
            }
        }

        // 第三步：使用MD5/HMAC加密
        String value = query.toString().substring(0, query.toString().length() - 1);
        Log.e("steven", value);
        byte[] bytes = encryptMD5(value);
//        if (Constants.SIGN_METHOD_HMAC.equals(signMethod)) {
//            bytes = encryptHMAC(query.toString(), secret);
//        } else {
//            bytes = encryptMD5(query.toString());
//        }
        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }

    private static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(Constants.CHARSET_UTF8), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(Constants.CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    private static byte[] encryptMD5(String data) {
        byte[] md5Byte = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = data.getBytes("UTF-8");
            md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Byte;
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toLowerCase());
        }
        return sign.toString();
    }

    /**
     * 基本参数
     */
    public static TreeMap<String, Object> getTreeCrc(TreeMap<String, Object> maps) {
        try {
            maps.put("timestamp", String.valueOf(System.currentTimeMillis()));
            String value = signTopRequest(maps, Constants.SIGN_METHOD_MD5).toUpperCase();
            Log.e("steven", value);
            maps.put("sign", value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    // 时间戳转换
    public static String timeStamp2Date(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String pattern = "yyyy-MM-dd HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    // 时间戳转换
    public static String timeStampDate(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String pattern = "yyyy-MM-dd HH:mm";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    // 时间戳转换
    public static String timeStampDate2(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String pattern = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    // 时间戳转换
    public static String timeStampDateL(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String pattern = "yyyy/MM/dd HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }


    public static RequestBody convertToRequestBody(String param) {
        return RequestBody.create(MediaType.parse("text/plain"), param);
    }


    // 检测是否有网络
    @SuppressLint("MissingPermission")
    public static boolean hasNetwork(Context mContext) {
        // 得到连接管理器对象
        if (mContext == null) {
            return true;
        }


        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI) || activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE));
    }

    // 将文件进行SHA1加密
    private static String getFileContent(File file) {
        try {
            StringBuilder sb = new StringBuilder();
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            FileInputStream fin = new FileInputStream(file);
            int len;
            byte[] buffer = new byte[1024];//设置输入流的缓存大小 字节
            //将整个文件全部读入到加密器中
            while ((len = fin.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            //对读入的数据进行加密
            byte[] bytes = digest.digest();
            for (byte b : bytes) {
                // 数byte 类型转换为无符号的整数
                int n = b & 0XFF;
                // 将整数转换为16进制
                String s = Integer.toHexString(n);
                // 如果16进制字符串是一位，那么前面补0
                if (s.length() == 1) {
                    sb.append("0").append(s);
                } else {
                    sb.append(s);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
