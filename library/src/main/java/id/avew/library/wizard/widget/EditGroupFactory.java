package id.avew.library.wizard.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.avew.library.wizard.R;
import id.avew.library.wizard.constant.JsonFormConstants;
import id.avew.library.wizard.expression.JsonExpressionResolver;
import id.avew.library.wizard.i18n.JsonFormBundle;
import id.avew.library.wizard.interfaces.CommonListener;
import id.avew.library.wizard.interfaces.FormWidgetFactory;

import static id.avew.library.wizard.utils.FormUtils.FONT_BOLD_PATH;
import static id.avew.library.wizard.utils.FormUtils.MATCH_PARENT;
import static id.avew.library.wizard.utils.FormUtils.WRAP_CONTENT;
import static id.avew.library.wizard.utils.FormUtils.getLayoutParams;
import static id.avew.library.wizard.utils.FormUtils.getTextViewWith;

/**
 * Created by jurkiri on 22/11/17.
 */

public class EditGroupFactory implements FormWidgetFactory {

    private static final String TAG = "EditGroupFactory";

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject parentJson, CommonListener listener, JsonFormBundle bundle, JsonExpressionResolver resolver, int visualizationMode) throws JSONException {
        List<View> viewsFromJson = new ArrayList<>();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setTag(R.id.key, parentJson.getString("key"));
        linearLayout.setTag(R.id.type, JsonFormConstants.EDIT_GROUP);



        String groupTitle = bundle.resolveKey(parentJson.optString("title"));
        if (!TextUtils.isEmpty(groupTitle)) {
            viewsFromJson.add(getTextViewWith(context, 16, groupTitle, parentJson.getString("key"),
                    parentJson.getString("type"), getLayoutParams(MATCH_PARENT, WRAP_CONTENT, 0, 0, 0, 0),
                    FONT_BOLD_PATH));
        }

        try {
            JSONArray fields = parentJson.getJSONArray(JsonFormConstants.FIELDS_FIELD_NAME);
            long optNumber = parentJson.getLong("optNumber");
            linearLayout.setWeightSum(optNumber);
            for (int i = 0; i < optNumber; i++) {
                JSONObject childJson = fields.getJSONObject(i);
                try {
                    List<View> views = WidgetFactoryRegistry.getWidgetFactory(childJson.getString("type")).getViewsFromJson(stepName, context, childJson, listener, bundle,resolver, visualizationMode);
                    for (View v : views) {
                        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                        layoutParams.setMargins(0,0,10,0);
                        v.setLayoutParams(layoutParams);
                        linearLayout.addView(v);
                    }
                } catch (Exception e) {
                    Log.e(TAG,
                            "Exception occurred in making child view at index : " + i + " : Exception is : "
                                    + e.getMessage());
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json exception occurred : " + e.getMessage());
        }
        if (linearLayout.getChildCount() > 0) {
            viewsFromJson.add(linearLayout);
        }
        return viewsFromJson;
    }
}
