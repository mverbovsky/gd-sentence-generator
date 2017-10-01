package cz.verbovsky.web.sg.word.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import cz.verbovsky.web.sg.ResourceView;
import cz.verbovsky.web.sg.word.domain.ValidationGroup;
import cz.verbovsky.web.sg.word.domain.Word;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Resource for {@link Word} that wrapped {@link Word} instance.
 *
 * @author Martin Verbovsky
 */
public class WordResource {

    @JsonView(ResourceView.REST.class)
    @NotNull(groups = {ValidationGroup.All.class, ValidationGroup.Category.class})
    @Valid
    private final Word word;

    @JsonCreator
    public WordResource(@JsonProperty("word") Word word) {
        this.word = word;
    }

    public Word getWord() {
        return word;
    }
}

