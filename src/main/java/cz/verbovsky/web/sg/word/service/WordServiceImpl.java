package cz.verbovsky.web.sg.word.service;

import cz.verbovsky.web.sg.ResourceNotFoundException;
import cz.verbovsky.web.sg.DuplicateResourceException;
import cz.verbovsky.web.sg.word.ForbiddenWordException;
import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;
import cz.verbovsky.web.sg.word.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link WordService}.
 *
 * @author Martin Verbovsky
 */
@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordRepository wordRepository;

    @Override
    public List<Word> findAll() {
        return wordRepository.findAll();
    }

    @Override
    public List<Word> findAllPermitted() {
        return wordRepository.findByForbidden(false);
    }

    @Override
    public List<Word> findAllForbidden() {
        return wordRepository.findByForbidden(true);
    }

    @Override
    public Word findByWord(String word) {
        return wordRepository.findByWord(word);
    }

    @Override
    public Word insert(Word entity) throws DuplicateResourceException, ForbiddenWordException {
        Word wordInDb = wordRepository.findByWord(entity.getWord());

        if (wordInDb == null) {
            return wordRepository.insert(entity);
        } else if (wordInDb.isForbidden()) {
            throw new ForbiddenWordException(entity.getWord());
        } else {
            throw new DuplicateResourceException(entity.getWord());
        }
    }

    @Override
    public Word findRandom(WordCategory wordCategory) {
        return wordRepository.findRandom(wordCategory);
    }

    @Override
    public Word forbid(String word) {
        Word entity = wordRepository.findByWord(word);

        if (entity == null) {
            throw new ResourceNotFoundException("Word \"" + word + "\" does not exist.");
        }

        entity.setForbidden(true);
        return wordRepository.save(entity);
    }

}
