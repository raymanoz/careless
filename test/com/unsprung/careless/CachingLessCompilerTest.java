package com.unsprung.careless;

import com.googlecode.totallylazy.Option;
import org.junit.Before;
import org.junit.Test;
import org.lesscss.LessSource;

import java.io.File;
import java.io.IOException;

import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.time.Dates.MAX_VALUE;
import static com.unsprung.careless.CompiledLess.compiledLess;
import static com.unsprung.careless.StubCompiler.returning;
import static org.junit.Assert.assertThat;

public class CachingLessCompilerTest {
    private final CompiledLessCache cache = CompiledLessCache.compiledLessCache();
    private final String less = "// I'm a less file";
    private final File lessFile = new File("out", "test.less");
    private final String name = "foo";

    @Before
    public void createOutputDirectory() throws IOException {
        new File("out").mkdir();
    }

    @Test
    public void theCachingCompilerShouldCacheCompiledCss() throws IOException {
        write(bytes(less), lessFile);

        assertThat(cache.get(name), is(Option.<CompiledLess>none()));

        final StubCompiler stubCompiler = returning("bar");
        CachingLessCompiler.cachingLessCompiler(stubCompiler, cache).compile(new LessSource(lessFile), name);
        assertThat(cache.get(name), is(some(stubCompiler.compiledResult)));
    }

    @Test
    public void itShouldNotCacheOldFiles() throws IOException {
        write(bytes(less), lessFile);

        CompiledLess reallyNewCompiledLess = compiledLess(name, "some css", MAX_VALUE);
        cache.put(reallyNewCompiledLess.name, reallyNewCompiledLess);

        final StubCompiler stubCompiler = returning("bar");
        CachingLessCompiler.cachingLessCompiler(stubCompiler, cache).compile(new LessSource(lessFile), reallyNewCompiledLess.name);
        assertThat(cache.get(reallyNewCompiledLess.name), is(some(reallyNewCompiledLess)));
    }
}