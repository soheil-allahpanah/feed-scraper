package ir.sooall.feedscraper.common.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.MalformedURLException;
import java.net.URL;

public class UriValidator implements ConstraintValidator<Uri, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            new URL(value.toString());
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}