package id.uniq.uniqpos.util

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import id.uniq.uniqpos.data.local.entity.PendingPrintEntity
import id.uniq.uniqpos.data.local.sharedpref.SharedPref
import id.uniq.uniqpos.data.local.sharedpref.getLocalDataBoolean
import id.uniq.uniqpos.data.local.sharedpref.getLocalDataString
import id.uniq.uniqpos.model.SocketMessage
import id.uniq.uniqpos.util.printer.PrinterSocket
import timber.log.Timber
import java.net.NetworkInterface

/**
 * Created by annasblackhat on 13/02/18.
 */

fun GetIpAddress(): String{
    var ip = "not found"
    try {
        val enumNetworkInterface = NetworkInterface.getNetworkInterfaces()
        while (enumNetworkInterface.hasMoreElements()){
            val networkInterface = enumNetworkInterface.nextElement()
            val enumInetAddress = networkInterface.inetAddresses
            while (enumInetAddress.hasMoreElements()){
                val inetAddress = enumInetAddress.nextElement()

                if(inetAddress.isSiteLocalAddress){
                    ip = inetAddress.hostAddress
                }
            }
        }
    }catch (e: Exception){
       Timber.i("Get IP Address Failed. $e")
    }
    return ip
}

fun IsValidField(vararg views: View): Boolean {
    var status = true
    for (v in views) {
        if (v is TextInputEditText) {
            if (v.text.toString().isEmpty()) {
                v.error = "Field can't be blank"
                status = false
            } else {
                v.error = null
            }
        } else if (v is MaterialBetterSpinner) {
            if (v.editableText.toString().isEmpty()) {
                status = false
                v.error = "Field can't be blank"
            }
        }
    }
    return status
}

fun copyAsset(context: Context, assetFileName: String, fileOut: File): Boolean{
    var input: InputStream? = null
    var output: OutputStream? = null
    try {
        input = context.assets.open(assetFileName)
        output = FileOutputStream(fileOut)
        val buffer = ByteArray(1024)
        var read = input.read(buffer)
        while (read != -1){
            output.write(buffer, 0, read)
            read = input.read(buffer)
        }
        return true
    } catch (e: Exception) {
        println("Copy asset Error $e")
        context.toast("Error $e")
    } finally {
        input?.close()
        output?.close()
    }
    return false
}
