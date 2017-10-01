package cz.verbovsky.web.sg.word.controller;

import cz.verbovsky.web.sg.SentenceGeneratorApplication;
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

import java.util.List;

import static cz.verbovsky.web.sg.word.WordTestUtils.createTestList;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Set of integration tests for {@link WordController}
 *
 * @author Martin Verbovsky
 */

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SentenceGeneratorApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WordControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

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
    public void shouldFindAllWordsPermitted() throws Exception {
        mvc.perform(get("/words")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("words").isPresent())
                .andExpect(json().node("words").isArray())
                .andExpect(json().node("words").matches(hasSize(2)))
                .andExpect(json().node("words[0]").isEqualTo("{\"word\":\"big\",\"wordCategory\":\"ADJECTIVE\"}"))
                .andExpect(json().node("words[1]").isEqualTo("{\"word\":\"man\",\"wordCategory\":\"NOUN\"}"));
    }

    @Test
    public void shouldFindAllWordsForbidden() throws Exception {
        mvc.perform(get("/words")
                .param("forbidden", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("words").isPresent())
                .andExpect(json().node("words").isArray())
                .andExpect(json().node("words").matches(hasSize(1)))
                .andExpect(json().node("words[0]").isEqualTo("{\"word\":\"create\",\"wordCategory\":\"VERB\"}"));
    }

    @Test
    public void shouldFindRandom() throws Exception {
        mvc.perform(get("/words")
                .param("random", WordCategory.NOUN.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("word").isPresent())
                .andExpect(json().node("word.word").isPresent())
                .andExpect(json().node("word.wordCategory").isPresent());
    }

    @Test
    public void shouldFailFindRandom_resourceNotFound() throws Exception {
        mvc.perform(get("/words")
                .param("random", WordCategory.VERB.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFindWord() throws Exception {
        mvc.perform(get("/words/big")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("word").isPresent())
                .andExpect(json().node("word.word").isEqualTo("big"))
                .andExpect(json().node("word.wordCategory").isEqualTo("ADJECTIVE"));
    }

    @Test
    public void shouldFailFindWord_forbidden() throws Exception {
        mvc.perform(get("/words/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldFailFindWord_resourceNotFound() throws Exception {
        mvc.perform(get("/words/is")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddWord() throws Exception {
        mvc.perform(put("/words/is")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"word\":{\"wordCategory\":\"VERB\"}}"))
                .andExpect(status().isCreated())
                .andExpect(json().node("word").isPresent())
                .andExpect(json().node("word.word").isEqualTo("is"))
                .andExpect(json().node("word.wordCategory").isEqualTo("VERB"));
    }

    @Test
    public void shouldFailAddWord_resourceExists() throws Exception {
        mvc.perform(put("/words/big")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"word\":{\"wordCategory\":\"ADJECTIVE\"}}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldFailAddWord_forbidden() throws Exception {
        mvc.perform(put("/words/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"word\":{\"wordCategory\":\"VERB\"}}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldForbid() throws Exception {
        mvc.perform(patch("/words/big/forbid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().node("word").isPresent())
                .andExpect(json().node("word.word").isEqualTo("big"))
                .andExpect(json().node("word.wordCategory").isEqualTo("ADJECTIVE"));
    }

    @Test
    public void shouldFailForbid_resourceNotFound() throws Exception {
        mvc.perform(patch("/words/is/forbid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
