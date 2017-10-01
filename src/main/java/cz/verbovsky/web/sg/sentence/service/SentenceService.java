package cz.verbovsky.web.sg.sentence.service;

import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.sentence.domain.Sentence;

import java.util.List;

/**
 * Interface for sentence service.
 *
 * @author Martin Verbovsky
 */
public interface SentenceService {

    /**
     * Find all sentences
     *
     * @return all sentences
     *
     */
    List<Sentence> findAll();

    /**
     * Find a sentence by the given {@code id} and increment number of views.
     *
     * @param id
     *
     * @return the document with the given sentence or {@literal null} if none found
     *
     */
    Sentence findOneAndIncrementView(String id);

    /**
     * Generate new sentence.
     *
     * @return generated sentence
     *
     * @throws ResourceNotFoundException
     */
    Sentence generate();

}
