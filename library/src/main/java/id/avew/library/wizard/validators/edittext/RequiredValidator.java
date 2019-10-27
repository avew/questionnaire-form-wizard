package id.avew.library.wizard.validators.edittext;

import com.rengwuxian.materialedittext.validation.METValidator;


public class RequiredValidator extends METValidator {

    public RequiredValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public boolean isValid(CharSequence charSequence, boolean isEmpty) {
        return !isEmpty;
    }
}
