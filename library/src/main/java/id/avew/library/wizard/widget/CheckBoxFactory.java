package id.avew.library.wizard.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.avew.library.wizard.R;
import id.avew.library.wizard.constant.JsonFormConstants;
import id.avew.library.wizard.customview.CheckBox;
import id.avew.library.wizard.expression.JsonExpressionResolver;
import id.avew.library.wizard.i18n.JsonFormBundle;
import id.avew.library.wizard.interfaces.CommonListener;
import id.avew.library.wizard.interfaces.FormWidgetFactory;
import id.avew.library.wizard.interfaces.JsonApi;
import id.avew.library.wizard.utils.JsonFormUtils;

import static id.avew.library.wizard.utils.FormUtils.FONT_BOLD_PATH;
import static id.avew.library.wizard.utils.FormUtils.FONT_REGULAR_PATH;
import static id.avew.library.wizard.utils.FormUtils.MATCH_PARENT;
import static id.avew.library.wizard.utils.FormUtils.WRAP_CONTENT;
import static id.avew.library.wizard.utils.FormUtils.getLayoutParams;
import static id.avew.library.wizard.utils.FormUtils.getTextViewWith;


public class CheckBoxFactory implements FormWidgetFactory {

    private static final String TAG = "CheckBoxFactory";

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject,
                                       CommonListener listener, JsonFormBundle bundle, JsonExpressionResolver resolver,
                                       int visualizationMode) throws JSONException {
        List<View> views = null;
        switch (visualizationMode) {
            case JsonFormConstants.VISUALIZATION_MODE_READ_ONLY:
                views = getReadOnlyViewsFromJson(context, jsonObject, bundle, resolver);
                break;
            default:
                views = getEditableViewsFromJson(context, jsonObject, listener, bundle, resolver);
        }
        return views;
    }

    private List<View> getEditableViewsFromJson(Context context, JSONObject jsonObject,
                                                CommonListener listener, JsonFormBundle bundle, JsonExpressionResolver resolver)
            throws JSONException {
        List<View> views = new ArrayList<>(1);
        views.add(getTextViewWith(context, 16, bundle.resolveKey(jsonObject.getString("label")),
                jsonObject.getString("key"),
                jsonObject.getString("type"),
                getLayoutParams(MATCH_PARENT, WRAP_CONTENT, 0, 0, 0, 0),
                FONT_BOLD_PATH));
        JSONArray options = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
        for (int i = 0; i < options.length(); i++) {
            JSONObject item = options.getJSONObject(i);
            if (isVisible(item, context, resolver)) {
                CheckBox checkBox = (CheckBox) LayoutInflater.from(context)
                        .inflate(R.layout.item_checkbox, null);
                checkBox.setText(bundle.resolveKey(item.getString("text")));
                checkBox.setTag(R.id.key, jsonObject.getString("key"));
                checkBox.setTag(R.id.type, jsonObject.getString("type"));
                checkBox.setTag(R.id.childKey, item.getString("key"));
                checkBox.setGravity(Gravity.CENTER_VERTICAL);
                checkBox.setTextSize(16);
                checkBox.setTypeface(
                        Typeface.createFromAsset(context.getAssets(), FONT_REGULAR_PATH));
                checkBox.setOnCheckedChangeListener(listener);
                if (!TextUtils.isEmpty(item.optString("value"))) {
                    checkBox.setChecked(item.optBoolean("value"));
                }
                if (i == options.length() - 1) {
                    checkBox.setLayoutParams(
                            getLayoutParams(MATCH_PARENT, WRAP_CONTENT, 0, 0, 0, (int) context
                                    .getResources().getDimension(R.dimen.extra_bottom_margin)));
                }
                views.add(checkBox);
            }
        }
        return views;
    }

    private List<View> getReadOnlyViewsFromJson(Context context, JSONObject jsonObject,
                                                JsonFormBundle bundle, JsonExpressionResolver resolver) throws JSONException {
        List<View> views = new ArrayList<>(1);
        views.add(getTextViewWith(context, 16, bundle.resolveKey(jsonObject.getString("label")),
                jsonObject.getString("key"),
                jsonObject.getString("type"),
                getLayoutParams(MATCH_PARENT, WRAP_CONTENT, 0, 0, 0, (int) context
                        .getResources().getDimension(R.dimen.extra_bottom_margin)),
                FONT_BOLD_PATH));
        JSONArray options = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
        for (int i = 0; i < options.length(); i++) {
            JSONObject item = options.getJSONObject(i);
            if (isVisible(item, context, resolver)) {
                if (!TextUtils.isEmpty(item.optString("value")) && item.optBoolean("value")) {
                    views.add(
                            getTextViewWith(context, 16, bundle.resolveKey(item.getString("text")),
                                    item.getString("key"),
                                    null, getLayoutParams(MATCH_PARENT, WRAP_CONTENT, 0, 0, 0,
                                            (int) context
                                                    .getResources()
                                                    .getDimension(R.dimen.default_bottom_margin)),
                                    FONT_REGULAR_PATH));
                }
            }
        }
        return views;
    }

    private boolean isVisible(JSONObject jsonObject, Context context,
                              JsonExpressionResolver resolver) {

        final String showExpression = jsonObject.optString("show");
        if (TextUtils.isEmpty(showExpression)) {
            return true;
        }
        if (resolver.isValidExpression(showExpression)) {
            try {
                JSONObject currentValues = getCurrentValues(context);
                return resolver.existsExpression(showExpression, currentValues);
            } catch (JSONException e) {
                Log.e(TAG, "isVisible: Error evaluating expression " + showExpression, e);
            }
            return false;
        }

        return ("true".equalsIgnoreCase(showExpression));
    }

    @Nullable
    private JSONObject getCurrentValues(Context context) throws JSONException {
        JSONObject currentValues = null;
        if (context instanceof JsonApi) {
            String currentJsonState = ((JsonApi) context).currentJsonState();
            JSONObject currentJsonObject = new JSONObject(currentJsonState);
            currentValues = JsonFormUtils.extractDataFromForm(currentJsonObject, false);
        }
        return currentValues;
    }
}
