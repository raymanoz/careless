package com.unsprung.careless;

import org.lesscss.LessException;
import org.lesscss.LessSource;

import java.util.Date;

import static com.unsprung.careless.CompiledLess.compiledLess;

public class OfficialLessCompiler implements LessCompiler {
    @Override
    public CompiledLess compile(LessSource lessSource, String name) {
        try {
            org.lesscss.LessCompiler compiler = new org.lesscss.LessCompiler();
            compiler.init();
            return compiledLess(name, compiler.compile(lessSource), new Date(lessSource.getLastModifiedIncludingImports()));
        } catch (LessException e) {
            throw new RuntimeException(e);
        }
    }
}