package com.raymanoz.careless;

import org.lesscss.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.googlecode.totallylazy.Sequences.sequence;

public class JarURLResolver implements Resource {
    private final String file;

    public JarURLResolver(String file) {
        this.file = file;
    }

    @Override
    public boolean exists() {
        return urlConnection(file).getLastModified() != 0;
    }

    @Override
    public long lastModified() {
        return urlConnection(file).getLastModified();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return urlConnection(file).getInputStream();
    }

    @Override
    public Resource createRelative(String relativeResourcePath) throws IOException {
        return new JarURLResolver(sequence(relativeResourcePath.split("/")).init().toString("", "/", "/"));
    }

    @Override
    public String getName() {
        return file;
    }

    private URLConnection urlConnection(String file) {
        try {
            return new URL(file).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
