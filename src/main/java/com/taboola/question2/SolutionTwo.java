package com.taboola.question2;

public class SolutionTwo implements Runnable{
    @Override
    public void run() {
        WordsCounter wc = new WordsCounter();
        wc.load(
                "src/main/resources/inputs/question2/java_song.txt",
                "src/main/resources/inputs/question2/scala_song.txt"
        );
        wc.displayStatus();
    }
}
