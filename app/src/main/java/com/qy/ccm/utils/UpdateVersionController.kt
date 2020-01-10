package com.qy.ccm.utils

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import android.view.KeyEvent
import com.qy.ccm.utils.Utils
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


public class UpdateVersionController(public val context: Context) {
    //下载进度条
    public var pd: ProgressDialog? = null

    /**
     * 步骤三：下载文件
     */
    public fun downLoadApk(appUrl: String) {
        // 进度条对话框
        pd = ProgressDialog(context)
        pd?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        pd?.setMessage("下载中...")
        pd?.setCanceledOnTouchOutside(false)
        //        pd.setProgressNumberFormat("%1d M/%2d M");
        pd?.setCancelable(false)
        // 监听返回键--防止下载的时候点击返回
        pd?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
                Utils.Toast("正在下载请稍后")
                true
            } else {
                false
            }
        }
        // Sdcard不可用
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Utils.Toast("SD卡不可用~")
        } else {
            pd?.show()
            //下载的子线程
            object : Thread() {
                override fun run() {
                    try {
                        // 在子线程中下载APK文件

                        val file = getFileFromServer(appUrl, pd)
                        //                        sleep(1000);
                        // 安装APK文件
                        //                        installApk(file);
                        openFile(file!!.absolutePath)
                        pd?.dismiss() // 结束掉进度条对话框
                    } catch (e: Exception) {
                        //                        Utils.Toast( "文件下载失败了~");
                        pd?.dismiss()
                        e.printStackTrace()
                    }

                }

            }.start()
        }
    }

    /**
     * 从服务器下载apk
     */
    @Throws(Exception::class)
    fun getFileFromServer(path: String?, pd: ProgressDialog?): File? {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val url = URL(path)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5000
            // 获取到文件的大小
            pd?.max = conn.contentLength / 1024 / 1024
            val i_s = conn.inputStream

            val file = File(Environment.getExternalStorageDirectory().path + "/Download", "blockchain_" + System.currentTimeMillis() + ".apk")
            //判断文件夹是否被创建
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            } else {
                file.delete()
            }

            val fos = FileOutputStream(file)
            val bis = BufferedInputStream(i_s)
            val buffer = ByteArray(50 * 1024)
            var total = 0

            var len = bis.read(buffer)
            while (len != -1) {
                fos.write(buffer, 0, len)
                total += len
                // 获取当前下载量
                pd?.progress = total / 1024 / 1024

                val all = (conn.contentLength / 1024 / 1024).toFloat()
                val percent = (total / 1024 / 1024).toFloat()
                pd?.setProgressNumberFormat(String.format("%.1fM/%.1fM", percent, all))

                len = bis.read(buffer)
            }
            fos.close()
            bis.close()
            i_s.close()
            return file
        } else {
            return null
        }
    }

    fun openFile(path: String) {
        //之前有部分手机不能安装
        //        Intent intent = new Intent();
        //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        intent.setAction(Intent.ACTION_VIEW);
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //            Uri uriForFile = FileProvider.getUriForFile(context, context_of_activity.getApplicationContext().getPackageName() + ".provider", file);
        //            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
        //        } else {
        //            intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        //        }

        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //            Uri fileUri = FileProvider.getUriForFile(context, getFileProviderAuthority(context_of_activity), new File(path));
            val fileUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".update", File(path))         //TODO 非常强力好用的方法！！！！仅需要在【Manifest】里面，加入：android:authorities="你的包名"+"指定后缀"。就好了。
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            intent.setDataAndType(Uri.fromFile(File(path)), "application/vnd.android.package-archive")
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Utils.Toast("没有找到打开此类文件的程序")
        }

    }

    companion object {

        fun getInstance(context: Context): UpdateVersionController {
            return UpdateVersionController(context)
        }


        /**
         * 获取版本名
         */
        fun getVerName(context: Context): String {

            var verName = ""
            try {
                // 获取packagemanager的实例
                val packageManager = context.packageManager
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                val packInfo = packageManager.getPackageInfo(
                        context.packageName, 0)

                verName = packInfo.versionName
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return verName
        }

        /**
         * 获取版本号
         */
        fun getVerCode(context: Context): Int {
            var verCode = -1
            try {
                // 获取packagemanager的实例
                val packageManager = context.packageManager
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                val packInfo = packageManager.getPackageInfo(
                        context.packageName, 0)

                verCode = packInfo.versionCode
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Utils.Toast("本地的版本号是————————>${verCode}")
            return verCode
        }

        /**
         * 获取FileProvider的auth
         */
        public fun getFileProviderAuthority(context: Context): String? {
            try {
                for (provider in context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PROVIDERS).providers) {
                    if (FileProvider::class.java.name == provider.name && provider.authority.endsWith(".update_app.file_provider")) {
                        return provider.authority
                    }
                }
            } catch (ignore: PackageManager.NameNotFoundException) {
            }

            return null
        }
    }
}
