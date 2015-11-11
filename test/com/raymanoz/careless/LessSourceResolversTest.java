package com.raymanoz.careless;

import com.googlecode.totallylazy.Files;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.raymanoz.careless.LessSourceResolvers.*;
import static com.raymanoz.careless.TestHelpers.createJar;
import static org.junit.Assert.assertThat;

public class LessSourceResolversTest {
    @Before
    public void createOutputDirectory() throws IOException {
        new File("out").mkdir();
    }

    @Test
    public void eachSchemeShouldBeResolvable() throws Exception {
        File jar = createJar("out1.jar", sequence(pair("thing.less", "// stuff")));
        from("jar:file:"+ jar.getAbsolutePath()+"!/thing.less");
        File file = Files.temporaryFile();
        from("file:" + file.getAbsolutePath());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowAnExceptionOnBadScheme() throws Exception {
        from("mars:/wecomeinpeace/shoottokill");
    }

    @Test
    public void fileResolverShouldResolverToAFile() throws IOException {
        String content = "// content";
        write(bytes(content), new File("out", "file.less"));
        assertThat(byFile().apply("out/file.less").getContent(), is(content));
    }

    @Test
    public void jarResolverShouldResolverToAFileInAJar() throws Exception {
        final String content = "// content";
        String jar = createJar("out2.jar", sequence(pair("thing.less", content))).getAbsolutePath();
        assertThat(byJar().apply("jar:file:" + jar + "!/thing.less").getContent(), is(content));
    }
}