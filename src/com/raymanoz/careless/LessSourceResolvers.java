package com.raymanoz.careless;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import org.lesscss.LessSource;

import java.io.File;

import static com.googlecode.totallylazy.regex.Regex.regex;

public class LessSourceResolvers {
    public static Function1<String, LessSource> byJar(final String path) {
        return new Function1<String, LessSource>() {
            @Override
            public LessSource call(String lessFile) throws Exception {
                return new LessSource(new JarResource(path + lessFile));
            }
        };
    }

    public static Function1<String, LessSource> byFile(final String path) {
        return new Function1<String, LessSource>() {
            @Override
            public LessSource call(String lessFile) throws Exception {
                return new LessSource(new File(path + lessFile));
            }
        };
    }

    public static Function1<String, LessSource> from(String property) {
        final Sequence<String> extract = regex("(jar|file):(.*)").extract(property);
        final String scheme = extract.get(0);

        if (scheme.equals("jar")) return byJar(property);
        else if (scheme.equals("file")) return byFile(extract.get(1));

        throw new RuntimeException("Illegal scheme: " + scheme);
    }
}