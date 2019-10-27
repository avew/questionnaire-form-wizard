package id.avew.library.wizard.validators.edittext;


public class MaxLengthValidator extends LengthValidator {

    public MaxLengthValidator(String errorMessage, int maxLength) {
        super(errorMessage, 0, maxLength);
    }
}
