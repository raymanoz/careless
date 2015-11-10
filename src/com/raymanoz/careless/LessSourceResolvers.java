package com.raymanoz.careless;

import com.googlecode.totallylazy.regex.Regex;
import org.lesscss.LessSource;

import java.io.File;
import java.io.IOException;

public class LessSourceResolvers {

    public static LessSource byJar(String lessFile) throws Exception {
        return new LessSource(new JarURLResolver(lessFile));
    }

    public static LessSource byFile(String lessFile) throws IOException {
        return new LessSource(new File(lessFile.replace("file:", "")));
    }

    public static LessSource from(String property) throws Exception {
        final Regex regex = Regex.regex("(jar|file):(.*)");

        final String scheme = regex.extract(property).head();

        if (scheme.equals("jar")) return byJar(property);
        else if (scheme.equals("file")) return byFile(property);

        throw new RuntimeException("Illegal scheme: " + scheme);
    }
}
