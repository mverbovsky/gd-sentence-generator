package cz.verbovsky.web.sg.sentence.service;

import cz.verbovsky.web.sg.DuplicateResourceException;
import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.sentence.domain.DefaultSentenceFormatter;
import cz.verbovsky.web.sg.sentence.domain.Sentence;
import cz.verbovsky.web.sg.sentence.repository.SentenceRepository;
import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import cz.verbovsky.web.sg.word.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Implementation of {@link SentenceService}.
 *
 * @author Martin Verbovsky
 */
@Service
public class SentenceServiceImpl implements SentenceService {

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private WordService wordService;

    @Override
    public List<Sentence> findAll() {
        return sentenceRepository.findAll();
    }

    @Override
    public Sentence findOneAndIncrementView(String id) {
        Sentence sentence = sentenceRepository.findOne(id);

        if (sentence != null) {
            sentence.setNumberOfViews(sentence.getNumberOfViews() + 1);
            sentence = sentenceRepository.save(sentence);
        }

        return sentence;
    }

    @Override
    public Sentence generate() {
        Word noun = findRandom(WordCategory.NOUN);
        Word verb = findRandom(WordCategory.VERB);
        Word adjective = findRandom(WordCategory.ADJECTIVE);

        Sentence sentence = sentenceRepository.findByNounAndVerbAndAdjective(
                noun.getWord(), verb.getWord(), adjective.getWord());

        if (sentence != null) {
            throw new DuplicateResourceException(new DefaultSentenceFormatter().format(sentence));
        }

        sentence = new Sentence();
        sentence.setNoun(noun.getWord());
        sentence.setVerb(verb.getWord());
        sentence.setAdjective(adjective.getWord());
        sentence.setNumberOfViews(0);
        sentence.setCreated(Instant.now());

        return sentenceRepository.save(sentence);
    }

    private Word findRandom(WordCategory wordCategory) {
        Word random = wordService.findRandom(wordCategory);
        if (random == null) {
            throw new ResourceNotFoundException("Word category \"" + wordCategory + "\" is empty.");
        }
        return random;
    }
}
