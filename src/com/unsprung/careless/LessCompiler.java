package com.unsprung.careless;

import org.lesscss.LessSource;

public interface LessCompiler {
    CompiledLess compile(LessSource lessSource, String name);
}
