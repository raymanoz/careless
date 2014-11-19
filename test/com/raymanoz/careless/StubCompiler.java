package com.raymanoz.careless;

import org.lesscss.LessSource;

import java.util.Date;

public class StubCompiler implements LessCompiler {
    private String css;
    public CompiledLess compiledResult;

    private StubCompiler(String css) {
        this.css = css;
    }

    public static StubCompiler returning(String css) {return new StubCompiler(css);}

    @Override
    public CompiledLess compile(LessSource lessSource, String name) {
        compiledResult = CompiledLess.compiledLess(name, css, new Date());
        return compiledResult;
    }
}
