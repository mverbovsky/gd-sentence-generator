package cz.verbovsky.web.sg.sentence.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.verbovsky.web.sg.ResourceView;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectSerializer;

import java.io.IOException;

/**
 * Json component with serializer for {@link SentenceFormatter}.
 *
 * @author Martin Verbovsky
 */
@JsonComponent
public class SentenceJson {

    public static class Serializer extends JsonObjectSerializer<Sentence> {

        @Override
        protected void serializeObject(Sentence value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {

            Class<?> activeViewClass = provider.getActiveView();
            SentenceFormatter sentenceFormatter = getFormatter(activeViewClass);

            jgen.writeStringField("text", sentenceFormatter.format(value));
            if (activeViewClass != null && activeViewClass.equals(ResourceView.REST.class)) {
                jgen.writeStringField("showDisplayCount", String.valueOf(value.getNumberOfViews()));
            }
        }

        private SentenceFormatter getFormatter(Class<?> activeViewClass) {
            SentenceFormatter sentenceFormatter;
            if (activeViewClass != null && activeViewClass.equals(ResourceView.YodaTalk.class)) {
                sentenceFormatter = new YodaSentenceFormatter();
            } else {
                sentenceFormatter = new DefaultSentenceFormatter();
            }
            return sentenceFormatter;
        }

    }
}
