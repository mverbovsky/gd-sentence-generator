package cz.verbovsky.web.sg.sentence.resource;

import com.fasterxml.jackson.annotation.JsonView;
import cz.verbovsky.web.sg.ResourceView;
import cz.verbovsky.web.sg.sentence.domain.Sentence;

import java.util.Collection;

/**
 * Resource for collection of {@link Sentence} instances.
 *
 * @author Martin Verbovsky
 */
public class SentencesResource {

    @JsonView({ResourceView.REST.class, ResourceView.YodaTalk.class})
    private final Collection<Sentence> sentences;

    public SentencesResource(Collection<Sentence> sentences) {
        this.sentences = sentences;
    }

    public Collection<Sentence> getSentences() {
        return sentences;
    }
}
