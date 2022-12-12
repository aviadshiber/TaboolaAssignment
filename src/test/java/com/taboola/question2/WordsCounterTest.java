package com.taboola.question2;

import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;


public class WordsCounterTest {


    @Test
    public void testCount() {
        StringWriter writer = new StringWriter();
        WordsCounter wc = new WordsCounter(50, TimeUnit.MILLISECONDS, writer);
        // not ideal - we are working with IO here, but had no choice,
        // since the question requirement demands it
        wc.load(
                "src/main/resources/inputs/question2/java_song.txt",
                "src/main/resources/inputs/question2/scala_song.txt"
        );
        wc.displayStatus();
        assertThat(writer.toString(), containsString("code 5"));
        assertThat(writer.toString(), containsString("scala 4"));
        assertThat(writer.toString(), containsString("with 6"));
        assertThat(writer.toString(), containsString("programmers 3"));
        assertThat(writer.toString(), containsString("java 6"));
        assertThat(writer.toString(), containsString("** total: 237"));
    }

}