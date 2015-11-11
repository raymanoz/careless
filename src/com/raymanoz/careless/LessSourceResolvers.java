package com.raymanoz.careless;

import com.googlecode.totallylazy.Function1;
import org.lesscss.LessSource;

import java.io.File;

import static com.googlecode.totallylazy.regex.Regex.regex;

public class LessSourceResolvers {
    public static Function1<String, LessSource> byJar() {
        return new Function1<String, LessSource>() {
            @Override
            public LessSource call(String lessFile) throws Exception {
                return new LessSource(new JarURLResolver(lessFile));
            }
        };
    }

    public static Function1<String, LessSource> byFile() {
        return new Function1<String, LessSource>() {
            @Override
            public LessSource call(String lessFile) throws Exception {
                return new LessSource(new File(lessFile));
            }
        };
    }

    public static Function1<String, LessSource> from(String property) {
        final String scheme = regex("(jar|file):(.*)").extract(property).get(0);

        if (scheme.equals("jar")) return byJar();
        else if (scheme.equals("file")) return byFile();

        throw new RuntimeException("Illegal scheme: " + scheme);
    }
}