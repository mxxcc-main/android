package com.qy.ccm.utils

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.github.jdsjlzx.recyclerview.LRecyclerView
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qy.ccm.utils.Utils
import okhttp3.Request
import okio.Buffer
import java.io.*
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.regex.Pattern

/**
 * Created by Administrator on 2018/5/16.
 */


//String字符串，如果为null，或长度为0，则都视为Empty
fun String.ext_isNull_or_Length0(): Boolean {
    if (this == null) {
        return true
    }
    if (this.length == 0) {
        return true
    }
    return false
}

//String字符串，直接转化为Gson对象
fun <T> String.ext_transToGsonObj(clazz: Class<T>? = null): T {
    if (clazz == null) {
        return Gson().fromJson(this, object : TypeToken<T>() {}.type)
        //TODO，这里，这个吊诡的protect构造方法的构造方式，真是吊诡。而且，这到底是不是一个匿名内部类？
    } else {
        return Gson().fromJson(this, clazz)
    }
}

//Gson对象，转化为String字符串
fun Any.ext_transGsonObj_toString(): String {
    return Gson().toJson(this)
}

//显示标准的Yes/No的dialog。
fun Activity.ext_show_standard_Yes_No_Dialog(title: String, msg: String, yes_callback: (DialogInterface) -> Unit, no_callback: (DialogInterface) -> Unit, yes_text: String = "确定", no_text: String = "取消") {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(msg)

    //确定方法
    builder.setPositiveButton(yes_text) { dialog: DialogInterface, which: Int ->
        yes_callback.invoke(dialog)
    }

    //取消方法
    builder.setNegativeButton(no_text) { dialog: DialogInterface, which: Int ->
        no_callback.invoke(dialog)
    }

    builder.show()

}


//在当前已指定视图焦点ViewFocus的情况下，隐藏软件盘
fun Any.ext_standardHideSoftKeyBoard(inputMethodMgr: InputMethodManager, currentFocusView: View?) {
    inputMethodMgr.hideSoftInputFromWindow(currentFocusView?.windowToken, 2)              //这里，编号2，是什么意思？
}

//利用系统原生机制，申请动态权限
fun Any.ext_standardCheckDynamicPermissions(activity: Activity, permissions: Array<String>) {
    // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // 检查该权限是否已经获取
        val check_result = ContextCompat.checkSelfPermission(activity, permissions[0])
        // 权限是否已经 授权 GRANTED---授权  DENIED---拒绝
        if (check_result != PackageManager.PERMISSION_GRANTED) {
            // 如果没有授予该权限，就去提示用户请求
            ActivityCompat.requestPermissions(activity, permissions, 321)
        }
    }
}


//快速调整Activity的Window的透明度
fun Activity.ext_setBackgroudAlpha(activity: Activity, alpha_volume: Float) {
    val lp = activity.window.attributes
    lp.alpha = alpha_volume                                                                         //从0.0到1.0的透明度

    activity.window.attributes = lp                                                                 //设置Activity之Window，透明度
}

//将一个View所展示的内容，全部截图下来，并保存在本地。
fun View.ext_saveScreenCropSnap(): Bitmap {

    //TODO 上面这个方法，有两个缺点：一、截屏时，会抖动。二、截屏，只能截取一部分ImageView。
//    this.measure(
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//    )
//    this.layout(0, 0, this.measuredWidth, this.measuredHeight)
//    this.isDrawingCacheEnabled = true
//    this.buildDrawingCache()
//    val bitmap = this.drawingCache
//    return bitmap


    //TODO 下面这个方法，效果完美。一、截屏无抖动。二、截屏能够截取目标View的所有内容。三、截屏时，还会有一个恰到好处的轻微闪烁效果。      参考资料：https://blog.csdn.net/Billy_Zuo/article/details/71077681的第三种。
    this.isDrawingCacheEnabled = true
    this.buildDrawingCache()

    if (Build.VERSION.SDK_INT >= 11) {
        this.measure(
                View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(this.height, View.MeasureSpec.EXACTLY)
        )
        this.layout(this.x.toInt(), this.y.toInt(), (this.x + this.measuredWidth).toInt(), (this.y + this.measuredHeight).toInt())
    } else {
        this.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        this.layout(0, 0, this.measuredWidth, this.measuredHeight)
    }

    val bitmap = Bitmap.createBitmap(this.drawingCache, 0, 0, this.measuredWidth, this.measuredHeight)
    this.isDrawingCacheEnabled = false
    this.destroyDrawingCache()
    return bitmap

}

//将一张Bitmap，以文件的形式，保存在本地。//TODO 并且，在【系统相册】中展示。
fun Bitmap.ext_saveToLocalDirectory(context: Context, son_dir: String = "Pictures/Screenshots", desc_text: String = "InviteCode"): String {
    //TODO 方法确实很有效，建议把目标路径，定为系统截图的目录之下。（方便用户查找）
    //TODO 参考资料：https://blog.csdn.net/z1246300949/article/details/50681435


    // 首先保存图片
    val appDir = File(Environment.getExternalStorageDirectory(), son_dir)                           //指定保存到  /sdcard/BlockChain目录之下。
    if (!appDir.exists()) {
        appDir.mkdir()
    }
    val fileName = "${System.currentTimeMillis()}.jpg"                                              //保存命名。
    val file = File(appDir, fileName)                                                               //文件的最终命名。

    try {
        val fos = FileOutputStream(file)                                                                    //以指定文件的最终路径，初始化【文件流】。
        this.compress(Bitmap.CompressFormat.JPEG, 100, fos)                                 //压缩且生成【文件流】。
        fos.flush()
        fos.close()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // 其次把文件插入到系统图库
    try {
        MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath, fileName, desc_text)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    // 最后通知图库更新
    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://${file.absolutePath}")))

    //返回一个图片的绝对路径，方便显示给用户。
    return file.absolutePath
}

//在Activity内部，发起【拍照图片】的功能。
fun Activity.ext_startCamera(camera_request_code: Int): String {                                    //TODO 注意：此处为了兼容性的完美方案考虑。最后的图片URI不在onActivityResult里进行获取；而是另开一个集合，收录图片保存路径的集合。
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    val app_name = packageInfo.packageName
    val dir_path = "${Environment.getExternalStorageDirectory().path}/${app_name}/camera/"          //获取应用名称，并用该名称，来命名将要创建的文件夹
    val camera_save_path = "${dir_path}${System.currentTimeMillis()}.png"                           //设置拍摄完成后，图片的保存路径

    //检查SD卡是否已挂载。
    val sd_mount_state = Environment.getExternalStorageState()
    if (sd_mount_state.equals(Environment.MEDIA_MOUNTED)) {
        //TODO 1.检查，指定存放的文件夹，是否存在？
        File(dir_path).let {
            if (false == it.exists()) {
                it.mkdirs()
            }
        }

        //TODO 2.指定开启系统相机的Action（并设置拍摄的保存路径。）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0版本及以上

            //新建特殊化的URI。
            val content_values = ContentValues(1)
            content_values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            content_values.put(MediaStore.Images.Media.DATA, camera_save_path)
            val mCameraTempUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content_values)

            //新建特殊化的Intent。
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            if (mCameraTempUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri)
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
            }
            startActivityForResult(intent, camera_request_code)                 //开始进入拍摄过程。

        } else {
            //7.0版本以下
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE                     //指定Action：相机拍摄
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(camera_save_path)))                       //1.把文件地址转换成Uri格式。2.且，指定成片保存路径。
            startActivityForResult(intent, camera_request_code)                 //开始进入拍摄过程。
        }

    } else {
        Utils.Toast("请确认已经插入SD卡")
    }

    return camera_save_path     //TODO 返回图片将来保存的路径。
}

//在Activity内部，发起【从相册选取】的功能。
fun Activity.ext_startAlbum(album_request_code: Int) {
    val intent: Intent
//    if (Build.VERSION.SDK_INT < 19) {
//        intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)                                                         //指定Action：
//    } else {
//        intent = Intent(Intent.ACTION_OPEN_DOCUMENT)                                                //指定Action：
//    }
    intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)                                  //TODO 此处曾遇到一个严重BUG（和版本有关。见这里：https://blog.csdn.net/u010694658/article/details/52131937）。现在已经修复。
    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")            //指定文件范围：外部存储的image图片

    startActivityForResult(intent, album_request_code)                                              //开始进入相册。
}

//从相册或相机返回的Uri，解析出绝对地址。
fun Uri?.ext_getAbsolutePath(context: Context): String? {
    if (null == this) {                                 //允许Uri的空值调用？
        return null
    }
    val scheme = this.scheme
    var path_Str = ""

    if (scheme == null)
        path_Str = this.path.toString()        //没有协议的情况。
    else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
        path_Str = this.path.toString()        //File协议的情况。
    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
        //Content协议的情况。
        val cursor = context.contentResolver.query(
                this, arrayOf(MediaStore.Images.ImageColumns.DATA),
                null, null, null)
        if (null != cursor && cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            if (index > -1) {
                path_Str = cursor.getString(index)
            }
            cursor.close()
        }
    }
    return path_Str
}

//获取手机系统的剪贴板内容。
fun Activity.ext_getText_fromClipboard(): CharSequence {
    //TODO 进行增强（考虑到HTML标签混入的情况）。参考资料：https://www.jianshu.com/p/b78e6697e15f
    try {
        //从剪贴板得到基本数据。
        val mClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = mClipboardManager.primaryClip
        //获取基本数据中的文本内容。
        val item = mClipData?.getItemAt(0)
        val clip_address_text = item!!.text
        return clip_address_text.trim()
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

//对TextView，进行富文本的转换文字颜色操作。
fun TextView.ext_multipleText_TransToRed(plain_text: String) {
    val spannable = Spannable.Factory.getInstance().newSpannable(plain_text)                                    //新建Span文本内容
    val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#FF0000"))                                  //新建Span标签容器
    spannable.setSpan(foregroundColorSpan, 0, plain_text.indexOf(" "), Spanned.SPAN_INCLUSIVE_INCLUSIVE)            //将文本内容，填充进标签容器。
    this.text = spannable                                                                                                   //为TextView赋予颜色。
}

//View补间动画
//Object属性动画
//图片帧动画


/**
 * 处理因为数字过大过复杂，而引起的：
 * ① 问题一：科学计数法表示，5.5E10
 * ② 问题二：小数点后保留位数表示。
 * ③
 */

//TODO 注意，TextWatcher方面，检查的问题，和【解构科学计数法 + 小数精度选择】这块的问题，并不是同一个问题。（TextWatcher，涉及更多的当前文本的截取问题。）

//【Double】处理科学计数法。（同时处理了小数点后位数的问题。）
fun Double.ext_allScientificNotation_to_formatDouble(need_digits_crop: Boolean = false, remaining_digits: Int = 9, round_mode: Int = BigDecimal.ROUND_FLOOR): String {
    //TODO 此处，Double要想正常地显示，不变成科学计数法：只有用String来表示。
    //TODO 不然，随便是BigDecimal或重新返回Double，都会又变回科学计数法。

    if (false == need_digits_crop) {            //如果不需要处理保留小数位数
        return BigDecimal(this.toString()).toPlainString()//TODO 用String转化，可以减少精度的丢失。
    }

    //TODO 常规部分。（老版本备份。）
//    var patterm_str = "#."
//    repeat(remaining_digits, {
//        patterm_str += "0"                                      //小数点后有几位，就在小数点后加几个零。
//    })
//    if (patterm_str == "#.") {
//        patterm_str = "#.0"             //考虑repeat语法糖函数没有执行的情况。
//    }

    //TODO 新增去尾的部分。
//    var patterm_str = "#."                                                              //TODO 此处，做手动的去尾判断。
//    repeat(
//            remaining_digits + 1,                                   //TODO 手动新增一位。
//            {
//                patterm_str += "0"                                      //小数点后有几位，就在小数点后加几个零。
//            })
//
//    if (patterm_str.length >= 3 + 1) {                                   //TODO 手动新增一位。
//        patterm_str = patterm_str.substring(0, patterm_str.length - 1)          //TODO 此处，采取截掉尾巴。
//    }
//
//    if (patterm_str.equals("#.")) {
//        patterm_str = "#.0"             //考虑repeat语法糖函数没有执行的情况。
//    }


    //TODO 一个非常坑的地方：此处，如果用最终结果的String，再放入BigDecimal，然后取值toString，则又会变成有【科学计数法】的形式。非常讨厌！
//    return DecimalFormat(patterm_str).format(this)//TODO 1.先用DecimalFormat进行小数点后的格式化。2.后续可以拿返回值，用String放入BigDecimal转化，可以减少精度的丢失。


    //TODO 替换掉以上的方式，有一种更好的方式。

    return BigDecimal(this.toString()).setScale(remaining_digits, round_mode).toPlainString()              //如此，一句话就可以了？？
}


//【String】处理科学计数法。（同时处理了小数点后位数的问题。）
fun String.ext_allScientificNotation_to_formatDouble(need_digits_crop: Boolean = false, remaining_digits: Int = 9, round_mode: Int = BigDecimal.ROUND_FLOOR): String {
//    if (this.trim().isEmpty()) {
//        return BigDecimal(0)
//    }
//
//    if (false == need_digits_crop) {            //如果不需要处理保留小数位数
//        return BigDecimal(this)//TODO 用String转化，可以减少精度的丢失。
//    } else {
//        var patterm_str = "#."
//        repeat(remaining_digits, {
//            patterm_str += "0"                                      //小数点后有几位，就在小数点后加几个零。
//        })
//        if (patterm_str == "#.") {
//            patterm_str = "#.0"             //考虑repeat语法糖函数没有执行的情况。
//        }
//
//        return BigDecimal(DecimalFormat(patterm_str).format(this.toDouble()))//TODO 1.先用DecimalFormat进行小数点后的格式化。2.用String转化，可以减少精度的丢失。
//    }

    //TODO 发现有Bug。后来选择全部使用Double的方法，来进行处理
    if (this.ext_isPureNumber_orDecimal()) {
        //TODO 全面换用新方法。
        return BigDecimal(this).setScale(remaining_digits, round_mode).toPlainString()              //如此，一句话就可以了？？
        //return this.toDouble().ext_allScientificNotation_to_formatDouble(need_digits_crop, remaining_digits)
    } else {
        return ""
    }

}

//【String】科学计数法。去掉小数点后多余的零。
fun String.ext_removeZero_andDot(): String {
    var result_str = this
    if (result_str.indexOf(".") > 0) {
        result_str = Pattern.compile("0+?$").matcher(result_str).replaceAll("").trim()//去掉多余的0
        result_str = Pattern.compile("[.]$").matcher(result_str).replaceAll("").trim()//如最后一位是.则去掉
    }
    return result_str
}

//【String】，判断一个字符串，是否是数字。
fun String.ext_isPureNumber_orDecimal(): Boolean {
    val reg_num = "^[0-9]+(.[0-9]+)?$".toRegex()                                                //判断是否是数字。
    //TODO 完美做法。
    val reg_science = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$".toRegex()                         //判断
    return this.matches(reg_num) || this.matches(reg_science)
}

//【String】，进行身份证号码的隐藏。
fun String.ext_MaskIdCard_Num(front: Int, end: Int): String {
    //身份证不能为空
    if (this.isEmpty()) {
        return ""
    }
    //需要截取的长度不能大于身份证号长度
    if ((front + end) > this.length) {
        return ""
    }
    //需要截取的不能小于0
    if (front < 0 || end < 0) {
        return ""
    }
    //计算*的数量
    val asteriskCount = this.length - (front + end)
    val asteriskStr = StringBuffer()
    var i = 0
    for (i in 0..(asteriskCount - 1)) {
        asteriskStr.append("*")
    }
    val regex = "(\\w{${front}})(\\w+)(\\w{$end})"
    return Pattern.compile(regex).matcher(this).replaceAll("$1$asteriskStr$3").trim()       //TODO 进行正则的匹配。然后去替换掉身份证的显示。

}

//TODO 这里，非常需要一个Double转【无科学计数法】【精度完美】【无空白的零】的方法。
//TODO 这里，非常需要一个Double转【无科学计数法】【精度完美】【无空白的零】的方法。
//TODO 这里，非常需要一个Double转【无科学计数法】【精度完美】【无空白的零】的方法。
//TODO 这里，非常需要一个Double转【无科学计数法】【精度完美】【无空白的零】的方法。
//TODO 这里，非常需要一个Double转【无科学计数法】【精度完美】【无空白的零】的方法。
//TODO 这里，非常需要一个Double转【无科学计数法】【精度完美】【无空白的零】的方法。
//TODO 这里，非常需要一个Double转【无科学计数法】【精度完美】【无空白的零】的方法。
//TODO 这里，非常需要一个Double转【无科学计数法】【精度完美】【无空白的零】的方法。

fun Double.ext_getRawDoubleValue(): String {
    return BigDecimal(this.toString()).toPlainString().ext_removeZero_andDot()
}

fun String.ext_getRawDoubleValue(): String {
    return BigDecimal(this).toPlainString().ext_removeZero_andDot()
}


//使用Double，切取精度。

//使用String，切取精度。


//对图片，进行指定大小的压缩。（反复压缩，直到指定的体积大小。）
//参考资料：最高效方式，https://www.jianshu.com/p/e9e1db845c21。

//快速实现方式：以下第一种。
fun Bitmap.ext_compressBitmap(target_mb_size: Int): Bitmap? {

    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    var options = 90

    while (baos.toByteArray().size / 1024 > target_mb_size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        baos.reset() // 重置baos即清空baos
        this.compress(Bitmap.CompressFormat.JPEG, options, baos)// 这里压缩options%，把压缩后的数据存放到baos中
        options -= 10// 每次都减少10
    }

    val isBm = ByteArrayInputStream(baos.toByteArray())// 把压缩后的数据baos存放到ByteArrayInputStream中
    val final_bitmap = BitmapFactory.decodeStream(isBm, null, null)// 把ByteArrayInputStream数据生成图片
    return final_bitmap
}

//所有的TextView，过滤科学计数法。
fun TextView.ext_setPlainText_filterScienceExpression(str_value: String) {
    if (str_value.ext_isPureNumber_orDecimal()) {   //是常规小数，或者科学计数法小数的话。
        val plain_str = str_value.ext_allScientificNotation_to_formatDouble()
        this.text = plain_str                   //转化后，赋值给TextView。
    } else {                                    //是其它文本的话。
        this.text = str_value
    }
}

//所有的EditText，过滤科学计数法。
fun EditText.ext_setPlainText_filterScienceExpression(str_value: String) {
    if (str_value.ext_isPureNumber_orDecimal()) {   //是常规小数，或者科学计数法小数的话。
        val plain_str = str_value.ext_allScientificNotation_to_formatDouble()
        this.setText(plain_str)                     //转化后，赋值给EditText。
    } else {                                    //是其它文本的话。
        this.setText(str_value)
    }
}


//TODO 需要加入，RecyclerView的快速适配器初始化。
fun RecyclerView.ext_InitAdapter() {

}

//TODO 需要加入，ListView的快速适配器初始化。
fun ListView.ext_InitAdapter() {

}

//TODO 此处，最好使用和火币一样的K线图框架。


//还原精度的完美计算。
fun Any.ext_testCalculation() {
    val num_1 = BigDecimal("3.0")
    val num_2 = BigDecimal("2.0")

    //乘法
    num_1.multiply(num_2)           //如此，就可以实现精确计算乘法。（小数位必定有限）

    //除法
    num_1.divide(num_2, 99, RoundingMode.FLOOR)         //除法，保留99位小数（存在无限循环取不尽的情况）。取舍运算：不管尾部是多少，去掉尾部。
    num_1.divide(num_2, 99, RoundingMode.HALF_UP)       //除法，保留99位小数（存在无限循环取不尽的情况）。取舍运算：四舍五入（最标准的常规取精度）。

    //加法
    num_1.add(num_2)                //如此，一般精度是两者中最长的那个的精度。

    //减法
    num_1.subtract(num_2)           //如此，不考虑正负的情况下，一般精度是两者中最长的那个的精度。

    //截取小数位数。
    num_1.setScale(99, RoundingMode.FLOOR)              //保留99位小数。多出位数的取舍运算：不管尾部是多少，去掉尾部。
    num_2.setScale(99, RoundingMode.HALF_UP)            //保留99位小数。多出位数的取舍运算：四舍五入（最标准的常规取精度）。

    //还原精度的完美输出。
    num_1.toString()                                            //会以【科学计数法】的形式返回String。不推荐使用这种形式。
    num_2.toEngineeringString()                                 //会以【工程计数法】的形式返回String。十的指数，始终保持3，6，9的3之倍数的形式。不推荐使用这种形式。
    num_1.toPlainString()                                       //会以【数字完全展开】的原始形式返回String。最完美的就是这种形式。无条件推荐。


}

fun String.ext_FineMultiply(second_number: String): String {        //TODO 推荐以String字符串的形式，进行计算。
    val num_1 = BigDecimal(this)
    val num_2 = BigDecimal(second_number)

    val result = num_1.multiply(num_2)

    return result.toPlainString()
}

fun String.ext_FineDivide(second_number: String, remain_scale: Int, round_mode: RoundingMode = RoundingMode.FLOOR): String {        //TODO 推荐以String字符串的形式，进行计算。
    val num_1 = BigDecimal(this)
    val num_2 = BigDecimal(second_number)

    val result = num_1.divide(num_2, remain_scale, round_mode)

    return result.toPlainString()
}

fun String.ext_FineAdd(second_number: String): String {        //TODO 推荐以String字符串的形式，进行计算。
    val num_1 = BigDecimal(this)
    val num_2 = BigDecimal(second_number)

    val result = num_1.add(num_2, MathContext.DECIMAL128)

    return result.toPlainString()
}

fun String.ext_FineSubtract(second_number: String): String {        //TODO 推荐以String字符串的形式，进行计算。
    val num_1 = BigDecimal(this)
    val num_2 = BigDecimal(second_number)

    val result = num_1.subtract(num_2)

    return result.toPlainString()
}

fun String.ext_FineScale(scale_num: Int = 8, round_mode: RoundingMode = RoundingMode.FLOOR): String {        //TODO 推荐以String字符串的形式，进行计算。
    val num_1 = BigDecimal(this)

    val result = num_1.setScale(scale_num, round_mode)

    return result.toPlainString()
}

//TODO 再写一个下拉刷新的【LRecyclerView】，快速初始化。
fun <VH : RecyclerView.ViewHolder> Context.ext_newLRecyclerView_RefreshAdapter(inner_adapter: RecyclerView.Adapter<VH>, binding_lrecycler_view: LRecyclerView,
                                                                               pull_refresh_action: () -> Unit,
                                                                               drag_load_more: () -> Unit
): LRecyclerViewAdapter {
    val refresh_adapter = LRecyclerViewAdapter(inner_adapter)

    //开始绑定到【View】视图。
    binding_lrecycler_view.adapter = refresh_adapter

    binding_lrecycler_view.setPullRefreshEnabled(true)      //下拉刷新全部
    binding_lrecycler_view.setOnRefreshListener {
        pull_refresh_action.invoke()
    }

    binding_lrecycler_view.setLoadMoreEnabled(true)         //上滑加载更多
    binding_lrecycler_view.setOnLoadMoreListener {
        drag_load_more.invoke()
    }

    return refresh_adapter
}


////TODO 新加载一个Fragment。
//TODO 全部，统一放到懒加载中，去做。
//fun AppCompatActivity.ext_addNewFragment(new_add_fragment: Fragment, container_view_id: Int = R.id.forget_password_fragment, hide_fragment_list: List<Fragment> = listOf(), need_back_from_stack: Boolean = false) {
//
//    val transaction = this.supportFragmentManager.beginTransaction()
//
//    transaction?.add(container_view_id, new_add_fragment)       //新加
//    transaction?.show(new_add_fragment)                         //显示
//
//    hide_fragment_list.forEach {
//        if (it != new_add_fragment) {
//            transaction.hide(it)                                    //隐藏其它
//        }
//    }
//    if (need_back_from_stack) {
//        transaction?.addToBackStack(null)                   //加入回退栈。                   //TODO 并且，如果按照默认模式来的话，返回键，就会从栈顶一直清空到栈底的。（默认的【返回操作】）。（当然，你也可以自己在onBackPress里面玩些花样）
//    }
//    transaction?.commit()//最终提交。
//}


//TODO 从列表中，随机选取一个元素，返回
fun <E> List<E>.ext_return_A_randElement(): E? {
    if (this.size > 0) {                    //TODO 当当前列表，存在元素的情况下。
        val random_ratio = Math.random() + 0.5          //代表范围。以前的时候，需要手动加上0.5，从0-0.9999999，增加到了0.5-1.4999999。//TODO 现如今，Kotlin内置已经提供了这一【四舍五入】方法。
        val random_index = (this.size * random_ratio).toInt()             //算出——>四舍五入后的索引数值。//TODO 可以直接考虑，使用roundToInt()。
        return this[random_index]
    } else {
        return null
    }
}


//TODO 【逐帧动画】
fun Any.ext_createNew_EveryFrameAnimation(drawable_img_list: List<Int>, each_frame_duration: Int, context: Context, target_view_Or_imageView: View): AnimationDrawable {
    //TODO 用多张静态图片，创建一个【逐帧图片】。
    val frameAnimDrawable = AnimationDrawable()
    drawable_img_list.forEach {
        frameAnimDrawable.addFrame(context.resources.getDrawable(it), each_frame_duration)          //逐帧地加入图片。
    }
    frameAnimDrawable.isOneShot = false                                                             //图片是应该播放一次结束，还是永久重复播放。//TODO 此处设置【不是】一次播放结束。

    target_view_Or_imageView.setBackgroundDrawable(frameAnimDrawable)                                       //以背景图片的形式，传入一个View（或者ImageView）。

    //frameAnimDrawable.start()             //开始播放【逐帧动画】
    //frameAnimDrawable.stop()              //停止播放【逐帧动画】
    return frameAnimDrawable                                            //返回的这个对象，可以控制【View】中，【逐帧动画】的播放和停止。
}

//TODO 【很差的补间动画】
fun View.ext_Bad_TweenBujianAnimation(
        translate_anim: TranslateAnimation = TranslateAnimation(0F, 1000F, 0F, 1000F),//TODO 【位移动画】，X起点，X位移距离，Y起点，Y位移距离。
        scale_anim: ScaleAnimation = ScaleAnimation(1F, 2F, 1F, 2F, 0.5f, 0.5f),//TODO 【缩放动画】，X轴起始倍数大小，X轴最终倍数大小，Y轴起始倍数大小，Y轴最终倍数大小,缩放参考系的X轴位置，缩放参考系的Y轴位置。比如这里，就是横向（X轴）扩大两倍，纵向（Y轴）扩大两倍，以宽度的一半高度的一半为缩放参考自的中心点。
        rotate_anim: RotateAnimation = RotateAnimation(0F, 180F, 0.5f, 0.5f),//TODO 【旋转动画】，动画起始角度，动画结束角度。参考系中心点X轴位置，参考系中心点Y轴位置。
        alpha_anim: AlphaAnimation = AlphaAnimation(1F, 0F),//TODO 【不透明度动画】。其实【Alpha】是【不透明】的意思。动画起始不透明度，动画结束不透明度。不透明为1，则完全不透明；不透明为0，则完全透明。
        animation_duration: Long,//动画的持续时间
        stop_in_endPoint: Boolean,//动画播放结束后，是否停留在【动画末尾位置】
        stop_in_beginPoint: Boolean,//动画播放结束后，是否停留在【动画开始的位置】
        animation_interpolator: Interpolator? = listOf(//设置动画的【曲线变化插值器】//TODO 在以下所有【曲线变化插值器】中，随机选择一种。
                AccelerateInterpolator(), DecelerateInterpolator(), AccelerateDecelerateInterpolator(),     //增速插值器，减速插值器，先增速后减速插值器
                AnticipateInterpolator(), OvershootInterpolator(), AnticipateOvershootInterpolator(),       //向下跳绳插值器，向上跳绳插值器，先向下后向上跳绳插值器
                BounceInterpolator(), CycleInterpolator(5F)).ext_return_A_randElement(),            //倒置的小球弹跳插值器，正弦波状起伏插值器（构造参数，代表重复正弦完整周期次数）。
        repeat_time: Int,//设置动画的重复次数//TODO 注意，设置0的话，执行1次；设置1的话，执行1次；设置<0小于0，执行无限次。
        repeat_mode: Int = TranslateAnimation.RESTART,//设置动画在重复播放时的模式。Reverse是到末尾时，倒序回放。Restart是从头开始又播放一遍。
        anim_start_delay: Long,//设置当【开启动画】后，原样【延迟】多久时间后，开始正式【动画播放】。
        z_height_adjust_mode: Int = Animation.ZORDER_TOP, //【动画内容运行】时，在Z轴上的位置。top高/bottom低/normal中。
        callback_animation_start: (Animation?) -> Unit,//动画开始时，执行【开始回调】。
        callback_animation_end_finish: (Animation?) -> Unit,//动画结束时，执行【结束回调】。
        callback_animation_on_repeating: (Animation?) -> Unit//动画重复播放时，执行【重复中回调】。
) {
    arrayListOf(
            translate_anim,
            scale_anim,
            rotate_anim,
            alpha_anim
    ).forEach {
        it.duration = animation_duration
        it.fillAfter = stop_in_endPoint
        it.fillBefore = stop_in_beginPoint
        it.interpolator = animation_interpolator
        it.repeatCount = repeat_time
        it.repeatMode = repeat_mode
        it.startOffset = anim_start_delay
        it.zAdjustment = z_height_adjust_mode
        it.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                callback_animation_start.invoke(animation)
            }

            override fun onAnimationEnd(animation: Animation?) {
                callback_animation_end_finish.invoke(animation)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                callback_animation_on_repeating.invoke(animation)
            }
        })
    }

    //TODO 平移动画。

    //TODO 缩放动画。
    //TODO 旋转动画。
    //TODO 透明度动画。

    //TODO AnimationSet，其实是一种经过了混合的单个Animation。（效果经过了混合。）
    val my_animation_set = AnimationSet(false)                                    //TODO 关闭默认的统一默认【曲线变化插值器】。使用每个【Animation动画】所自带的【插值器】。
    my_animation_set.addAnimation(translate_anim)
    my_animation_set.addAnimation(scale_anim)
    my_animation_set.addAnimation(rotate_anim)
    my_animation_set.addAnimation(alpha_anim)

    this.startAnimation(my_animation_set)                                       //开启所有的动画吧！！！
}


//TODO 【取而代之，更好更规范的属性动画】
fun Any.ext_Good_PropertyShuXingAnimation_ValueAnimator(target_view: View, target_any_object: Any) {

    val value_animator = ValueAnimator.ofInt(0, 3)//① 创建动画实例。② 为动画实例，设置多个【平滑过渡阶段】

    //TODO 上面，采用的是一种【整型估值器】的型式。
    //TODO 与此同时，还有一种【非Int，是Double类型】的ValueAnimator.ofFloat的【浮点型估值器】的型式。
    //TODO 第三种，此处，有一种更加宽泛的ValueAnimator.ofObject的【对象型估值器】的型式。


    value_animator.duration = 1000
    value_animator.startDelay = 500
    value_animator.repeatCount = 10
    value_animator.repeatMode = ValueAnimator.RESTART

    value_animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(animation: ValueAnimator?) {
            val current_changing_value = animation?.animatedValue                           //动画过渡到这一步，所处的值【状态值】。
            Utils.Toast("【过渡阶段】当下的状态值为：$current_changing_value")
            target_view.tag = current_changing_value                                                //测试代码。
            target_view.requestLayout()                     //当当前【View】的视图变得过期时，重新【绘制View】。
        }
    })

    value_animator.start()      //

}


//TODO 【傻瓜化的简单化版ViewPropertyAnimator】，完成一些基本的功能。
fun View.ext_Noob_viewPropertyAnimator() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.animate().alpha(0.5f).translationX(100F).scaleX(10F).x(360F).y(100F).z(5F).setDuration(5000).setStartDelay(500).setInterpolator(LinearInterpolator()).setUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                //当动画更新时。
            }
        }).start()       //TODO 此处，建议始终显式地调用view.animate().start()方法，播放动画。
    }
}

@SuppressLint("ObjectAnimatorBinding")      //TODO 此处，临时这样处理。
//TODO 快速的【ViewGroup】动画（包括【ListView、RecyclerView】），可以快速应用于ViewGroup。
//TODO BUG预警：很奇怪，不能在【RecyclerView】下使用，使用必报错。
fun ViewGroup.ext_quick_ViewGroupAnimation() {


//    val mLayoutTransition = LayoutTransition()
//
//    //设置每个动画持续的时间
//    mLayoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 500)
//    mLayoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 500)
//    mLayoutTransition.setStagger(LayoutTransition.APPEARING, 500)
//    mLayoutTransition.setStagger(LayoutTransition.DISAPPEARING, 500)
//
//    val appearingScaleX = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1.0f)
//    val appearingScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1.0f)
//    val appearingAlpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
//    val mAnimatorAppearing = ObjectAnimator.ofPropertyValuesHolder(this, appearingAlpha, appearingScaleX, appearingScaleY)
//
//    //为LayoutTransition设置动画及动画类型
//    mLayoutTransition.setAnimator(LayoutTransition.APPEARING, mAnimatorAppearing)
//
//    val disappearingAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
//    val disappearingRotationY = PropertyValuesHolder.ofFloat("rotationY", 0.0f, 90.0f);
//    val mAnimatorDisappearing = ObjectAnimator.ofPropertyValuesHolder(this, disappearingAlpha, disappearingRotationY);
//
//    //为LayoutTransition设置动画及动画类型
//    mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, mAnimatorDisappearing)
//
//
//    val mAnimatorChangeDisappearing = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f)
//    //为LayoutTransition设置动画及动画类型
//    mLayoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, mAnimatorChangeDisappearing);
//
//    val mAnimatorChangeAppearing = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f)
//    //为LayoutTransition设置动画及动画类型
//    mLayoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, mAnimatorChangeAppearing)
//
//
//    this.layoutTransition = mLayoutTransition


}

//TODO 该方法无效？？？
//fun RecyclerView.ext_quick_RecyclerViewAnimation() {
//    val defaultItemAnimator = DefaultItemAnimator()
//    defaultItemAnimator.addDuration = 1000
//    defaultItemAnimator.removeDuration = 1000
//    this.itemAnimator = defaultItemAnimator
//}


//TODO 简单的【View】淡入淡出动画特效
fun View.ext_simpleVisibleAnimation() {
//    var mHideAnimation = new AlphaAnimation (1.0f, 0.0f);
//    mHideAnimation.setDuration(duration);
//    mHideAnimation.setFillAfter(true);
//    view.startAnimation(mHideAnimation);
}

//TODO 真正可用的，【View】的View.Visible和View.Gone的动画切换。
fun View.ext_showOrHiddenAnimation(visible_state: Int = View.VISIBLE, anim_duaration: Long, translate_anim: TranslateAnimation? = null) {
    if (this.visibility == visible_state) {
        return                  //TODO 如果此处，是重复了当下的状态，则不作任何改变。
    }

    var start = 0f
    var end = 0f
    if (visible_state == View.VISIBLE) {
        end = 1f
        this.visibility = View.VISIBLE
    } else
        if (visible_state == View.GONE) {
            start = 1f
            this.visibility = View.GONE
        }
    val alpha_animation = AlphaAnimation(start, end)
    alpha_animation.duration = anim_duaration
    alpha_animation.fillAfter = true
    alpha_animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            //TODO 此处，开启高度渐变的动画。
            this@ext_showOrHiddenAnimation.layoutParams
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            this@ext_showOrHiddenAnimation.clearAnimation()
        }

    })
    this.animation = alpha_animation
    alpha_animation.start()

}

//TODO 动态地设置View的高度的动画。
fun View.ext_dynamicSet_viewHeight_animation(target_height: Int, duration: Long) {
    val anim = ValueAnimator.ofInt(this.layoutParams.height, target_height)
    anim.interpolator = LinearInterpolator()        //TODO 新增一个【曲线插值器】。
    //TODO 开始动态属性监听
    anim.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as Int
        val layoutParams = this.layoutParams            //每次View的布局属性，都要通过这种方式获取
        layoutParams.height = value                                                 //修改高度
        this.layoutParams = layoutParams
    }
    anim.duration = duration
    anim.start()
}


//TODO 仅需要提前准备，两个，【ViewPager】和【IndicatorBar】两个XML【控件】。


//TODO 为【ViewPager】，预备几种【动画特效】的模式。

//TODO 为【DialogFragment】，添加特殊的启动方法。（？）
//（创建，设置数据，show显示）


//打印Request的Body。
fun Request.ext_RequestBody_toString(): String {
    try {
        val copy = this.newBuilder().build()
        val buffer = Buffer()
        copy.body()?.writeTo(buffer)
        return buffer.readUtf8()
    } catch (e: Exception) {
        return "我的body trans , did not work"
    }
}