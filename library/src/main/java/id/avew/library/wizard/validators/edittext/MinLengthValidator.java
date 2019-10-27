package id.avew.library.wizard.validators.edittext;


public class MinLengthValidator extends LengthValidator {

    public MinLengthValidator(String errorMessage, int minLength) {
        super(errorMessage, minLength, Integer.MAX_VALUE);
    }
}
