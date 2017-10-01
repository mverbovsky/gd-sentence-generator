package cz.verbovsky.web.sg.word;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an attempt to insert a forbidden word.
 *
 * @author Martin Verbovsky
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ForbiddenWordException extends DataIntegrityViolationException {

    private final String word;

    public ForbiddenWordException(String word) {
        super("Word \"" + word + "\" is forbidden.");
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
