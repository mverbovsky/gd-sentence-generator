package cz.verbovsky.web.sg.sentence;

import cz.verbovsky.web.sg.sentence.domain.Sentence;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for testing.
 *
 * @author Martin Verbovsky
 */
public final class SentenceTestUtils {

    private SentenceTestUtils() {}

    public static Sentence createTestEntity(String id, String adjective, String noun, String verb) {
        Sentence entity = new Sentence();
        entity.setId(id);
        entity.setNumberOfViews(1);
        entity.setCreated(Instant.now());
        entity.setAdjective(adjective);
        entity.setNoun(noun);
        entity.setVerb(verb);
        return entity;
    }

    public static List<Sentence> createTestList() {
        List<Sentence> sentences = new ArrayList<>();
        sentences.add(createTestEntity("1", "nice", "foo", "is"));
        sentences.add(createTestEntity("2", "great", "yoda", "is"));
        return sentences;
    }
}
