package cz.verbovsky.web.sg.sentence.controller;

import cz.verbovsky.web.sg.SentenceGeneratorApplication;
import cz.verbovsky.web.sg.sentence.SentenceTestUtils;
import cz.verbovsky.web.sg.sentence.domain.Sentence;
import cz.verbovsky.web.sg.word.WordTestUtils;
import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Set of integration tests for {@link SentenceController}
 *
 * @author Martin Verbovsky
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SentenceGeneratorApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SentenceControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

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
        mongoTemplate.dropCollection("WORD");
    }

    @Test
    public void shouldFindAll() throws Exception {
        mvc.perform(get("/sentences")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("sentences").isPresent())
                .andExpect(json().node("sentences").isArray())
                .andExpect(json().node("sentences").matches(hasSize(2)))
                .andExpect(json().node("sentences[0].text").isEqualTo("\"foo is nice\""))
                .andExpect(json().node("sentences[0].showDisplayCount").isEqualTo("\"1\""))
                .andExpect(json().node("sentences[1].text").isEqualTo("\"yoda is great\""))
                .andExpect(json().node("sentences[1].showDisplayCount").isEqualTo("\"1\""));
    }

    @Test
    public void shouldFindOne() throws Exception {
        mvc.perform(get("/sentences/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("sentence").isPresent())
                .andExpect(json().node("sentence").isObject())
                .andExpect(json().node("sentence.text").isEqualTo("\"foo is nice\""))
                .andExpect(json().node("sentence.showDisplayCount").isEqualTo("\"2\""));
    }

    @Test
    public void shouldFailFindOne_resourceNotFound() throws Exception {
        mvc.perform(get("/sentences/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFindOneYodaTalk() throws Exception {
        mvc.perform(get("/sentences/1/yodaTalk")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("sentence").isPresent())
                .andExpect(json().node("sentence").isObject())
                .andExpect(json().node("sentence.text").isEqualTo("\"foo nice is\""))
                .andExpect(json().node("sentence.showDisplayCount").isAbsent());
    }

    @Test
    public void shouldFailFindOneYodaTalk_resourceNotFound() throws Exception {
        mvc.perform(get("/sentences/3/yodaTalk")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGenerate() throws Exception {
        List<Word> words = new ArrayList<>(3);

        words.add(WordTestUtils.createTestEntity("1","is", WordCategory.VERB, false));
        words.add(WordTestUtils.createTestEntity("2","big", WordCategory.ADJECTIVE, false));
        words.add(WordTestUtils.createTestEntity("3","yoda", WordCategory.NOUN, false));

        mongoTemplate.insert(words, Word.class);

        mvc.perform(post("/sentences/generate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(json().node("sentence").isPresent())
                .andExpect(json().node("sentence").isObject())
                .andExpect(json().node("sentence.text").isEqualTo("\"yoda is big\""))
                .andExpect(json().node("sentence.showDisplayCount").isEqualTo("\"0\""));
    }

    @Test
    public void shouldFailGenerate_resourceNotFound() throws Exception {
        mvc.perform(post("/sentences/generate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFailGenerate_duplicateSentence() throws Exception {
        List<Word> words = new ArrayList<>(3);

        words.add(WordTestUtils.createTestEntity("1","is", WordCategory.VERB, false));
        words.add(WordTestUtils.createTestEntity("2","great", WordCategory.ADJECTIVE, false));
        words.add(WordTestUtils.createTestEntity("3","yoda", WordCategory.NOUN, false));

        mongoTemplate.insert(words, Word.class);

        mvc.perform(post("/sentences/generate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }


}
