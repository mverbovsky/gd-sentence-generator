package cz.verbovsky.web.sg;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an attempt to insert already existing word.
 *
 * @author Martin Verbovsky
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    private final String resource;

    public DuplicateResourceException(String resource) {
        super("Resource \"" + resource + "\" already exists.");
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
