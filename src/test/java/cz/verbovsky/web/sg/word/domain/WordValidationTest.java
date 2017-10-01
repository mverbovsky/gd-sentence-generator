package cz.verbovsky.web.sg.word.domain;

import cz.verbovsky.web.sg.ValidationTest;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Set of unit tests for testing validations on domain object {@link Word}
 *
 * @author Martin Verbovsky
 */
public class WordValidationTest extends ValidationTest {

    @Test
    public void shouldHaveNoViolationsGroupAll() {
        Word word = new Word();
        word.setId("id");
        word.setWord("word");
        word.setWordCategory(WordCategory.NOUN);
        word.setForbidden(false);

        Set<ConstraintViolation<Word>> violations
                = validator.validate(word, ValidationGroup.All.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldHaveNoViolationsGroupCategory() {
        Word word = new Word();
        word.setWordCategory(WordCategory.NOUN);

        Set<ConstraintViolation<Word>> violations
                = validator.validate(word, ValidationGroup.Category.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldDetectNullId() {
        Word word = new Word();
        word.setId(null);
        word.setWord("word");
        word.setWordCategory(WordCategory.NOUN);
        word.setForbidden(false);

        Set<ConstraintViolation<Word>> violations
                = validator.validate(word, ValidationGroup.All.class);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Word> violation = violations.iterator().next();

        assertEquals("may not be empty", violation.getMessage());
        assertEquals("id", violation.getPropertyPath().toString());
    }

    @Test
    public void shouldDetectEmptyId() {
        Word word = new Word();
        word.setId("");
        word.setWord("word");
        word.setWordCategory(WordCategory.NOUN);
        word.setForbidden(false);

        Set<ConstraintViolation<Word>> violations
                = validator.validate(word, ValidationGroup.All.class);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Word> violation = violations.iterator().next();

        assertEquals("may not be empty", violation.getMessage());
        assertEquals("id", violation.getPropertyPath().toString());
    }

    @Test
    public void shouldDetectNullWord() {
        Word word = new Word();
        word.setId("id");
        word.setWord(null);
        word.setWordCategory(WordCategory.NOUN);
        word.setForbidden(false);

        Set<ConstraintViolation<Word>> violations
                = validator.validate(word, ValidationGroup.All.class);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Word> violation = violations.iterator().next();

        assertEquals("may not be empty", violation.getMessage());
        assertEquals("word", violation.getPropertyPath().toString());
    }

    @Test
    public void shouldDetectEmptyWord() {
        Word word = new Word();
        word.setId("id");
        word.setWord("");
        word.setWordCategory(WordCategory.NOUN);
        word.setForbidden(false);

        Set<ConstraintViolation<Word>> violations
                = validator.validate(word, ValidationGroup.All.class);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Word> violation = violations.iterator().next();

        assertEquals("may not be empty", violation.getMessage());
        assertEquals("word", violation.getPropertyPath().toString());
    }

    @Test
    public void shouldDetectNullCategory() {
        Word word = new Word();
        word.setId("id");
        word.setWord("word");
        word.setWordCategory(null);
        word.setForbidden(false);

        Set<ConstraintViolation<Word>> violations
                = validator.validate(word, ValidationGroup.All.class, ValidationGroup.Category.class);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Word> violation = violations.iterator().next();

        assertEquals("may not be null", violation.getMessage());
        assertEquals("wordCategory", violation.getPropertyPath().toString());
    }

}
