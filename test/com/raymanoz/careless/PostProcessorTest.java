package com.raymanoz.careless;

import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unary;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.raymanoz.careless.StubCompiler.returning;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PostProcessorTest {
    private static final String in = "div { color: #ff0000; }";

    @Test
    public void canChangeCompiledCss() {
        String out = "div { color: #ff1234; }";
        assertThat(PostProcessor.postProcessor(returning(in), sequence(replaceColor())).compile(null, null).css, is(out));
    }

    @Test
    public void noChangeMadeWhenThereAreNoPostProcessors() {
        assertThat(PostProcessor.postProcessor(returning(in), Sequences.<Unary<String>>empty()).compile(null, null).css, is(in));
    }

    @Test
    public void shouldApplyChangesInOrder() {
        String out = "div { color: #ff0000; }foobar";
        assertThat(PostProcessor.postProcessor(returning(in), sequence(addText("foo"), addText("bar"))).compile(null, null).css, is(out));
    }

    private Unary<String> replaceColor() {
        return new Unary<String>() {
            @Override
            public String call(String css) throws Exception {
                return css.replaceAll("#ff0000", "#ff1234");
            }
        };
    }

    private Unary<String> addText(final String text) {
        return new Unary<String>() {
            @Override
            public String call(String css) throws Exception {
                return css + text;
            }
        };
    }
}