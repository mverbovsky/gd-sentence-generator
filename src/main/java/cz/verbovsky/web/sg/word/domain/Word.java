package cz.verbovsky.web.sg.word.domain;

import com.fasterxml.jackson.annotation.JsonView;
import cz.verbovsky.web.sg.ResourceView;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

/**
 * Domain object representing word.
 *
 * @author Martin Verbovsky
 */
@Document(collection = "WORD")
public class Word {

    @Id
    @Field("id")
    @NotBlank(groups = {ValidationGroup.All.class})
    private String id;

    @JsonView(ResourceView.REST.class)
    @Field("word")
    @NotBlank(groups = {ValidationGroup.All.class})
    private String word;

    @JsonView(ResourceView.REST.class)
    @Field("wordCategory")
    @NotNull(groups = {ValidationGroup.All.class, ValidationGroup.Category.class})
    private WordCategory wordCategory;

    @Field("forbidden")
    private boolean forbidden;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public WordCategory getWordCategory() {
        return wordCategory;
    }

    public void setWordCategory(WordCategory wordCategory) {
        this.wordCategory = wordCategory;
    }

    public boolean isForbidden() {
        return forbidden;
    }

    public void setForbidden(boolean forbidden) {
        this.forbidden = forbidden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        if (forbidden != word1.forbidden) return false;
        if (id != null ? !id.equals(word1.id) : word1.id != null) return false;
        if (word != null ? !word.equals(word1.word) : word1.word != null) return false;
        return wordCategory == word1.wordCategory;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (wordCategory != null ? wordCategory.hashCode() : 0);
        result = 31 * result + (forbidden ? 1 : 0);
        return result;
    }
}
