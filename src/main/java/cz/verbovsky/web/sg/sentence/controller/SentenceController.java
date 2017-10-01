package cz.verbovsky.web.sg.sentence.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.ResourceView;
import cz.verbovsky.web.sg.sentence.domain.Sentence;
import cz.verbovsky.web.sg.sentence.resource.SentenceResource;
import cz.verbovsky.web.sg.sentence.resource.SentencesResource;
import cz.verbovsky.web.sg.sentence.service.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for /words resource.
 *
 * @author Martin Verbovsky
 */
@RestController
public class SentenceController {

    @Autowired
    private SentenceService sentenceService;

    @JsonView(ResourceView.REST.class)
    @RequestMapping(method = RequestMethod.GET, value = "/sentences")
    public ResponseEntity<SentencesResource> getAllSentences() {
        List<Sentence> sentences = sentenceService.findAll();

        return new ResponseEntity<>(new SentencesResource(sentences), HttpStatus.OK);
    }

    @JsonView(ResourceView.REST.class)
    @RequestMapping(method = RequestMethod.GET, value = "/sentences/{id}")
    public ResponseEntity<SentenceResource> getWord(@PathVariable("id") String id) {
        Sentence sentenceRecord = sentenceService.findOneAndIncrementView(id);

        if (sentenceRecord == null) {
            throw new ResourceNotFoundException("Sentence for \"id=" + id + "\" does not exist.");
        }

        return new ResponseEntity<>(new SentenceResource(sentenceRecord), HttpStatus.OK);
    }

    @JsonView(ResourceView.YodaTalk.class)
    @RequestMapping(method = RequestMethod.GET, value = "/sentences/{id}/yodaTalk")
    public ResponseEntity<SentenceResource> getWordYodaTalk(@PathVariable("id") String id) {
        Sentence sentenceRecord = sentenceService.findOneAndIncrementView(id);

        if (sentenceRecord == null) {
            throw new ResourceNotFoundException("Sentence for \"id=" + id + "\" does not exist.");
        }

        return new ResponseEntity<>(new SentenceResource(sentenceRecord), HttpStatus.OK);
    }

    @JsonView(ResourceView.REST.class)
    @RequestMapping(method = RequestMethod.POST, value = "/sentences/generate")
    public ResponseEntity<SentenceResource> generate() {
        Sentence newSentence = sentenceService.generate();

        return new ResponseEntity<>(new SentenceResource(newSentence), HttpStatus.CREATED);
    }

}
