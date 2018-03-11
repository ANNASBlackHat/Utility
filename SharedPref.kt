package id.uniq.uniqpos.data.local.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.annasblackhat.tebakwarna.BuildConfig


/**
 * Created by ANNASBlackHat on 16/09/2017.
 */

class SharedPref(context: Context) {

    private val sp: SharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    private val spe: SharedPreferences.Editor

    init {
        spe = sp.edit()
    }

    companion object {
        val TOKEN_DATA = "token"
    }

    fun putData(key: String, value: Any?) {
        spe.putString(key, value.toString())
        spe.commit()
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean  {
        return sp.getBoolean(key, default)
    }

    fun getString(key: String, default: String? = null): String? {
        return sp.getString(key, default)
    }

    fun removeAllData(){
        spe.clear()
        spe.commit()
    }

    fun clearJson(jsonKey: String){
        spe.remove(jsonKey)
        spe.commit()
    }

    fun <T: Any> saveJson(jsonKey: String, cls: T) {
        val data = Gson().toJson(cls)
        putData(jsonKey, data)
    }

    fun <T> getJson(key: String,cls: Class<T>): T?{
        val data = getString(key)
        return  Gson().fromJson(data, cls)
    }

}

fun Context.sharedPref() = SharedPref(this)

fun Context.putData(key: String, value: Any){
    val sp = SharedPref(this)
    sp.putData(key, value)
}

fun Context.getLocalDataString(key: String, default: String? = null) : String{
    val sp = SharedPref(this)
    return sp.getString(key, default) ?: ""
}

fun Context.getLocalDataBoolean(key: String, default: Boolean = false) : Boolean{
    val sp = SharedPref(this)
    return sp.getBoolean(key, default)
}

fun <T: Any> Context.putJson(key: String, cls: T){
    val sp = SharedPref(this)
    sp.saveJson(key, cls)
}

fun <T: Any> Context.getJson(key: String,cls: Class<T>): T?{
    val sp = SharedPref(this)
    return sp.getJson(key, cls)
}

fun Context.clearJson(key: String){
    val sp = SharedPref(this)
    sp.clearJson(key)
}

fun Context.clearAllData(){
    val sp = SharedPref(this)
    sp.removeAllData()
}
