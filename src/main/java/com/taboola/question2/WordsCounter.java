package com.taboola.question2;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

import static com.taboola.FileUtils.newBufferedReader;

/**
 * WordsCounter `loads` files and compute in parallel on each file
 * the word count using ConcurrentHashMap<String, LongAdder>.
 * with LongAdder we can increment on several keys with lock-free algorithms
 * which are great for performance (assuming there are no cache misses).
 * the `displayStatus` await for the results, and finally write them to the writer.
 */
@Slf4j
public class WordsCounter {

    private final Map<String, LongAdder> wordToCount;
    private final long timeout;
    private final TimeUnit timeoutUnit;
    private final ExecutorService executor;
    private final Writer writer;

    public WordsCounter() {
        //initialize WordsCounter with default values:
        this(100, TimeUnit.MILLISECONDS, new PrintWriter(System.out));
    }

    public WordsCounter(long timeout, TimeUnit timeoutUnit, Writer writer) {
        //each thread is reading from IO, hence it is best to have newCachedThreadPool
        executor = Executors.newCachedThreadPool();
        this.wordToCount = new ConcurrentHashMap<>();
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
        this.writer = writer;
    }

    @SneakyThrows
    public void load(String... files) {
        @Cleanup val filesStream = Arrays.stream(files).onClose(executor::shutdown);

        filesStream.forEach(filePath -> {
            CompletableFuture.runAsync(() -> {
                try {
                    countWordsInFile(filePath);
                } catch (Exception e) {
                    log.error(String.valueOf(e));
                    executor.shutdown();
                }
            }, executor);
        });


    }

    @SneakyThrows
    private void countWordsInFile(String filePath) {
        @Cleanup val br = newBufferedReader(filePath, StandardCharsets.UTF_8);
        //TODO: Separate read pool & compute pool with CompletableFuture
        br.lines().map(l -> l.split("\\s+")).forEach(words -> {
            for (String word : words) {
                val key = normalize(word);
                if (!key.isEmpty()) {
                    val adder = wordToCount.computeIfAbsent(key, k -> new LongAdder());
                    adder.increment();
                }
            }
        });
    }

    private String normalize(String word) {
        return word.toLowerCase().strip();
    }

    @SneakyThrows
    public void displayStatus() {
        log.info("Awaiting for results...");
        if (!executor.awaitTermination(timeout, timeoutUnit)) {
            log.warn("Timeout been reached, collecting results until now");
        }
        val totalCount = new LongAdder();
        wordToCount.entrySet().parallelStream().forEach(entry -> {
            val word = entry.getKey();
            val count = entry.getValue().sum();
            totalCount.add(count);
            try {
                val out = String.format("%s %d%n", word, count);
                writer.write(out);
            } catch (IOException e) {
                log.error(String.valueOf(e));
            }
        });
        val summary = String.format("** total: %s%n", totalCount.sum());
        writer.write(summary);
        writer.flush();
    }
}
