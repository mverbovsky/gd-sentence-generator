package cz.verbovsky.web.sg.sentence.service;

import cz.verbovsky.web.sg.DuplicateResourceException;
import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.sentence.domain.Sentence;
import cz.verbovsky.web.sg.sentence.repository.SentenceRepository;
import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import cz.verbovsky.web.sg.word.WordTestUtils;
import cz.verbovsky.web.sg.word.service.WordService;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static cz.verbovsky.web.sg.sentence.SentenceTestUtils.createTestEntity;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.when;

/**
 * Set of test for {@link SentenceService}
 *
 * @author Martin Verbovsky
 */
@RunWith(SpringRunner.class)
public class SentenceServiceImplTest {

    @TestConfiguration
    static class SentenceServiceImplTestContextConfiguration {

        @Bean
        public SentenceService sentenceService() {
            return new SentenceServiceImpl();
        }
    }

    @Autowired
    private SentenceService sentenceService;

    @MockBean
    private SentenceRepository sentenceRepository;

    @MockBean
    private WordService wordService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldFindAll() {
        Sentence sentence = createTestEntity("1", "big", "yoda", "is");

        List<Sentence> allSentences = Arrays.asList(sentence);

        when(sentenceRepository.findAll()).thenReturn(allSentences);

        List<Sentence> foundSentences = sentenceService.findAll();

        assertNotNull(foundSentences);
        assertThat(foundSentences, hasSize(1));
        assertThat(foundSentences, IsIterableContainingInAnyOrder.containsInAnyOrder(allSentences.toArray()));
    }

    @Test
    public void shouldFindOneAndIncrementView() {
        Sentence sentence = createTestEntity("1", "big", "yoda", "is");
        int actualNumberOfViews = sentence.getNumberOfViews();

        when(sentenceRepository.findOne("1")).thenReturn(sentence);
        when(sentenceRepository.save(sentence)).thenReturn(sentence);

        Sentence foundSentence = sentenceService.findOneAndIncrementView("1");

        assertNotNull(foundSentence);
        assertThat(foundSentence, equalTo(foundSentence));
        assertThat(foundSentence.getNumberOfViews(), equalTo(actualNumberOfViews + 1));
    }

    @Test
    public void shouldNotFindByWordAndIncrementView() {
        when(sentenceRepository.findOne("1")).thenReturn(null);

        Sentence foundSentence = sentenceService.findOneAndIncrementView("1");

        assertNull(foundSentence);
    }

    @Test
    public void shouldGenerate() {
        Word noun = WordTestUtils.createTestEntity("1", "yoda", WordCategory.NOUN, false);
        Word adjective = WordTestUtils.createTestEntity("2", "big", WordCategory.ADJECTIVE, false);
        Word verb = WordTestUtils.createTestEntity("3", "is", WordCategory.VERB, false);

        when(wordService.findRandom(WordCategory.NOUN)).thenReturn(noun);
        when(wordService.findRandom(WordCategory.ADJECTIVE)).thenReturn(adjective);
        when(wordService.findRandom(WordCategory.VERB)).thenReturn(verb);

        when(sentenceRepository.findByNounAndVerbAndAdjective(noun.getWord(), verb.getWord(), adjective.getWord())).thenReturn(null);

        Sentence sentence = new Sentence();
        sentence.setId("1");
        sentence.setNoun(noun.getWord());
        sentence.setVerb(verb.getWord());
        sentence.setAdjective(adjective.getWord());
        sentence.setNumberOfViews(0);
        sentence.setCreated(Instant.now());

        when(sentenceRepository.save(refEq(sentence, "created", "id"))).thenReturn(sentence);

        Sentence genSentence = sentenceService.generate();

        assertNotNull(genSentence);
        assertNotNull(genSentence.getCreated());
        assertThat(genSentence.getNumberOfViews(), equalTo(0));
        assertThat(genSentence.getAdjective(), equalTo(adjective.getWord()));
        assertThat(genSentence.getNoun(), equalTo(noun.getWord()));
        assertThat(genSentence.getVerb(), equalTo(verb.getWord()));
    }

    @Test
    public void shouldFailGenerate_throwsResourceNotFoundException() {
        Word adjective = WordTestUtils.createTestEntity("2", "big", WordCategory.ADJECTIVE, false);
        Word verb = WordTestUtils.createTestEntity("3", "is", WordCategory.VERB, false);

        when(wordService.findRandom(WordCategory.NOUN)).thenReturn(null);
        when(wordService.findRandom(WordCategory.ADJECTIVE)).thenReturn(adjective);
        when(wordService.findRandom(WordCategory.VERB)).thenReturn(verb);

        thrown.expect(ResourceNotFoundException.class);
        thrown.expectMessage("Word category \"NOUN\" is empty.");

        sentenceService.generate();
    }

    @Test
    public void shouldFailGenerate_throwsDuplicateResourceException() {
        Word noun = WordTestUtils.createTestEntity("1", "yoda", WordCategory.NOUN, false);
        Word adjective = WordTestUtils.createTestEntity("2", "big", WordCategory.ADJECTIVE, false);
        Word verb = WordTestUtils.createTestEntity("3", "is", WordCategory.VERB, false);

        when(wordService.findRandom(WordCategory.NOUN)).thenReturn(noun);
        when(wordService.findRandom(WordCategory.ADJECTIVE)).thenReturn(adjective);
        when(wordService.findRandom(WordCategory.VERB)).thenReturn(verb);

        Sentence duplicateSentence = createTestEntity("1", adjective.getWord(), noun.getWord(), verb.getWord());

        when(sentenceRepository.findByNounAndVerbAndAdjective(noun.getWord(), verb.getWord(), adjective.getWord())).thenReturn(duplicateSentence);

        thrown.expect(DuplicateResourceException.class);
        thrown.expectMessage("Resource \"yoda is big\" already exists.");

        sentenceService.generate();
    }

}
