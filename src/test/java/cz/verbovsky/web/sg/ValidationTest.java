package cz.verbovsky.web.sg;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Abstract class for validation unit tests that provides
 * validator instance for running validations.
 *
 *  @author Martin Verbovsky
 */
public abstract class ValidationTest {

    protected static ValidatorFactory validatorFactory;
    protected static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }
}
