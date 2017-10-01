package cz.verbovsky.web.sg.word.domain;

/**
 * Interface for validation groups.
 *
 * @author Martin Verbovsky
 */
public interface ValidationGroup {

    /**
     * Validation group that groups all validations
     */
    interface All {}

    /**
     * Validation group only for category validation.
     */
    interface Category {}
}
