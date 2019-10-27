package id.avew.library.wizard.customview;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import id.avew.library.wizard.R;
import id.avew.library.wizard.interfaces.JsonApi;

public class GenericTextWatcher implements TextWatcher {

    private static final String TAG = "GenericTextWatcher";

    private View mView;
    private String mStepName;

    public GenericTextWatcher(String stepName, View view) {
        mView = view;
        mStepName = stepName;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not implementation needed
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not implementation needed
    }

    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        JsonApi api = null;
        Context ctx = mView.getContext();
        if(ctx instanceof JsonApi) {
            api = (JsonApi) ctx;
        } else if(ctx instanceof ContextWrapper) {
            ContextWrapper contextWrapper = (ContextWrapper) ctx;
            api = (JsonApi) contextWrapper.getBaseContext();
        } else {
            throw new RuntimeException("Could not fetch context");
        }

        String key = (String) mView.getTag(R.id.key);
        try {
            api.writeValue(mStepName, key, text);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}