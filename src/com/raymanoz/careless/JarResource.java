package com.raymanoz.careless;

import org.lesscss.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.googlecode.totallylazy.Sequences.sequence;

public class JarResource implements Resource {
    private final String entry;

    public JarResource(String entry) {
        this.entry = entry;
    }

    @Override
    public boolean exists() {
        return urlConnection(entry).getLastModified() != 0;
    }

    @Override
    public long lastModified() {
        return urlConnection(entry).getLastModified();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return urlConnection(entry).getInputStream();
    }

    @Override
    public Resource createRelative(String relativeResourcePath) throws IOException {
        return new JarResource(sequence(entry.split("/")).init().toString("", "/", "/") + relativeResourcePath);
    }

    @Override
    public String getName() {
        return entry;
    }

    private URLConnection urlConnection(String file) {
        try {
            return new URL(file).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
