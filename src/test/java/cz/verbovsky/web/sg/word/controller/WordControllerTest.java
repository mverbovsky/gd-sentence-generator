package cz.verbovsky.web.sg.word.controller;

import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.DuplicateResourceException;
import cz.verbovsky.web.sg.word.ForbiddenWordException;
import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import cz.verbovsky.web.sg.word.service.WordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static cz.verbovsky.web.sg.word.WordTestUtils.createTestEntity;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.*;


/**
 * Set of tests for {@link WordController}
 *
 * @author Martin Verbovsky
 */
@RunWith(SpringRunner.class)
@WebMvcTest(WordController.class)
public class WordControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WordService wordService;

    @Test
    public void shouldFindAllWordsPermitted() throws Exception {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, false);

        List<Word> allWords = Arrays.asList(wordBig);

        when(wordService.findAllPermitted()).thenReturn(allWords);

        mvc.perform(get("/words")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("words").isPresent())
                .andExpect(json().node("words").isArray())
                .andExpect(json().node("words").matches(hasSize(1)))
                .andExpect(json().node("words").isEqualTo("[{\"word\":\"big\",\"wordCategory\":\"NOUN\"}]"));
    }

    @Test
    public void shouldFindAllWordsForbidden() throws Exception {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, true);

        List<Word> allWords = Arrays.asList(wordBig);

        when(wordService.findAllForbidden()).thenReturn(allWords);

        mvc.perform(get("/words")
                .param("forbidden", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("words").isPresent())
                .andExpect(json().node("words").isArray())
                .andExpect(json().node("words").matches(hasSize(1)))
                .andExpect(json().node("words").isEqualTo("[{\"word\":\"big\",\"wordCategory\":\"NOUN\"}]"));
    }

    @Test
    public void shouldFindRandom() throws Exception {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, false);

        when(wordService.findRandom(WordCategory.NOUN)).thenReturn(wordBig);

        mvc.perform(get("/words")
                .param("random", WordCategory.NOUN.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("word").isPresent())
                .andExpect(json().node("word.word").isEqualTo("big"))
                .andExpect(json().node("word.wordCategory").isEqualTo("NOUN"));
    }

    @Test
    public void shouldFailFindRandom_resourceNotFound() throws Exception {
        when(wordService.findRandom(WordCategory.NOUN)).thenReturn(null);

        mvc.perform(get("/words")
                .param("random", WordCategory.NOUN.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFindWord() throws Exception {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, false);

        when(wordService.findByWord("big")).thenReturn(wordBig);

        mvc.perform(get("/words/big")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("word").isPresent())
                .andExpect(json().node("word.word").isEqualTo("big"))
                .andExpect(json().node("word.wordCategory").isEqualTo("NOUN"));
    }

    @Test
    public void shouldFailFindWord_forbidden() throws Exception {
        Word wordBig = createTestEntity("1", "big", WordCategory.NOUN, true);

        when(wordService.findByWord("big")).thenReturn(wordBig);

        mvc.perform(get("/words/big")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldFailFindWord_resourceNotFound() throws Exception {
        when(wordService.findByWord("big")).thenReturn(null);

        mvc.perform(get("/words/big")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddWord() throws Exception {
        Word wordBig = createTestEntity(null, "big", WordCategory.NOUN, false);

        when(wordService.insert(wordBig)).thenReturn(wordBig);

        mvc.perform(put("/words/big")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"word\":{\"wordCategory\":\"NOUN\"}}"))
                .andExpect(status().isCreated())
                .andExpect(json().node("word").isPresent())
                .andExpect(json().node("word.word").isEqualTo("big"))
                .andExpect(json().node("word.wordCategory").isEqualTo("NOUN"));
    }

    @Test
    public void shouldFailAddWord_resourceExists() throws Exception {
        Word wordBig = createTestEntity(null, "big", WordCategory.NOUN, false);

        when(wordService.insert(wordBig)).thenThrow(new DuplicateResourceException("big"));

        mvc.perform(put("/words/big")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"word\":{\"wordCategory\":\"NOUN\"}}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldFailAddWord_forbidden() throws Exception {
        Word wordBig = createTestEntity(null, "big", WordCategory.NOUN, false);

        when(wordService.insert(wordBig)).thenThrow(new ForbiddenWordException("big"));

        mvc.perform(put("/words/big")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"word\":{\"wordCategory\":\"NOUN\"}}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldForbid() throws Exception {
        Word wordBigForbidden = createTestEntity("1", "big", WordCategory.NOUN, true);

        when(wordService.forbid("big")).thenReturn(wordBigForbidden);

        mvc.perform(patch("/words/big/forbid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("word").isPresent())
                .andExpect(json().node("word.word").isEqualTo("big"))
                .andExpect(json().node("word.wordCategory").isEqualTo("NOUN"));
    }

    @Test
    public void shouldFailForbid_resourceNotFound() throws Exception {
        when(wordService.forbid("big")).thenThrow(new ResourceNotFoundException("Word \"big\" does not exist."));

        mvc.perform(patch("/words/big/forbid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
