package com.raymanoz.careless;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unary;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.raymanoz.careless.TestHelpers.createJar;
import static org.junit.Assert.assertThat;

public class JarURLResolverTest {
    @Test
    public void jarURLResolverResolveNestedLessFiles() throws Exception {
        String source = ".font-awesome {\n  font-family: FontAwesome;\n}\n\n";
        File jar = createJar("nested.jar", sequence(
                pair("base.less", "@import \"nested/nested.less\";"),
                pair("nested/nested.less", source)));
        CarelessCompiler compiler = CarelessCompiler.carelessCompiler(Sequences.<Pair<String, URL>>empty(), Sequences.<Unary<String>>empty());
        CompiledLess compiledLess = compiler.compile(LessSourceResolvers.byJar().apply("jar:file:" + jar.getAbsolutePath() + "!/base.less"), null);
        assertThat(compiledLess.css, is(source));
    }
}