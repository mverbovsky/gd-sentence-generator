package cz.verbovsky.web.sg.sentence.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

/**
 * Domain object representing sentence
 *
 * @author Martin Verbovsky
 */
@Document(collection = "SENTENCE")
public class Sentence {

    @Id
    @Field("id")
    private String id;

    @Field("created")
    private Instant created;

    @Field("noun")
    private String noun;

    @Field("verb")
    private String verb;

    @Field("adjective")
    private String adjective;

    @Field("numberOfViews")
    private int numberOfViews;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public int getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(int numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getAdjective() {
        return adjective;
    }

    public void setAdjective(String adjective) {
        this.adjective = adjective;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sentence sentence = (Sentence) o;

        if (numberOfViews != sentence.numberOfViews) return false;
        if (id != null ? !id.equals(sentence.id) : sentence.id != null) return false;
        if (created != null ? !created.equals(sentence.created) : sentence.created != null) return false;
        if (noun != null ? !noun.equals(sentence.noun) : sentence.noun != null) return false;
        if (verb != null ? !verb.equals(sentence.verb) : sentence.verb != null) return false;
        return adjective != null ? adjective.equals(sentence.adjective) : sentence.adjective == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (noun != null ? noun.hashCode() : 0);
        result = 31 * result + (verb != null ? verb.hashCode() : 0);
        result = 31 * result + (adjective != null ? adjective.hashCode() : 0);
        result = 31 * result + numberOfViews;
        return result;
    }
}
