package cz.verbovsky.web.sg.word.repository;

import cz.verbovsky.web.sg.word.domain.Word;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Mongo repository for word entity that is extended by custom {@link WordRepositoryExt}
 *
 * @author Martin Verbovsky
 */
@Repository
public interface WordRepository extends MongoRepository<Word, String>, WordRepositoryExt {

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
     * Find all documents by the given {@code forbidden} property.
     *
     * @param forbidden
     *
     * @return all documents that fulfil {@code forbidden} property
     *
     */
    List<Word> findByForbidden(boolean forbidden);

}
