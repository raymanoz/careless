package com.raymanoz.careless;

import com.googlecode.totallylazy.Option;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.junit.Assert.assertThat;

public class CacheFlufferTest {
    @Before
    public void createOutputDirectory() throws IOException {
        new File("out").mkdir();
    }

    @Test
    public void flufferShouldPreloadCacheIfFileExists() throws IOException {
        final File file = new File("out", "out.css");
        write(bytes("file content"), file);

        CompiledLessCache cache = CompiledLessCache.compiledLessCache();
        CacheFluffer.cacheFluffer(one(pair("file.css", file.toURI().toURL())), cache);
        assertThat(cache.get("file.css").get().css, is("file content"));
    }

    @Test
    public void itShouldIgnoreAndNotPreloadIfFileDoesNotExist() throws MalformedURLException {
        CompiledLessCache cache = CompiledLessCache.compiledLessCache();
        CacheFluffer.cacheFluffer(one(pair("file2.css", new URL("file:./out/file2.css"))), cache);
        assertThat(cache.get("file2.css"), is(Option.<CompiledLess>none()));
    }
}