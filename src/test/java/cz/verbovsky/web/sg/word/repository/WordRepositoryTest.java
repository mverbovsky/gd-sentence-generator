package cz.verbovsky.web.sg.word.repository;

import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static cz.verbovsky.web.sg.word.WordTestUtils.createTestList;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Set of tests for {@link WordRepository}
 *
 * @author Martin Verbovsky
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataMongoTest
public class WordRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WordRepository wordRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private List<Word> expectedList;

    @Before
    public void before() {
        expectedList = createTestList();
        mongoTemplate.insert(expectedList, Word.class);
    }

    @After
    public void after() {
        expectedList = null;
        mongoTemplate.dropCollection("WORD");
    }

    @Test
    public void shouldInsertEntity() {
        Word word = new Word();
        word.setWord("word");
        word.setWordCategory(WordCategory.NOUN);
        word.setForbidden(false);

        Word insertedWord = wordRepository.insert(word);

        assertNotNull(insertedWord);
        assertNotNull(insertedWord.getId());
        assertEquals(word.getWord(), insertedWord.getWord());
        assertEquals(word.getWordCategory(), insertedWord.getWordCategory());
        assertEquals(word.isForbidden(), insertedWord.isForbidden());
    }

    @Test
    public void shouldFindByWordEntity() {
        Word word = new Word();
        word.setWord("word");
        word.setWordCategory(WordCategory.NOUN);
        word.setForbidden(false);

        mongoTemplate.insert(word);

        Word foundWord = wordRepository.findByWord("word");

        assertNotNull(foundWord);
        assertNotNull(foundWord.getId());
        assertEquals(word.getWord(), foundWord.getWord());
        assertEquals(word.getWordCategory(), foundWord.getWordCategory());
        assertEquals(word.isForbidden(), foundWord.isForbidden());
    }

    @Test
    public void shouldNotFindByWordEntity() {
        Word word = new Word();
        word.setWord("word");
        word.setWordCategory(WordCategory.NOUN);
        word.setForbidden(false);

        mongoTemplate.insert(word);

        Word foundWord = wordRepository.findByWord("null");

        assertNull(foundWord);
    }

    @Test
    public void shouldFindAllEntities() {
        List<Word> wordList = wordRepository.findAll();

        assertNotNull(wordList);
        assertThat(wordList, hasSize(3));
        assertThat(wordList, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedList.toArray()));
    }

    @Test
    public void shouldFindByForbiddenEntities() {
        List<Word> wordList = wordRepository.findByForbidden(true);

        assertNotNull(wordList);
        assertThat(wordList, hasSize(1));
        assertThat(wordList, contains(expectedList.get(0)));
    }

    @Test
    public void shouldSaveEntity() {
        Word entity = mongoTemplate.findOne(Query.query(Criteria.where("word").is("big")), Word.class);

        entity.setWord("small");

        Word savedEntity = wordRepository.save(entity);

        assertNotNull(savedEntity);
        assertThat(savedEntity, equalTo(entity));
    }

    @Test
    public void shouldFailSaveEntity() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Entity must not be null!");
        wordRepository.save((Word)null);
    }

    @Test
    public void shouldFindRandom() {
        Word entity = wordRepository.findRandom(WordCategory.NOUN);

        assertNotNull(entity);
        assertThat(entity.getWordCategory(), is(WordCategory.NOUN));
        assertFalse(entity.isForbidden());
    }

    @Test
    public void shouldNotFindRandom() {
        Word entity = wordRepository.findRandom(WordCategory.VERB);

        assertNull(entity);
    }

    @Test
    public void shouldFailFindRandom_throwIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Word category must not be null.");

        wordRepository.findRandom(null);
    }

}
