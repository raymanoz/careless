package com.unsprung.careless;

import org.lesscss.LessSource;

import java.util.Date;

import static com.unsprung.careless.CompiledLess.compiledLess;

public class StubCompiler implements LessCompiler {
    private String css;
    public CompiledLess compiledResult;

    private StubCompiler(String css) {
        this.css = css;
    }

    public static StubCompiler returning(String css) {return new StubCompiler(css);}

    @Override
    public CompiledLess compile(LessSource lessSource, String name) {
        compiledResult = compiledLess(name, css, new Date());
        return compiledResult;
    }
}
