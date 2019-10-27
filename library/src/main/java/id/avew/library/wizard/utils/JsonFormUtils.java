package id.avew.library.wizard.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonFormUtils {

    private static final String TAG = "JsonFormUtils";

    public static JSONObject mergeFormData(JSONObject form, JSONObject dataJson)
            throws JSONException {
        JSONObject mergedForm = new JSONObject(form.toString());

        Iterator<String> keys = dataJson.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject formField = findFieldInForm(mergedForm, key);
            if (formField != null) {
                formField.put("value", dataJson.get(key));
            } else {
                Log.d(TAG, key + " NOT FOUND!");
            }
        }

        return mergedForm;
    }

    public static JSONObject extractDataFromForm(JSONObject form) throws JSONException {
        return extractDataFromForm(form, true);
    }

    public static JSONObject extractDataFromForm(JSONObject form, boolean includeBase64)
            throws JSONException {
        Map<String, Object> dataMap = new HashMap<>();

        JSONArray names = form.names();
        for (int i = 0; i < names.length(); i++) {
            String nodeName = names.get(i).toString();
            if (nodeName.contains("step")) {
                processFieldContainer((JSONObject) form.get(nodeName), dataMap, includeBase64);
            }
        }
        return new JSONObject(dataMap);
    }

    private static JSONObject findFieldInForm(JSONObject form, String key) throws JSONException {

        JSONArray names = form.names();
        for (int i = 0; i < names.length(); i++) {
            String nodeName = names.get(i).toString();
            if (nodeName.contains("step")) {
                JSONObject step = form.getJSONObject(nodeName);
                JSONObject field = findInJSON(step, key);
                if (field != null) {
                    return field;
                }
            }
        }
        return null;
    }

    private static JSONObject findInJSON(JSONObject root, String key) throws JSONException {
        JSONArray fields = root.getJSONArray("fields");
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = (JSONObject) fields.get(i);
            if (isContainer(field)) {
                field = findInJSON(field, key);
            }
            if (field != null && field.has("key") && field.getString("key").equals(key)) {
                return field;
            }
        }
        return null;
    }

    private static void processFieldContainer(JSONObject container, Map<String, Object> dataMap,
                                              boolean incluideBase64)
            throws JSONException {
        JSONArray fields = container.optJSONArray("fields");
        if (fields != null) {
            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = (JSONObject) fields.get(i);
                if (isContainer(field)) {
                    processFieldContainer(field, dataMap, incluideBase64);
                } else {
                    if (isCheckbox(field)) {
                        processCheckbox(field, dataMap);
                    } else if (isImageChooser(field) && incluideBase64) {
                        processImageChooser(field, dataMap);
                    } else if (field.has("key") && field.has("value")) {
                        dataMap.put(field.getString("key"), field.get("value"));
                    }
                }
            }
        }
    }

    private static void processImageChooser(JSONObject field, Map<String, Object> dataMap)
            throws JSONException {
        String imagePath = field.optString("value");
        if (!TextUtils.isEmpty(imagePath)) {
            String base64 = ImageFileUtils.processImageFromFile(imagePath);
            dataMap.put(field.getString("key"), field.get("value"));
            dataMap.put(field.getString("key") + "#base64", base64);
        }
    }

    private static void processCheckbox(JSONObject field, Map<String, Object> dataMap)
            throws JSONException {
        JSONArray options = field.optJSONArray("options");
        if (options != null) {
            for (int j = 0; j < options.length(); j++) {
                JSONObject option = (JSONObject) options.get(j);
                if (option.has("key") && option.has("value")) {
                    dataMap.put(option.getString("key"), option.get("value"));
                }
            }
        }
    }

    private static boolean isContainer(JSONObject field) throws JSONException {
        return "edit_group".equals(field.getString("type"));
    }

    private static boolean isCheckbox(JSONObject field) throws JSONException {
        return "check_box".equals(field.getString("type"));
    }

    private static boolean isImageChooser(JSONObject field) throws JSONException {
        return "choose_image".equals(field.getString("type"));
    }

}
