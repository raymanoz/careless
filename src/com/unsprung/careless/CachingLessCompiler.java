package com.unsprung.careless;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.annotations.tailrec;
import org.lesscss.LessSource;

import java.util.Date;

public class CachingLessCompiler implements LessCompiler {
    private final LessCompiler compiler;
    private final CompiledLessCache cache;

    private CachingLessCompiler(LessCompiler compiler, CompiledLessCache cache) {
        this.compiler = compiler;
        this.cache = cache;
    }

    public static CachingLessCompiler cachingLessCompiler(LessCompiler compiler, CompiledLessCache cache) {return new CachingLessCompiler(compiler, cache);}

    @Override
    @tailrec
    public CompiledLess compile(final LessSource lessSource, final String name) {
        if (compileRequired(lessSource, name)) cache.put(name, compiler.compile(lessSource, name));
        return cache.get(name).getOrElse(new Function<CompiledLess>() {
            @Override
            public CompiledLess call() throws Exception {
                return compile(lessSource, name);
            }
        });
    }

    private boolean compileRequired(final LessSource lessSource, String name) {
        return cache.get(name).map(cache.stale(new Date(lessSource.getLastModifiedIncludingImports()))).getOrElse(true);
    }
}
