package com.unsprung.careless;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Runnables;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.unsprung.careless.LessSourceResolvers.*;
import static org.junit.Assert.assertThat;

public class LessSourceResolversTest {
    @Before
    public void createOutputDirectory() throws IOException {
        new File("out").mkdir();
    }

    @Test
    public void eachSchemeShouldBeResolvable() {
        from("jar:file:/stuff!/read.me");
        from("file:/file/on/your/disk.txt");
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowAnExceptionOnBadScheme() {
        from("mars:/wecomeinpeace/shoottokill");
    }

    @Test
    public void fileResolverShouldResolverToAFile() throws IOException {
        String content = "// content";
        write(bytes(content), new File("out", "file.less"));
        assertThat(byFile("out/").apply("file.less").getContent(), is(content));
    }

    @Test
    public void jarResolverShouldResolverToAFileInAJar() throws IOException {
        File jar = new File("out", "out.jar");
        final String content = "// content";
        using(aJar(jar), new Callable1<JarOutputStream, Void>() {
            @Override
            public Void call(JarOutputStream stream) throws Exception {
                withFile("thing.less", content, stream);
                return Runnables.VOID;
            }
        });
        assertThat(byJar("jar:file:" + jar.getAbsolutePath() + "!/").apply("thing.less").getContent(), is(content));
    }

    private JarOutputStream aJar(File file) {
        try {
            return new JarOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void withFile(String name, String content, JarOutputStream stream) {
        try {
            stream.putNextEntry(new JarEntry(name));
            stream.write(bytes(content));
            stream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}