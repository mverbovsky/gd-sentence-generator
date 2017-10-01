package cz.verbovsky.web.sg.sentence.domain;

/**
 * Implementation of {@link SentenceFormatter} gives sentence
 * in form of {@code NOUN VERB ADJECTIVE}.
 *
 * @author Martin Verbovsky
 *
 */
public class DefaultSentenceFormatter implements SentenceFormatter {

    @Override
    public String format(Sentence sentence) {
        return sentence.getNoun() + " " + sentence.getVerb() + " " + sentence.getAdjective();
    }

}
