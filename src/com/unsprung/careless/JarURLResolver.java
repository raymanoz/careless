package com.unsprung.careless;

import org.lesscss.LessResolver;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.string;

public class JarURLResolver implements LessResolver {
    private final String path;

    public JarURLResolver(String path) {
        this.path = path;
    }

    @Override
    public boolean exists(String file) {
        return urlConnection(file).getLastModified() != 0;
    }

    @Override
    public String resolve(String file) throws IOException {
        return string(urlConnection(file).getInputStream());
    }

    @Override
    public long getLastModified(String file) {
        return urlConnection(file).getLastModified();
    }

    @Override
    public LessResolver resolveImport(String parent) {
        return new JarURLResolver(sequence(full(parent).split("/")).init().toString("", "/", "/"));
    }

    private URLConnection urlConnection(String file) {
        try {
            return new URL(full(file)).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String full(String file) { return path + file; }
}
