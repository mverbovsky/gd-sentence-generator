package cz.verbovsky.web.sg.word.resource;

import cz.verbovsky.web.sg.ValidationTest;
import cz.verbovsky.web.sg.word.domain.ValidationGroup;
import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Set of unit tests for testing validations on resource {@link WordResource}
 *
 * @author Martin Verbovsky
 */
public class WordResourceValidationTest extends ValidationTest {

    @Test
    public void shouldHaveNoViolations() {
        Word word = new Word();
        word.setId("id");
        word.setWord("word");
        word.setWordCategory(WordCategory.NOUN);
        word.setForbidden(false);

        WordResource wordResource = new WordResource(word);

        Set<ConstraintViolation<WordResource>> violations
                = validator.validate(wordResource, ValidationGroup.All.class, ValidationGroup.Category.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldDetectNullWord() {
        WordResource wordResource = new WordResource(null);

        Set<ConstraintViolation<WordResource>> violations
                = validator.validate(wordResource, ValidationGroup.All.class, ValidationGroup.Category.class);

        assertEquals(violations.size(), 1);

        ConstraintViolation<WordResource> violation = violations.iterator().next();

        assertEquals("may not be null", violation.getMessage());
        assertEquals("word", violation.getPropertyPath().toString());
    }

}
