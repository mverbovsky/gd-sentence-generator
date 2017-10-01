package cz.verbovsky.web.sg.word.repository;

import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Implementation of the repository extension.
 *
 * @author Martin Verbovsky
 */
public class WordRepositoryImpl implements WordRepositoryExt {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Word findRandom(WordCategory wordCategory) {
        if (wordCategory == null) {
            throw new IllegalArgumentException("Word category must not be null.");
        }

        SampleOperation customSampleOperation = new SampleOperation(1);

        TypedAggregation<Word> typedAggr = Aggregation.newAggregation(Word.class,
                Aggregation.match(Criteria.where("wordCategory").is(wordCategory)),
                Aggregation.match(Criteria.where("forbidden").is(false)),
                customSampleOperation);

        AggregationResults<Word> aggregationResults = mongoTemplate.aggregate(typedAggr, Word.class);

        if (aggregationResults.getMappedResults().isEmpty()) {
            return null;
        }

        return aggregationResults.getMappedResults().get(0);
    }

}
