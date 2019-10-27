package id.avew.quisioner

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.avew.library.wizard.activity.JsonFormActivity
import id.avew.library.wizard.constant.JsonFormConstants
import id.avew.library.wizard.utils.JsonFormUtils
import id.avew.quisioner.util.CommonUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_start.setOnClickListener {
            val intent = Intent(this@MainActivity, JsonFormActivity::class.java)
            val json = CommonUtils.loadJSONFromAsset(applicationContext, DATA_JSON_PATH)
            intent.putExtra("json", json)
            intent.putExtra("resolver", "id.avew.quisioner.expressions.AssetsContentResolver")
            intent.putExtra(
                JsonFormConstants.ORIENTATION_EXTRA,
                JsonFormConstants.ORIENTATION_LANDSCAPE
            )
            intent.putExtra(
                JsonFormConstants.CURRENT_ORIENTATION_EXTRA,
                JsonFormConstants.ORIENTATION_LANDSCAPE
            )
            val rotation = windowManager.defaultDisplay.rotation
            intent.putExtra(JsonFormConstants.CURRENT_ORIENTATION_EXTRA, rotation)
            //intent.putExtra(JsonFormConstants.INPUT_METHOD_EXTRA, JsonFormConstants.INPUT_METHOD_HIDDEN);
            startActivityForResult(intent, REQUEST_CODE_GET_JSON)
        }
        button_start_ro.setOnClickListener {
            val intent = Intent(this@MainActivity, JsonFormActivity::class.java)
            val json = CommonUtils.loadJSONFromAsset(applicationContext, COMPLETE_JSON_PATH)
            intent.putExtra("json", json)
            intent.putExtra(
                JsonFormConstants.VISUALIZATION_MODE_EXTRA,
                JsonFormConstants.VISUALIZATION_MODE_READ_ONLY
            )
            startActivityForResult(
                intent, REQUEST_CODE_GET_JSON
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_GET_JSON && resultCode == Activity.RESULT_OK) {
            val json = data?.getStringExtra("json")
            Log.d(TAG, json)
            val result = json?.let { extractDataFromForm(it) }
            Log.d(TAG, result.toString())
        } else if ((requestCode == REQUEST_CODE_GET_JSON && resultCode == JsonFormConstants.RESULT_JSON_PARSE_ERROR)) {
            val alertDialog = AlertDialog.Builder(this@MainActivity).create()
            alertDialog.setTitle("Error")
            alertDialog.setMessage(data?.data.toString() as CharSequence)
            alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, "OK"
            ) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val REQUEST_CODE_GET_JSON = 1
        private const val TAG = "MainActivity"
        private const val DATA_JSON_PATH = "data.json"
        private const val COMPLETE_JSON_PATH = "complete.json"
        private fun extractDataFromForm(form: String): JSONObject? {
            try {
                return JsonFormUtils
                    .extractDataFromForm(JSONTokener(form).nextValue() as JSONObject)
            } catch (e: JSONException) {
                Log.e(TAG, "Error parsing JSON document", e)
            }
            return null
        }
    }
}
