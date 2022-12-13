package com.taboola.question2;

/**
 * An MVP for question 2.
 * same as presented in the document.
 */
public class SolutionTwo implements Runnable{
    @Override
    public void run() {
        WordsCounter wc = new WordsCounter();
        //paths from resources:
        wc.load(
                "inputs/question2/java_song.txt",
                "inputs/question2/scala_song.txt"
        );
        wc.displayStatus();
    }
}
