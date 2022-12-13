package com.taboola.question1;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.stream.Stream;

/**
 * An MVP example of using the SevenSegmentDisplayParser.
 * we read batch of 4 lines, in serial on each file
 * and using the SevenSegmentDisplayParser to parse the 7 segment display representation to numbers.
 */
@Slf4j
public class SolutionOne implements Runnable {
    private static final SevenSegmentDisplayParser parser = new SevenSegmentDisplayParser();
    private final static int batchSize = 4;
    private final String[] filePaths = new String[]{
            "inputs/question1/input_Q1a.txt",
            "inputs/question1/input_Q1b.txt"
    };

    @SneakyThrows
    @Override
    public void run() {
        @Cleanup Writer writer = new PrintWriter(System.out);

        for (String path : filePaths) {
            parse7SegmentFile(path, writer);
            log.debug("---> file ends here");
        }
    }

    @SneakyThrows
    private static void parse7SegmentFile(String filePath, Writer writer) {
        @Cleanup val batchReader = new BatchLineReader(filePath, batchSize);
        @Cleanup val parsedStream = getParsedStream(writer, batchReader);

        parsedStream.forEach(parsed -> {
            try {
                writer.write(String.format("%s\n", parsed));
            } catch (IOException e) {
                log.error(String.valueOf(e));
            }
        });
    }

    private static Stream<String> getParsedStream(Writer writer, BatchLineReader batchReader) {
        return batchReader.stream().map(parser)
                .onClose(() -> {
                    try {
                        writer.flush();
                    } catch (IOException e) {
                        log.error(String.valueOf(e));
                    }
                });
    }
}
