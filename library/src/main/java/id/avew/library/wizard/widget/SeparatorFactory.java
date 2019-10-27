package id.avew.library.wizard.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import id.avew.library.wizard.R;
import id.avew.library.wizard.expression.JsonExpressionResolver;
import id.avew.library.wizard.i18n.JsonFormBundle;
import id.avew.library.wizard.interfaces.CommonListener;
import id.avew.library.wizard.interfaces.FormWidgetFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jurkiri on 23/11/17.
 */

public class SeparatorFactory implements FormWidgetFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject, CommonListener listener, JsonFormBundle bundle, JsonExpressionResolver resolver, int visualizationMode) throws JSONException {
        List<View> views = new ArrayList<>(1);
        View v = new View(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,10,0,10);
        layoutParams.height=2;
        v.setBackgroundColor(context.getResources().getColor(R.color.primary));

        v.setLayoutParams(layoutParams);
        views.add(v);
        return views;
    }
}
