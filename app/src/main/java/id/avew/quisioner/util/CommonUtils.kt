package id.avew.quisioner.util

import android.content.Context
import android.util.Log
import java.io.IOException
import java.nio.charset.Charset

object CommonUtils {

    private const val TAG = "CommonUtils"

    @JvmStatic
    fun loadJSONFromAsset(context: Context, fileName: String): String? {
        val json: String?
        try {
            val `is` = context.getAssets().open(fileName)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            Log.d(TAG, "Exception Occurred : " + ex.message)
            return null
        }
        return json
    }
}