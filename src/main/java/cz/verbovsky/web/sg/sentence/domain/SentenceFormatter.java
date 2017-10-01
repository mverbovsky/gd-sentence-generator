package cz.verbovsky.web.sg.sentence.domain;

/**
 * Interface for formatting {@link Sentence}.
 *
 * @author Martin Verbovsky
 *
 */
public interface SentenceFormatter {

    /**
     * Format a sentence.
     *
     * @param sentence must not be {@literal null}.
     *
     * @return a formatted sentence
     */
    String format(Sentence sentence);

}
