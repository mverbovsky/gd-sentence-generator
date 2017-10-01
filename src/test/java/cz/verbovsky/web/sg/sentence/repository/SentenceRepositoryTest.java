package cz.verbovsky.web.sg.sentence.repository;

import cz.verbovsky.web.sg.sentence.SentenceTestUtils;
import cz.verbovsky.web.sg.sentence.domain.Sentence;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.*;

/**
 * Set of tests for {@link SentenceRepository}
 *
 * @author Martin Verbovsky
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataMongoTest
public class SentenceRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SentenceRepository sentenceRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private List<Sentence> expectedSentences;

    @Before
    public void before() {
        expectedSentences = SentenceTestUtils.createTestList();
        mongoTemplate.insert(expectedSentences, Sentence.class);
    }

    @After
    public void after() {
        expectedSentences = null;
        mongoTemplate.dropCollection("SENTENCE");
    }

    @Test
    public void shouldFindByNounAndVerbAndAdjective() {
        Sentence sentence = sentenceRepository.findByNounAndVerbAndAdjective("foo", "is", "nice");

        assertNotNull(sentence);
        assertThat(sentence, isIn(expectedSentences));
    }

    @Test
    public void shouldNotFindByNounAndVerbAndAdjective() {
        Sentence sentence = sentenceRepository.findByNounAndVerbAndAdjective("foo", "is", "bad");

        assertNull(sentence);
    }

}
