package com.raymanoz.careless;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Pair;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import static com.googlecode.totallylazy.Runnables.VOID;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.string;
import static com.raymanoz.careless.CompiledLess.compiledLess;

public class CacheFluffer {
    public final CompiledLessCache cache;

    private CacheFluffer(final Iterable<Pair<String, URL>> files, final CompiledLessCache cache) {
        this.cache = cache;
        sequence(files).each(new Function1<Pair<String, URL>, Void>() {
            @Override
            public Void call(Pair<String, URL> file) throws Exception {
                final URLConnection connection = file.second().openConnection();
                if (connection.getLastModified() != 0)
                    cache.put(file.first(), compiledLess(file.first(), string(connection.getInputStream()), new Date(connection.getLastModified())));
                return VOID;
            }
        });
    }

    public static CacheFluffer cacheFluffer(final Iterable<Pair<String, URL>> files, CompiledLessCache cache) {
        return new CacheFluffer(files, cache);
    }
}
