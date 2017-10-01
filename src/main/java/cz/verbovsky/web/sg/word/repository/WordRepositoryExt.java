package cz.verbovsky.web.sg.word.repository;

import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;

/**
 * Interface of the repository extension for word document.
 *
 * @author Martin Verbovsky
 */
public interface WordRepositoryExt {

    /**
     * Find a random document for the given {@code wordCategory}.
     *
     * @param wordCategory must not be {@literal null}.
     *
     * @return a random document with the given {@code wordCategory} or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code wordCategory} is {@literal null}
     */
    Word findRandom(WordCategory wordCategory);

}
