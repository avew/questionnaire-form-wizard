package id.avew.quisioner.expressions;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import id.avew.library.wizard.expression.ExternalContentResolver;
import id.avew.quisioner.util.CommonUtils;

public class AssetsContentResolver implements ExternalContentResolver {

    private static final String TAG = "AssetsContentResolver";

    private Context context;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public JSONObject loadExtenalContent(String id) {
        String jsonfile = id + ".json";
        String json = CommonUtils.loadJSONFromAsset(context, jsonfile);
        try {
            return parse(json);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing file " + jsonfile, e);
        }
        return null;
    }

    private JSONObject parse(String json) throws JSONException {
        Object value = new JSONTokener(json).nextValue();
        if (!(value instanceof JSONObject)) {
            throw new JSONException("A JSONObject was expected");
        }
        return (JSONObject) value;
    }
}
