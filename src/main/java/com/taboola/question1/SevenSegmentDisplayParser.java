package com.taboola.question1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * SevenSegmentDisplayParser is a pure transformation function, given 4 lines of SevenDisplaySegment
 * (last line is blank line)
 * will parse the display representation into a number representation.
 * For Example:
 * Given input:
 *   _  _  _        _     _  _
 * |_ | || |  ||_| _|  ||_ |_
 * |_||_||_|  |  | _|  | _| _|
 * The output will be:
 * 600143155
 */
public class SevenSegmentDisplayParser implements Function<String, String> {

    public static final char BROKEN_SEGMENT_VALUE = '?';
    private static final int segmentSize = 3;
    final static int lastSegmentStateId = segmentSize - 1;
    // Define the dictionary that maps each 7-segment representation to a number
    private static final Map<String, Character> SEGMENT_DICT = new HashMap<>();
    public static final Character BLANK = ' ';
    public static final String ILLEGAL_COLUMN = "ILLEGAL";


    /**
     * segmentation mapping from of 3
     * concatenated columns in 7-segment representation
     * to their number
     */
    static {
        SEGMENT_DICT.put(" ||_ _ ||", '0');
        SEGMENT_DICT.put("       ||", '1');
        SEGMENT_DICT.put("  |___ | ", '2');
        SEGMENT_DICT.put("   ___ ||", '3');
        SEGMENT_DICT.put(" |  _  ||", '4');
        SEGMENT_DICT.put(" | ___  |", '5');
        SEGMENT_DICT.put(" ||___  |", '6');
        SEGMENT_DICT.put("   _   ||", '7');
        SEGMENT_DICT.put(" ||___ ||", '8');
        SEGMENT_DICT.put(" | ___ ||", '9');
    }

    @Override
    public String apply(String lines) {
        String[] l = lines.split("\n");
        if (isInvalid(l)) return null;
        return segmentsToNumbers(linesToSegments(l));
    }

    private static boolean isInvalid(String[] lines) {
        //validate we have enough lines to work with
        return lines.length != segmentSize;
    }

    /**
     * Assuming we have segmentSize number of lines,
     * and at least segmentSize number of letter in each line -
     * transforms lines to list of segments.
     *
     * @param lines of the represented segments
     * @return List of segments
     */
    private static List<String> linesToSegments(String[] lines) {
        int numberOfColumns = Stream.of(lines[0], lines[1], lines[2])
                .map(String::length)
                .max(Integer::compareTo)
                .orElse(0);
        int numberOfSegments = numberOfColumns / segmentSize;
        List<String> segments = new ArrayList<>(numberOfSegments);
        //iterate in linear way on group of 3 columns
        StringBuilder segmentBuilder = new StringBuilder();

        for(int col=0;col<numberOfColumns;col++){
            for (int row = 0; row < segmentSize; row++) {
                int lineLength = lines[row].length();
                if(col<lineLength) {
                    segmentBuilder.append(lines[row].charAt(col));
                }else{
                    segmentBuilder.append(BLANK);
                }
            }
            if ((col % segmentSize) == lastSegmentStateId) {
                segments.add(new String(segmentBuilder));
                segmentBuilder = new StringBuilder();
            }
        }
        return segments;
    }

    /**
     * transforms segments ,
     * when each segment is concatenated as columns,
     * to a number represented as String.
     *
     * @param segments an entire segment number
     * @return number represented as String value.
     */
    private String segmentsToNumbers(List<String> segments) {
        StringBuilder numbers = new StringBuilder();
        boolean foundBrokenSegment = false;
        for (String segment : segments) {
            Character number = SEGMENT_DICT.get(segment);
            if(number==null){
                foundBrokenSegment = true;
                number = BROKEN_SEGMENT_VALUE;
            }
            numbers.append(number);
        }
        if(foundBrokenSegment){
            numbers.append(BLANK).append(ILLEGAL_COLUMN);
        }
        return new String(numbers);
    }


}
