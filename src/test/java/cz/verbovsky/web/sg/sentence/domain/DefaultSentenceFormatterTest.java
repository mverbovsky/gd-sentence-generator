package cz.verbovsky.web.sg.sentence.domain;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Set of tests for {@link DefaultSentenceFormatter}
 *
 * @author Martin Verbovsky
 */
public class DefaultSentenceFormatterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldFormat() {
        SentenceFormatter sentenceFormatter = new DefaultSentenceFormatter();

        Sentence sentence = new Sentence();
        sentence.setNoun("foo");
        sentence.setVerb("is");
        sentence.setAdjective("nice");

        Assert.assertThat(sentenceFormatter.format(sentence), Matchers.equalTo("foo is nice"));
    }

    @Test
    public void shouldFailFormat_throwsNullPointerException() {
        SentenceFormatter sentenceFormatter = new DefaultSentenceFormatter();

        thrown.expect(NullPointerException.class);

        sentenceFormatter.format(null);
    }

}
