package com.qy.ccm.utils.system;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * 权限工具类
 */
public class PermissionUtils {

    /**
     * 相机权限
     */
    public final static String CAMERA = Manifest.permission.CAMERA;

    /**
     * 安装apk 权限
     */
    public final static String REQUEST_INSTALL_PACKAGES = Manifest.permission.REQUEST_INSTALL_PACKAGES;

    /**
     * 电话权限
     */
    public final static String CALL_PHONE = Manifest.permission.CALL_PHONE;

    /**
     * 存储写权限
     */
    public final static String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    /**
     * 存储读权限
     */
    public final static String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;


    private static boolean state;

    /**
     * 权限判断
     */
    public static Boolean permissionJudge(Context context, String permission) {
        if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("CheckResult")
    public static Boolean getPermission(RxPermissions rxPermissions, String permission) {
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    // Always true pre-M
                    if (granted) {

                    } else {
                        // Oups permission denied
                    }
                    state = granted;
                });
        return state;
    }

}
