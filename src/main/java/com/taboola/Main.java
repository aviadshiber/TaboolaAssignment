package com.taboola;

import com.taboola.question1.BatchLineReader;
import com.taboola.question1.SevenSegmentDisplayParser;
import lombok.Cleanup;
import lombok.val;

import java.io.*;


public class Main {

    private static final SevenSegmentDisplayParser parser = new SevenSegmentDisplayParser();
    final static int batchSize = 4;

    public static void main(String[] args) throws IOException {
        playSolutionOne();
    }

    private static void playSolutionOne() throws IOException {
        @Cleanup Writer writer = new PrintWriter(System.out);
        String[] filePaths = new String[]{
                "src/main/resources/input_Q1a.txt",
                "src/main/resources/input_Q1b.txt"
        };

        for (String path : filePaths) {
            parse7SegmentFile(path, writer);
            System.out.println("----------NEW FILE FROM HERE-----------");
        }
    }

    private static void parse7SegmentFile(String filePath, Writer writer) throws IOException {
            @Cleanup val br = new BufferedReader(new FileReader(filePath));
            val batchReader = new BatchLineReader(br,batchSize);
            @Cleanup val parsedStream =
                    batchReader.stream().map(parser)
                    .onClose(() -> {
                        try {
                            writer.flush();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });

            parsedStream.forEach(parsed ->{
                try {
                    writer.write(String.format("%s\n", parsed));
                } catch (IOException e) {
                    System.err.println(e);
                }
            });

    }
}
