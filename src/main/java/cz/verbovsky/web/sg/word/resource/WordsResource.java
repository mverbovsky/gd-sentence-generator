package cz.verbovsky.web.sg.word.resource;

import com.fasterxml.jackson.annotation.JsonView;
import cz.verbovsky.web.sg.ResourceView;
import cz.verbovsky.web.sg.word.domain.Word;

import java.util.Collection;

/**
 * Resource for collection of {@link Word} instances.
 *
 * @author Martin Verbovsky
 */
public class WordsResource {

    @JsonView(ResourceView.REST.class)
    private final Collection<Word> words;

    public WordsResource(Collection<Word> words) {
        this.words = words;
    }

    public Collection<Word> getWords() {
        return words;
    }
}
