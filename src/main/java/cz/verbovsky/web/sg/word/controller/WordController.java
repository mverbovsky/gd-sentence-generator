package cz.verbovsky.web.sg.word.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.word.ForbiddenWordException;
import cz.verbovsky.web.sg.word.domain.ValidationGroup;
import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import cz.verbovsky.web.sg.ResourceView;
import cz.verbovsky.web.sg.word.resource.WordResource;
import cz.verbovsky.web.sg.word.resource.WordsResource;
import cz.verbovsky.web.sg.word.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for /words resource.
 *
 * @author Martin Verbovsky
 */
@RestController
public class WordController {

    @Autowired
    private WordService wordService;

    @JsonView(ResourceView.REST.class)
    @RequestMapping(method = RequestMethod.GET, value = "/words")
    public ResponseEntity<WordsResource> getAllWords(@RequestParam(value="forbidden", required=false) Boolean forbidden) {
        List<Word> words;
        if (forbidden == null
                || !forbidden.booleanValue()) {
            words = wordService.findAllPermitted();
        } else {
            words = wordService.findAllForbidden();
        }

        return new ResponseEntity<>(new WordsResource(words), HttpStatus.OK);
    }

    @JsonView(ResourceView.REST.class)
    @RequestMapping(method = RequestMethod.GET, value = "/words/{word}")
    public ResponseEntity<WordResource> getWord(@PathVariable("word") String word) {
        Word wordRecord = wordService.findByWord(word);

        if (wordRecord == null) {
            throw new ResourceNotFoundException("Word \"" + word + "\" does not exist.");
        }

        if (wordRecord.isForbidden()) {
            throw new ForbiddenWordException(word);
        }

        return new ResponseEntity<>(new WordResource(wordRecord), HttpStatus.OK);
    }

    @JsonView(ResourceView.REST.class)
    @RequestMapping(method = RequestMethod.GET, value = "/words", params = "random")
    public ResponseEntity<WordResource> getRandomWord(@RequestParam(value="random", required=true) WordCategory wordCategory) {
        Word randomWord = wordService.findRandom(wordCategory);

        if (randomWord == null) {
            throw new ResourceNotFoundException("Word category \"" + wordCategory + "\" is empty.");
        }

        return new ResponseEntity<>(new WordResource(randomWord), HttpStatus.OK);
    }

    @JsonView(ResourceView.REST.class)
    @RequestMapping(method = RequestMethod.PUT, value = "/words/{word}")
    public ResponseEntity<WordResource> add(@PathVariable("word") String wordText,
                                            @RequestBody @JsonView(ResourceView.REST.class) @Validated(ValidationGroup.Category.class) WordResource wordResource) {

        Word wordToInsert = new Word();
        wordToInsert.setWord(wordText);
        wordToInsert.setWordCategory(wordResource.getWord().getWordCategory());
        wordToInsert.setForbidden(false);

        Word wordDb = wordService.insert(wordToInsert);

        return new ResponseEntity<>(new WordResource(wordDb), HttpStatus.CREATED);
    }

    @JsonView(ResourceView.REST.class)
    @RequestMapping(method = RequestMethod.PATCH, value = "/words/{word}/forbid")
    public ResponseEntity<WordResource> forbidWord(@PathVariable("word") String word) {
        Word wordForbidden = wordService.forbid(word);
        return new ResponseEntity<>(new WordResource(wordForbidden), HttpStatus.OK);
    }

}
