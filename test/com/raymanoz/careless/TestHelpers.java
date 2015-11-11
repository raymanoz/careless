package com.raymanoz.careless;

import com.googlecode.totallylazy.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Strings.bytes;

public class TestHelpers {
    public static File createJar(final String name, final Sequence<Pair<String, String>> entries) {
        File jar = new File("out", name);
        using(aJar(jar), addEntries(entries));
        return jar;
    }

    private static Callable1<JarOutputStream, Void> addEntries(final Sequence<Pair<String, String>> files) {
        return new Callable1<JarOutputStream, Void>() {
            @Override
            public Void call(final JarOutputStream stream) throws Exception {
                files.each(addEntry(stream));
                return Runnables.VOID;
            }
        };
    }

    private static Block<Pair<String, String>> addEntry(final JarOutputStream stream) {
        return new Block<Pair<String, String>>() {
            @Override
            protected void execute(Pair<String, String> pair) throws Exception {
                withFile(pair.first(), pair.second(), stream);
            }
        };
    }

    private static JarOutputStream aJar(File file) {
        try {
            return new JarOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void withFile(String name, String content, JarOutputStream stream) {
        try {
            stream.putNextEntry(new JarEntry(name));
            stream.write(bytes(content));
            stream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
