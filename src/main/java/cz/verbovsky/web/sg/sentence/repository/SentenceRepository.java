package cz.verbovsky.web.sg.sentence.repository;

import cz.verbovsky.web.sg.sentence.domain.Sentence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Mongo repository for sentence entity.
 *
 * @author Martin Verbovsky
 */
@Repository
public interface SentenceRepository extends MongoRepository<Sentence, String> {

    /**
     * Find a sentence with the given {@code noun}, {@code verb} and {@code adjective}.
     *
     * @param noun
     * @param verb
     * @param adjective
     *
     * @return the sentence with the given {@code noun}, {@code verb} and {@code adjective}
     */
    Sentence findByNounAndVerbAndAdjective(String noun, String verb, String adjective);
}
