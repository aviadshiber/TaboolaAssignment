package com.taboola;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

@UtilityClass
public class FileUtils {
    public static BufferedReader newBufferedReader(String relativePath, Charset cs) {
        val decoder = cs.newDecoder();
        val inputStream = getFileFromResourceAsStream(relativePath);
        Reader reader = new InputStreamReader(inputStream, decoder);
        return new BufferedReader(reader);
    }

    private static InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}
