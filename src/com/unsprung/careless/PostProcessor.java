package com.unsprung.careless;

import com.googlecode.totallylazy.ReducerFunction;
import com.googlecode.totallylazy.Unary;
import org.lesscss.LessSource;

import static com.googlecode.totallylazy.Sequences.sequence;

public class PostProcessor implements LessCompiler {
    private final LessCompiler compiler;
    private final Iterable<Unary<String>> postProcessors;

    private PostProcessor(LessCompiler compiler, Iterable<Unary<String>> postProcessors) {
        this.compiler = compiler;
        this.postProcessors = postProcessors;
    }

    public static PostProcessor postProcessor(LessCompiler compiler, Iterable<Unary<String>> postProcessors) {
        return new PostProcessor(compiler, postProcessors);
    }

    @Override
    public CompiledLess compile(final LessSource lessSource, final String name) {
        return sequence(postProcessors).reduce(process(lessSource, name));
    }

    private ReducerFunction<Unary<String>, CompiledLess> process(final LessSource lessSource, final String name) {
        return new ReducerFunction<Unary<String>, CompiledLess>() {
            @Override
            public CompiledLess call(CompiledLess compiledLess, Unary<String> postProcessor) throws Exception {
                return compiledLess.compiledLess(postProcessor.call(compiledLess.css));
            }

            @Override
            public CompiledLess identity() {
                return compiler.compile(lessSource, name);
            }
        };
    }
}
