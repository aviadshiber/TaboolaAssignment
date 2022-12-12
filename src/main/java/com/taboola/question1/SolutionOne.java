package com.taboola.question1;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class SolutionOne implements Runnable {
    private static final SevenSegmentDisplayParser parser = new SevenSegmentDisplayParser();
    private final static int batchSize = 4;

    @SneakyThrows
    @Override
    public void run() {
        @Cleanup Writer writer = new PrintWriter(System.out);
        String[] filePaths = new String[]{
                "src/main/resources/inputs/question1/input_Q1a.txt",
                "src/main/resources/inputs/question1/input_Q1b.txt"
        };

        for (String path : filePaths) {
            parse7SegmentFile(path, writer);
            log.debug("---> file ends here");
        }
    }

    private static void parse7SegmentFile(String filePath, Writer writer) throws IOException {
        @Cleanup val br = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
        val batchReader = new BatchLineReader(br, batchSize);
        @Cleanup val parsedStream =
                batchReader.stream().map(parser)
                        .onClose(() -> {
                            try {
                                writer.flush();
                            } catch (IOException e) {
                                log.error(String.valueOf(e));
                            }
                        });

        parsedStream.forEach(parsed -> {
            try {
                writer.write(String.format("%s\n", parsed));
            } catch (IOException e) {
                log.error(String.valueOf(e));
            }
        });

    }
}
