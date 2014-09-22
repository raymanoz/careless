package com.unsprung.careless;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Unary;
import org.lesscss.LessSource;

import java.net.URL;

import static com.unsprung.careless.CacheFluffer.cacheFluffer;
import static com.unsprung.careless.CachingLessCompiler.cachingLessCompiler;
import static com.unsprung.careless.CompiledLessCache.compiledLessCache;
import static com.unsprung.careless.OfficialLessCompiler.officialLessCompiler;
import static com.unsprung.careless.PostProcessor.postProcessor;

public class CarelessCompiler implements LessCompiler {
    private final CachingLessCompiler compiler;

    private CarelessCompiler(Iterable<Pair<String, URL>> preloadedFiles, Iterable<Unary<String>> postProcessors) {
        compiler = cachingLessCompiler(
                postProcessor(officialLessCompiler(), postProcessors),
                cacheFluffer(preloadedFiles, compiledLessCache()).cache
        );
    }

    public static CarelessCompiler carelessCompiler(Iterable<Pair<String, URL>> preloadedFiles, Iterable<Unary<String>> postProcessors) {
        return new CarelessCompiler(preloadedFiles, postProcessors);
    }

    @Override
    public CompiledLess compile(LessSource lessSource, String name) {
        return compiler.compile(lessSource, name);
    }
}