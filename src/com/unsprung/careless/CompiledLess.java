package com.unsprung.careless;

import java.util.Date;

public class CompiledLess {
    public final String name;
    public final String css;
    public final Date modified;

    private CompiledLess(String name, String css, Date modified) {
        this.name = name;
        this.css = css;
        this.modified = modified;
    }

    public static CompiledLess compiledLess(String name, String css, Date modified) {
        return new CompiledLess(name, css, modified);
    }

    public boolean stale(Date date) {
        return modified.before(date);
    }

    public CompiledLess compiledLess(String css) {
        return compiledLess(name, css, modified);
    }
}