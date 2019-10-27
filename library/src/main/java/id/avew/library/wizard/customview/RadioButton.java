package id.avew.library.wizard.customview;

import android.content.Context;
import android.util.AttributeSet;

import com.rey.material.drawable.RadioButtonDrawable;


public class RadioButton extends CompoundButton {
    public RadioButton(Context context) {
        super(context);

        internalInit(context, null, 0, 0);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        internalInit(context, attrs, 0, 0);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        internalInit(context, attrs, defStyleAttr, 0);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

        internalInit(context, attrs, defStyleAttr, defStyleRes);
    }

    private void internalInit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        internalApplyStyle(context, attrs, defStyleAttr, defStyleRes);
    }

    public void applyStyle(int resId) {
        internalApplyStyle(getContext(), null, 0, resId);
    }

    private void internalApplyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        RadioButtonDrawable drawable = new RadioButtonDrawable.Builder(context, attrs, defStyleAttr, defStyleRes)
                .build();
        drawable.setInEditMode(isInEditMode());
        drawable.setAnimEnable(false);
        setButtonDrawable(null);
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        drawable.setAnimEnable(true);
    }

    @Override
    public void toggle() {
        // we override to prevent toggle when the radio is already
        // checked (as opposed to check boxes widgets)
        if (!isChecked()) {
            super.toggle();
        }
    }

    /**
     * Change the checked state of this button immediately without showing
     * animation.
     * 
     * @param checked
     *            The checked state.
     */
    public void setCheckedImmediately(boolean checked) {
        if (mButtonDrawable instanceof RadioButtonDrawable) {
            RadioButtonDrawable drawable = (RadioButtonDrawable) mButtonDrawable;
            drawable.setAnimEnable(false);
            setChecked(checked);
            drawable.setAnimEnable(true);
        } else {
            setChecked(checked);
        }
    }
}
