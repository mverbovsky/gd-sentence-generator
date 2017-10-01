package cz.verbovsky.web.sg.word;

import cz.verbovsky.web.sg.word.domain.Word;
import cz.verbovsky.web.sg.word.domain.WordCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for testing.
 *
 * @author Martin Verbovsky
 */

public final class WordTestUtils {

    private WordTestUtils() {}

    public static Word createTestEntity(String id, String word, WordCategory wordCategory, boolean forbidden) {
        Word entity = new Word();
        entity.setId(id);
        entity.setWord(word);
        entity.setWordCategory(wordCategory);
        entity.setForbidden(forbidden);
        return entity;
    }

    public static List<Word> createTestList() {
        List<Word> expectedList = new ArrayList<>(3);

        expectedList.add(createTestEntity("1","create", WordCategory.VERB, true));
        expectedList.add(createTestEntity("2","big", WordCategory.ADJECTIVE, false));
        expectedList.add(createTestEntity("3","man", WordCategory.NOUN, false));

        return expectedList;
    }
}
