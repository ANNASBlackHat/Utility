package id.uniq.uniqpos.util

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import id.uniq.uniqpos.R
import id.uniq.uniqpos.data.local.sharedpref.SharedPref
import id.uniq.uniqpos.data.local.sharedpref.getJson
import id.uniq.uniqpos.data.remote.model.Employee
import id.uniq.uniqpos.data.remote.model.ServerResponse
import id.uniq.uniqpos.data.remote.model.ServerResponseList
import id.uniq.uniqpos.model.RoleMobile
import id.uniq.uniqpos.util.security.AES
import retrofit2.Call
import timber.log.Timber
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ANNASBlackHat on 13/10/2017.
 */

fun <T : Any> Call<ServerResponseList<T>>.awaitList(func: (List<T>) -> Unit): Boolean{
    val data = try{ execute() } catch (e: Exception) {
        Timber.i("xxx error $e ")
        null }
    data?.let {
        if(data.isSuccessful){
            val items = data.body()
            items?.data?.let { func(it) }
            return true
        }
    }
    return false
}

fun <T : Any> Call<ServerResponseList<T>>.awaitListBase(func: (ServerResponseList<T>) -> Unit): Boolean{
    val data = try{ execute() } catch (e: Exception) {
        Timber.i("xxx error $e ")
        null }
    data?.let {
        if(data.isSuccessful && data.code() == 200){
            data.body()?.let { func(it) }
            return true
        }
    }
    return false
}

fun <T : Any> Call<ServerResponse<T>>.await(func: (T?) -> Unit): Boolean{
    val data = try{ execute() } catch (e: Exception) {
        Timber.i("xxx error $e")
        null }
    data?.let {
        if(data.isSuccessful && data.code() == 200){
            data.body()?.let { items ->
                if(items.status) {
                    func(items.data)
                    return true
                }
            }
        }
    }
    return false
}

fun <T : Any> Call<ServerResponse<T>>.awaitBase(func: (ServerResponse<T>?) -> Unit): Boolean{
    val data = try{ execute() } catch (e: Exception) {
        Timber.i("xxx error $e")
        null }
    data?.let {
        if(data.isSuccessful){
            func(data.body())
            return true
        }
    }
    return false
}

fun Int?.toCurrency(): String{
    val num = NumberFormat.getNumberInstance()
    return try {
        num.format(this)
    }catch (e: Exception){ "0" }
}

fun String.fromCurrency(): Int{
    val num = NumberFormat.getNumberInstance()
    return try {
        num.parse(this).toInt()
    } catch (e: Exception) { 0 }
}

fun View.drawableBackground(drawable: Int){
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
        setBackgroundDrawable(ContextCompat.getDrawable(context, drawable))
    }else{
        background = ContextCompat.getDrawable(context, drawable)
    }
}

fun StringBuilder.appendnl(value: String? = " ", lineAfter: Int = 1, lineBefore: Int = 0){
    for(i in 0 until lineBefore)this.appendln()
    this.append("$value")
    for(i in 0 until lineAfter) this.appendln()
}

fun String.change(add: String?): String{
    val tmpVal = this.substring((add?.length ?: 0))
    return add + tmpVal
}

fun String.loop(w: Int): String{
    var newVal = ""
    for (i in 0 until w) newVal += this
    return newVal
}

//align left
fun String.width(w: Int, max: Int = 32): String{
    var newValue = this
    for (i in length until w){
        newValue += " "
    }
    val newLength = newValue.length
    if(newLength > w){
        val tmp = newValue.substring(w, length)
        newValue = newValue.substring(0, w)
        for(i in w until max){
            newValue += " "
        }
        newValue += "\n$tmp"
        for(i in (newLength - w) until w)newValue += " "
    }
    return newValue
}

//align right
fun String.widthRight(w: Int): String{
    val newValue = StringBuilder()
    for(i in 1 until (w - this.length)){
        newValue.append(" ")
    }
    return newValue.append(this).toString()
}

fun String.center(widht: Int, flanks: String = " "): String{
    val center = widht/2
    val flank = center - (this.length/2)
    val newValue = StringBuilder()
    for(i in 1 until flank) newValue.append(flanks)
    newValue.append(this)
    for(i in 1 until flank) newValue.append(flanks)
    return newValue.toString()
}

fun Date.dateFormat(): String{
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    return sdf.format(this)
}

fun Date.dateTimeFormat(): String{
    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
    return sdf.format(this)
}

fun Long.dateTimeFormat(format: String? = null): String{
    val sdf = SimpleDateFormat(format ?: "dd-MM-yyyy HH:mm")
    return sdf.format(Date(this))
}

fun Long.dateFormat(format: String? = null): String{
    val sdf = SimpleDateFormat(format ?: "dd-MM-yyyy")
    return sdf.format(Date(this))
}

fun Context.showMessage(msg: String?, title: String? = null, positiveAction: DialogInterface.OnClickListener? = null,
                        negativeAction: DialogInterface.OnClickListener? = null, positiveMsg: String = "OK",
                        negativeMsg: String? = "Cancel"){
    val alert = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(positiveMsg, positiveAction)
    positiveAction?.let { alert.setNegativeButton(negativeMsg, negativeAction) }
    msg?.let { alert.show() }
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT){
    if(Looper.myLooper() == Looper.getMainLooper()){
        Toast.makeText(this, msg, duration).show()
    }else{
        Handler(Looper.getMainLooper()).post { Toast.makeText(this, msg, duration).show() }
    }
}

fun Throwable.errorMessage(context: Context?) = when(this){
    is java.net.UnknownHostException -> context?.getString(R.string.no_internet)
    is java.net.SocketTimeoutException -> "Please check your internet connection!"
    else -> message
}

fun TextInputEditText.toInt(): Int{
    return try {
        text.toString().toInt()
    } catch (e: Exception) {
        0
    }
}

fun TextInputEditText.value(): String{
    return try {
        text.toString()
    } catch (e: Exception) {
        ""
    }
}

fun String?.safeToInt(): Int{
    return try {
        this?.toInt() ?: kotlin.run { 0 }
    }catch (e: Exception){ 0 }
}

fun EditText?.safeToInt(): Int{
    return try {
        this?.text?.toString()?.toInt() ?: kotlin.run { 0 }
    }catch (e: Exception){ 0 }
}

fun EditText.isEmptyValue(): Boolean{
    return text.toString().isEmpty()
}

fun EditText.simpleOnTextChanged( onChange: (String) -> Unit ){
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onChange(cs.toString())
        }
    })
}

fun TextInputEditText.liveToCurrencyAndWatch(onChange: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
            if (text.toString().trim { it <= ' ' }.isNotEmpty()) {
                removeTextChangedListener(this)
                try {
                    var strNumber = s.toString().replace(",", "")
                    strNumber = strNumber.replace(".", "")
                    val number = Integer.parseInt(strNumber)
                    val currency = NumberFormat.getInstance().format(number.toLong())
                    setText(currency)
                    setSelection(currency.length)
                } catch (e: Exception) {
                }
                addTextChangedListener(this)
            }
            onChange(text.toString())
        }
        override fun afterTextChanged(editable: Editable) {}
    })
}

fun <T : Any> Map<String, List<T>>.getKey(index: Int): String?{
    var loop = 0
    forEach {
        if(loop++ == index){
            return it.key
        }
    }
    return null
}


fun ByteArray.toHex() : String{
    val result = StringBuffer()
    val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(HEX_CHARS[firstIndex])
        result.append(HEX_CHARS[secondIndex])
    }

    return result.toString()
}

fun String.isNumeric(): Boolean{
    return matches("-?\\d+(\\.\\d+)?".toRegex())
}

fun ClosedRange<Int>.random(lastChoosen: Int = start-1): Int{
    var choosen: Int
    do {
        choosen = Random().nextInt(endInclusive - start) +  start
    }while (choosen == lastChoosen)

    return choosen
}

fun ViewPager.OnPageSelected(selected: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            selected(position)
        }
    })
}

infix fun TextInputEditText.isMatch(secondField: TextInputEditText): Boolean{
    return text.toString() == secondField.text.toString()
}

infix fun TextInputEditText.isNotMatch(secondField: TextInputEditText): Boolean{
    return text.toString() != secondField.text.toString()
}
