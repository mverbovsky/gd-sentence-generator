package cz.verbovsky.web.sg.word.service;

import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.DuplicateResourceException;
import cz.verbovsky.web.sg.word.ForbiddenWordException;
import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

/**
 * Interface for word service.
 *
 * @author Martin Verbovsky
 */
public interface WordService {

    /**
     * Find all documents
     *
     * @return all documents
     *
     */
    List<Word> findAll();

    /**
     * Find all permitted documents
     *
     * @return all permitted documents
     *
     */
    List<Word> findAllPermitted();

    /**
     * Find all forbidden documents
     *
     * @return all forbidden documents
     *
     */
    List<Word> findAllForbidden();

    /**
     * Find a document by the given {@code word}.
     *
     * @param word
     *
     * @return the document with the given word or {@literal null} if none found
     *
     */
    Word findByWord(String word);

    /**
     * Inserts the given entity.
     *
     * @param entity must not be {@literal null}.
     *
     * @return the saved entity
     *
     * @throws DuplicateResourceException if {@code word} already exists
     * @throws ForbiddenWordException if {@code word} is forbidden
     */
    Word insert(Word entity) throws DuplicateKeyException, ForbiddenWordException;

    /**
     * Find a random permitted word for the given {@code wordCategory}.
     *
     * @param wordCategory must not be {@literal null}.
     *
     * @return a random word with the given {@code wordCategory} or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code wordCategory} is {@literal null}
     */
    Word findRandom(WordCategory wordCategory);

    /**
     * Forbid given word.
     *
     * @param word
     *
     * @return the forbidden entity
     *
     * @throws ResourceNotFoundException if {@code word} does not exist
     *
     */
    Word forbid(String word);
}
