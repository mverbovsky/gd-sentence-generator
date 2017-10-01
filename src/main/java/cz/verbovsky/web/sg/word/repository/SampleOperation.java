package cz.verbovsky.web.sg.word.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

/**
 * Implementation of {@link AggregationOperation} {@code $sample}.
 * See <a href="https://jira.spring.io/browse/DATAMONGO-1325</a>
 *
 * @author Martin Verbovsky
 */
public class SampleOperation implements AggregationOperation {

    private final int size;

    public SampleOperation(int size){
        this.size = size;
    }

    @Override
    public DBObject toDBObject(final AggregationOperationContext context){
        return new BasicDBObject("$sample", new BasicDBObject("size", size));
    }
}