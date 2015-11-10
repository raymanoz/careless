package com.raymanoz.careless;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.regex.Regex;
import org.lesscss.LessSource;

import java.io.File;

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
        final Regex regex = Regex.regex("(jar|file):(.*)");

        final Sequence<String> extract = regex.extract(property);
        final String scheme = extract.get(0);

        if (scheme.equals("jar")) return byJar();
        else if (scheme.equals("file")) return byFile();

        throw new RuntimeException("Illegal scheme: " + scheme);
    }
}
