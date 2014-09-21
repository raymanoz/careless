package com.unsprung.careless;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Option;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Option.option;

public class CompiledLessCache {
    private Map<String, CompiledLess> cache = new HashMap<String, CompiledLess>();

    private CompiledLessCache() {}

    public static CompiledLessCache compiledLessCache() {return new CompiledLessCache();}

    public Option<CompiledLess> put(String key, CompiledLess less) {
        return option(cache.put(key, less));
    }

    public Option<CompiledLess> get(String key) {
        return Option.option(cache.get(key));
    }

    public Function1<CompiledLess, Boolean> stale(final Date date) {
        return new Function1<CompiledLess, Boolean>() {
            @Override
            public Boolean call(CompiledLess compiledLess) throws Exception {
                return compiledLess.stale(date);
            }
        };
    }
}
