package cz.verbovsky.web.sg.sentence.domain;

/**
 * Implementation of {@link SentenceFormatter} gives sentence
 * in form of {@code NOUN ADJECTIVE VERB} - Yoda :).
 *
 * @author Martin Verbovsky
 *
 */
public class YodaSentenceFormatter implements SentenceFormatter {

    @Override
    public String format(Sentence sentence) {
        return sentence.getNoun() + " " + sentence.getAdjective() + " " + sentence.getVerb();
    }

}
