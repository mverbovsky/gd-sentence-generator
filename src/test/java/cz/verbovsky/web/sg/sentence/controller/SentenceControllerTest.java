package cz.verbovsky.web.sg.sentence.controller;

import cz.verbovsky.web.sg.DuplicateResourceException;
import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.sentence.domain.Sentence;
import cz.verbovsky.web.sg.sentence.SentenceTestUtils;
import cz.verbovsky.web.sg.sentence.service.SentenceService;
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

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Set of tests for {@link SentenceController}
 *
 * @author Martin Verbovsky
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SentenceController.class)
public class SentenceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SentenceService sentenceService;

    @Test
    public void shouldFindAll() throws Exception {
        Sentence sentence = SentenceTestUtils.createTestEntity("1", "big", "yoda", "is");

        List<Sentence> allSentences = Arrays.asList(sentence);

        when(sentenceService.findAll()).thenReturn(allSentences);

        mvc.perform(get("/sentences")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("sentences").isPresent())
                .andExpect(json().node("sentences").isArray())
                .andExpect(json().node("sentences").matches(hasSize(1)))
                .andExpect(json().node("sentences[0].text").isEqualTo("\"yoda is big\""))
                .andExpect(json().node("sentences[0].showDisplayCount").isEqualTo("\"1\""));
    }

    @Test
    public void shouldFindOne() throws Exception {
        Sentence sentence = SentenceTestUtils.createTestEntity("1", "big", "yoda", "is");

        when(sentenceService.findOneAndIncrementView("1")).thenReturn(sentence);

        mvc.perform(get("/sentences/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("sentence").isPresent())
                .andExpect(json().node("sentence").isObject())
                .andExpect(json().node("sentence.text").isEqualTo("\"yoda is big\""))
                .andExpect(json().node("sentence.showDisplayCount").isEqualTo("\"1\""));
    }

    @Test
    public void shouldFailFindOne_resourceNotFound() throws Exception {
        when(sentenceService.findOneAndIncrementView("1")).thenReturn(null);

        mvc.perform(get("/sentences/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFindOneYodaTalk() throws Exception {
        Sentence sentence = SentenceTestUtils.createTestEntity("1", "big", "yoda", "is");

        when(sentenceService.findOneAndIncrementView("1")).thenReturn(sentence);

        mvc.perform(get("/sentences/1/yodaTalk")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("sentence").isPresent())
                .andExpect(json().node("sentence").isObject())
                .andExpect(json().node("sentence.text").isEqualTo("\"yoda big is\""))
                .andExpect(json().node("sentence.showDisplayCount").isAbsent());
    }

    @Test
    public void shouldFailFindOneYodaTalk_resourceNotFound() throws Exception {
        when(sentenceService.findOneAndIncrementView("1")).thenReturn(null);

        mvc.perform(get("/sentences/1/yodaTalk")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGenerate() throws Exception {
        Sentence sentence = SentenceTestUtils.createTestEntity("1", "big", "yoda", "is");

        when(sentenceService.generate()).thenReturn(sentence);

        mvc.perform(post("/sentences/generate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(json().node("sentence").isPresent())
                .andExpect(json().node("sentence").isObject())
                .andExpect(json().node("sentence.text").isEqualTo("\"yoda is big\""))
                .andExpect(json().node("sentence.showDisplayCount").isEqualTo("\"1\""));
    }

    @Test
    public void shouldFailGenerate_resourceNotFound() throws Exception {
        when(sentenceService.generate()).thenThrow(new ResourceNotFoundException("Resource not found."));

        mvc.perform(post("/sentences/generate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFailGenerate_duplicateSentence() throws Exception {
        when(sentenceService.generate()).thenThrow(new DuplicateResourceException("sentence"));

        mvc.perform(post("/sentences/generate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }


}
