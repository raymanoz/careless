package com.raymanoz.careless;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Unary;
import org.lesscss.LessSource;

import java.net.URL;

import static com.raymanoz.careless.CompiledLessCache.compiledLessCache;
import static com.raymanoz.careless.OfficialLessCompiler.officialLessCompiler;

public class CarelessCompiler implements LessCompiler {
    private final CachingLessCompiler compiler;

    private CarelessCompiler(Iterable<Pair<String, URL>> preloadedFiles, Iterable<Unary<String>> postProcessors) {
        compiler = CachingLessCompiler.cachingLessCompiler(
                PostProcessor.postProcessor(officialLessCompiler(), postProcessors),
                CacheFluffer.cacheFluffer(preloadedFiles, compiledLessCache()).cache
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