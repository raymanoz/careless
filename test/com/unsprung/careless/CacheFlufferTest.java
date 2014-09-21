package com.unsprung.careless;

import com.googlecode.totallylazy.Files;
import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.unsprung.careless.CacheFluffer.cacheFluffer;
import static org.junit.Assert.assertThat;

public class CacheFlufferTest {
    @Test
    public void flufferShouldPreloadCacheIfFileExists() throws MalformedURLException {
        final File file = new File("out", "out.css");
        write(bytes("file content"), file);

        CompiledLessCache cache = new CompiledLessCache();
        cacheFluffer(one(pair("file.css", file.toURI().toURL())), cache);
        assertThat(cache.get("file.css").get().css, is("file content"));
    }

    @Test
    public void itShouldIgnoreAndNotPreloadIfFileDoesNotExist() throws MalformedURLException {
        CompiledLessCache cache = new CompiledLessCache();
        cacheFluffer(one(pair("file2.css", new URL("file:./out/file2.css"))), cache);
        assertThat(cache.get("file2.css"), is(Option.<CompiledLess>none()));
    }
}