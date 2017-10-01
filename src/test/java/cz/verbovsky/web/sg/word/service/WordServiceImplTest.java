package cz.verbovsky.web.sg.word.service;

import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.DuplicateResourceException;
import cz.verbovsky.web.sg.word.ForbiddenWordException;
import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import cz.verbovsky.web.sg.word.repository.WordRepository;
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

import java.util.Arrays;
import java.util.List;

import static cz.verbovsky.web.sg.word.WordTestUtils.createTestEntity;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Set of test for {@link WordService}
 *
 * @author Martin Verbovsky
 */
@RunWith(SpringRunner.class)
public class WordServiceImplTest {

    @TestConfiguration
    static class WordServiceImplTestContextConfiguration {

        @Bean
        public WordService wordService() {
            return new WordServiceImpl();
        }
    }

    @Autowired
    private WordService wordService;

    @MockBean
    private WordRepository wordRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldFindAll() {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, false);

        List<Word> allWords = Arrays.asList(wordBig);

        when(wordRepository.findAll()).thenReturn(allWords);

        List<Word> foundWords = wordService.findAll();

        assertNotNull(foundWords);
        assertThat(foundWords, hasSize(1));
        assertThat(foundWords, IsIterableContainingInAnyOrder.containsInAnyOrder(allWords.toArray()));
    }

    @Test
    public void shouldFindPermitted() {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, false);

        List<Word> allWords = Arrays.asList(wordBig);

        when(wordRepository.findByForbidden(false)).thenReturn(allWords);

        List<Word> foundWords = wordService.findAllPermitted();

        assertNotNull(foundWords);
        assertThat(foundWords, hasSize(1));
        assertThat(foundWords, IsIterableContainingInAnyOrder.containsInAnyOrder(allWords.toArray()));
    }

    @Test
    public void shouldFindForbidden() {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, true);

        List<Word> allWords = Arrays.asList(wordBig);

        when(wordRepository.findByForbidden(true)).thenReturn(allWords);

        List<Word> foundWords = wordService.findAllForbidden();

        assertNotNull(foundWords);
        assertThat(foundWords, hasSize(1));
        assertThat(foundWords, IsIterableContainingInAnyOrder.containsInAnyOrder(allWords.toArray()));
    }

    @Test
    public void shouldFindByWord() {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, true);

        when(wordRepository.findByWord("big")).thenReturn(wordBig);

        Word foundWord = wordService.findByWord("big");

        assertNotNull(foundWord);
        assertThat(foundWord, equalTo(wordBig));
    }

    @Test
    public void shouldNotFindByWord() {
        when(wordRepository.findByWord("bigger")).thenReturn(null);

        Word foundWord = wordService.findByWord("bigger");

        assertNull(foundWord);
    }

    @Test
    public void shouldInsert() {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, false);
        when(wordRepository.findByWord("big")).thenReturn(null);
        when(wordRepository.insert(wordBig)).thenReturn(wordBig);

        Word insertedWord = wordService.insert(wordBig);
        assertNotNull(insertedWord);
        assertThat(insertedWord, equalTo(wordBig));
    }

    @Test
    public void shouldFailInsert_throwsDuplicateWordException() {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, false);
        when(wordRepository.findByWord("big")).thenReturn(wordBig);
        when(wordRepository.insert(wordBig)).thenReturn(wordBig);

        thrown.expect(DuplicateResourceException.class);
        thrown.expectMessage("Resource \"big\" already exists.");

        wordService.insert(wordBig);
    }

    @Test
    public void shouldFailInsert_throwsForbiddenWordException() {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, true);
        when(wordRepository.findByWord("big")).thenReturn(wordBig);
        when(wordRepository.insert(wordBig)).thenReturn(wordBig);

        thrown.expect(ForbiddenWordException.class);
        thrown.expectMessage("Word \"big\" is forbidden.");

        wordService.insert(wordBig);
    }

    @Test
    public void shouldFindRandom() {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, false);
        when(wordRepository.findRandom(WordCategory.NOUN)).thenReturn(wordBig);

        Word randomWord = wordService.findRandom(WordCategory.NOUN);
        assertNotNull(randomWord);
        assertThat(randomWord, equalTo(wordBig));
    }

    @Test
    public void shouldNotFindRandom() {
        when(wordRepository.findRandom(WordCategory.NOUN)).thenReturn(null);

        Word randomWord = wordService.findRandom(WordCategory.NOUN);
        assertNull(randomWord);
    }

    @Test
    public void shouldFailFindRandom_throwsIllegalArgumentException() {
        when(wordRepository.findRandom(null)).thenThrow(new IllegalArgumentException("Word category must not be null."));

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Word category must not be null.");

        wordService.findRandom(null);
    }

    @Test
    public void shouldForbid() {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, false);
        when(wordRepository.findByWord("big")).thenReturn(wordBig);

        Word wordBigForbidden = createTestEntity(wordBig.getId(), wordBig.getWord(), wordBig.getWordCategory(), true);
        when(wordRepository.save(wordBigForbidden)).thenReturn(wordBigForbidden);

        Word forbiddenWord = wordService.forbid("big");
        assertNotNull(forbiddenWord);
        assertThat(forbiddenWord, equalTo(wordBigForbidden));
    }

    @Test
    public void shouldFailForbid_throwsResourceNotFoundException() {
        when(wordRepository.findByWord("big")).thenReturn(null);

        thrown.expect(ResourceNotFoundException.class);
        thrown.expectMessage("Word \"big\" does not exist.");

        wordService.forbid("big");
    }

}
