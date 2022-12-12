package com.taboola.question1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BatchLineReader implements AutoCloseable{
    private final int batchSize;
    private final BufferedReader br;

    public BatchLineReader(BufferedReader br, int batchSize){
        this.br=br;
        this.batchSize=batchSize;
    }

    public Stream<String> stream() {

        Iterator<String> iter = new Iterator<>() {
            String nextLine = null;

            @Override
            public boolean hasNext() {
                if (nextLine != null) {
                    return true;
                } else {
                    try {
                        nextLine = br.readLine();
                        return (nextLine != null);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }

            @Override
            public String next() {
                StringBuilder fourLinesBuffer = new StringBuilder();
                int page=0;
                while(page<batchSize && hasNext()){
                    fourLinesBuffer.append(this.nextLine).append("\n");
                    nextLine = null;
                    page++;
                }
                return new String(fourLinesBuffer);
            }
        };
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        iter,
                        Spliterator.ORDERED | Spliterator.NONNULL),
                false);
    }

    @Override
    public void close() throws Exception {
        br.close();
    }
}
