package com.qy.ccm.utils.function;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.qy.ccm.R;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.utils.Utils;

import java.math.BigDecimal;

/**
 * Description:
 * Data：2019/5/8-10:54 AM
 *
 * @author
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || "null".equals(str);
    }

    /**
     * 是否不为空
     *
     * @param o
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        if (o == null) {
            return false;
        }
        if ("".equals(FilterNull(o.toString()))) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 过滤空NULL
     *
     * @param o
     * @return
     */
    public static String FilterNull(Object o) {
        return o != null && !"null".equals(o.toString()) ? o.toString().trim() : "";
    }

    /**
     * BigDecimal四舍五入，保留8位小数
     */
    public static String bigDecimal8(BigDecimal bigDecimal) {
        BigDecimal big = bigDecimal.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros();
        return big.toPlainString();
    }

    /**
     * BigDecimal四舍五入，保留2位小数
     */
    public static String bigDecimal2(BigDecimal bigDecimal) {
        BigDecimal big = bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).stripTrailingZeros();
        return big.toPlainString();
    }

    /**
     * 转成bigDecimal
     *
     * @param decimal 要转化的值
     * @return
     */
    public static BigDecimal bigDecimal(String decimal) {
        try {
            if (StringUtils.isEmpty(decimal)) {
                return new BigDecimal(0);
            } else {
                return new BigDecimal(decimal);
            }
        } catch (Exception e) {
            return new BigDecimal(0);

        }
    }


    /**
     * bigDecimal转成String
     *
     * @param decimal 要转化的值
     */
    public static String stringBigDecimal(BigDecimal decimal) {
        if (decimal == null) {
            return "";
        } else {
            return decimal.toPlainString();
        }
    }

    /**
     * 复制
     */
    public static void isCopy(String str) {
        MyApp context = MyApp.getSingleInstance();
        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("copy_address", str);
        mClipboardManager.setPrimaryClip(clipData);
        Utils.Toast(context.getString(R.string.copysucceed));
    }

}
