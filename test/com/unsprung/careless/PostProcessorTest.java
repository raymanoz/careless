package com.unsprung.careless;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unary;
import org.junit.Test;
import org.lesscss.LessSource;

import java.util.Date;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.unsprung.careless.CompiledLess.compiledLess;
import static com.unsprung.careless.PostProcessor.postProcessor;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PostProcessorTest {
/*

  it should "make changes in order" in {
    val out =
      """div {
        |  color: #ff0000;
        |}foobar""".stripMargin

    assert(new PostProcessingLessCompiler(returning(in), addText("foo") _ :: addText("bar") _ :: Nil).compile(null, null).css === out)
  }

  private def replaceColor(source: String): String = source.replaceAll("#ff0000", "#ff1234")

  private def addText(text: String)(source: String): String = source + text

  private def returning(result: String) = new StubCompiler(result)
  private class StubCompiler(result: String) extends LessCompiler {
    override def compile(lessSource: LessSource, name: String): CompiledLess = CompiledLess("name", result, new Date())
  }

 */

    private static final String in = "div { color: #ff0000; }";

    @Test
    public void canChangeCompiledCss() {
        String out = "div { color: #ff1234; }";
        assertThat(postProcessor(returning(in), sequence(replaceColor())).compile(null, null).css, is(out));
    }

    @Test
    public void noChangeMadeWhenThereAreNoPostProcessors() {
        assertThat(postProcessor(returning(in), Sequences.<Unary<String>>empty()).compile(null, null).css, is(in));
    }

    @Test
    public void shouldApplyChangesInOrder() {
        String out = "div { color: #ff0000; }foobar";
        assertThat(postProcessor(returning(in), sequence(addText("foo"), addText("bar"))).compile(null, null).css, is(out));
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

    private StubCompiler returning(String css) { return new StubCompiler(css); }
    private static class StubCompiler implements LessCompiler {
        private String css;

        private StubCompiler(String css) {
            this.css = css;
        }

        @Override
        public CompiledLess compile(LessSource lessSource, String name) {
            return compiledLess(name, css, new Date());
        }
    }
}