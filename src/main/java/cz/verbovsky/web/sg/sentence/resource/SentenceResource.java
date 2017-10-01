package cz.verbovsky.web.sg.sentence.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import cz.verbovsky.web.sg.ResourceView;
import cz.verbovsky.web.sg.sentence.domain.Sentence;

/**
 * Resource for {@link Sentence} that wrapped {@link Sentence} instance.
 *
 * @author Martin Verbovsky
 */
public class SentenceResource {

    @JsonView({ResourceView.REST.class, ResourceView.YodaTalk.class})
    private final Sentence sentence;

    @JsonCreator
    public SentenceResource(@JsonProperty("sentence") Sentence sentence) {
        this.sentence = sentence;
    }

    public Sentence getSentence() {
        return sentence;
    }

}
