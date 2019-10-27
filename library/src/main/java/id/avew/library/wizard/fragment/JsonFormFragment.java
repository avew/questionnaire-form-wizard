package id.avew.library.wizard.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.rey.material.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import id.avew.library.wizard.R;
import id.avew.library.wizard.activity.JsonFormActivity;
import id.avew.library.wizard.customview.RadioButton;
import id.avew.library.wizard.expression.JsonExpressionResolver;
import id.avew.library.wizard.i18n.JsonFormBundle;
import id.avew.library.wizard.interfaces.CommonListener;
import id.avew.library.wizard.interfaces.JsonApi;
import id.avew.library.wizard.mvp.MvpFragment;
import id.avew.library.wizard.presenters.JsonFormFragmentPresenter;
import id.avew.library.wizard.utils.CarouselAdapter;
import id.avew.library.wizard.view.JsonFormFragmentView;
import id.avew.library.wizard.viewstate.JsonFormFragmentViewState;


public class JsonFormFragment extends MvpFragment<JsonFormFragmentPresenter, JsonFormFragmentViewState> implements
        CommonListener, JsonFormFragmentView<JsonFormFragmentViewState> {
    private static final String TAG = "JsonFormFragment";
    private LinearLayout mMainView;
    private Menu mMenu;
    private JsonApi mJsonApi;

    public void setJsonApi(JsonApi jsonApi) {
        this.mJsonApi = jsonApi;
    }

    public JsonApi getJsonApi() {
        return mJsonApi;
    }

    @Override
    public void onAttach(Context activity) {
        if (activity instanceof JsonApi) {
            mJsonApi = (JsonApi) activity;
        }
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_json_wizard, null);
        mMainView = rootView.findViewById(R.id.main_layout);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.addFormElements();
    }

    @Override
    protected JsonFormFragmentViewState createViewState() {
        return new JsonFormFragmentViewState();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mMenu = menu;
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar, menu);
        presenter.setUpToolBar();
    }

    @Override
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.onBackClick();
            return true;
        } else if (item.getItemId() == R.id.action_next) {
            presenter.onNextClick(mMainView);
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            presenter.onSaveClick(mMainView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        presenter.onClick(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDetach() {
        mJsonApi = null;
        super.onDetach();
    }

    @Override
    public void updateRelevantImageView(Bitmap bitmap, String imagePath, String currentKey) {
        int childCount = mMainView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mMainView.getChildAt(i);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                String key = (String) imageView.getTag(R.id.key);
                if (key.equals(currentKey)) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setTag(R.id.imagePath, imagePath);
                    //imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }
        }
    }

    @Override
    public void writeValue(String stepName, String key, String s) {
        try {
            mJsonApi.writeValue(stepName, key, s);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void writeValue(String stepName, String prentKey, String childObjectKey, String childKey, String value) {
        try {
            mJsonApi.writeValue(stepName, prentKey, childObjectKey, childKey, value);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public JSONObject getStep(String stepName) {
        return mJsonApi.getStep(stepName);
    }

    @Override
    public String getCurrentJsonState() {
        return mJsonApi.currentJsonState();
    }

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        JsonFormFragmentPresenter presenter = new JsonFormFragmentPresenter();
        presenter.setVisualizationMode(mJsonApi.getVisualizationMode());
        return presenter;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public CommonListener getCommonListener() {
        return this;
    }

    @Override
    public void addFormElements(List<View> views) {
        for (View view : views) {
            mMainView.addView(view);
        }
    }

    @Override
    public ActionBar getSupportActionBar() {
        return ((JsonFormActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public Toolbar getToolbar() {
        return ((JsonFormActivity) getActivity()).getToolbar();
    }

    @Override
    public void setToolbarTitleColor(int colorId) {
        getToolbar().setTitleTextColor(getContext().getResources().getColor(colorId));
    }

    @Override
    public void updateVisibilityOfNextAndSave(boolean next, boolean save) {
        mMenu.findItem(R.id.action_next).setVisible(next);
        mMenu.findItem(R.id.action_save).setVisible(save);
    }

    @Override
    public void hideKeyBoard() {
        super.hideSoftKeyboard();
    }

    @Override
    public void backClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void unCheckAllExcept(String parentKey, String childKey) {
        int childCount = mMainView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mMainView.getChildAt(i);
            if (view instanceof RadioGroup) {
                RadioGroup rg = (RadioGroup) view;
                for (int j = 0; j < rg.getChildCount(); j++) {
                    RadioButton child = (RadioButton) rg.getChildAt(j);
                    String parentKeyAtIndex = (String) child.getTag(R.id.key);
                    String childKeyAtIndex = (String) child.getTag(R.id.childKey);
                    if (parentKeyAtIndex.equals(parentKey) && !childKeyAtIndex.equals(childKey)) {
                        child.setChecked(false);
                    }
                }
            }
        }
    }

    @Override
    public String getCount() {
        return mJsonApi.getCount();
    }

    @Override
    public void finishWithResult(Intent returnIntent) {
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    @Override
    public void setUpBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void transactThis(JsonFormFragment next) {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,
                        R.anim.exit_to_right).replace(R.id.container, next)
                .addToBackStack(next.getClass().getSimpleName()).commit();
    }

    public static JsonFormFragment getFormFragment(String stepName) {
        JsonFormFragment jsonFormFragment = new JsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.onCheckedChanged(buttonView, isChecked);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemSelected(parent, view, position, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Not implementation needed
    }

    @Override
    public void onCheckedChanged(Switch view, boolean checked) {
        presenter.onSwitchOnOrOff(view, checked);
    }

    public LinearLayout getMainView() {
        return mMainView;
    }

    @Override
    public void onInitialValueSet(String parentKey, String childKey, String value) {
        // no ops
    }

    @Override
    public void onValueChange(String parentKey, String childKey, String value) {
        // no ops
    }

    @Override
    public void onVisibilityChange(String key, String o, boolean b) {
        // no ops
    }

    @Override
    public JsonFormBundle getBundle(Locale locale) {
        return mJsonApi.getBundle(locale);
    }

    @Override
    public JsonExpressionResolver getExpressionResolver() {
        return mJsonApi.getExpressionResolver();
    }

    @Override
    public void onCurrentItemChanged(@Nullable CarouselAdapter.ViewHolder viewHolder, int adapterPosition) {
        presenter.onCurrentItemChanged(viewHolder, adapterPosition);
    }

    @Override
    public void onScrollStart(@NonNull CarouselAdapter.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScrollEnd(@NonNull CarouselAdapter.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable CarouselAdapter
            .ViewHolder currentHolder, @Nullable CarouselAdapter.ViewHolder newCurrent) {

    }
}
