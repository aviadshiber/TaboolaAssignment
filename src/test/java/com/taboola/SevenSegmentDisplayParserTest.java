package com.taboola;

import com.taboola.question1.SevenSegmentDisplayParser;
import org.junit.jupiter.api.Test;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SevenSegmentDisplayParserTest {

    private static final SevenSegmentDisplayParser sevenSegmentDisplayParser =
            new SevenSegmentDisplayParser();

    private static void assertDisplayEqualsTo(String display, String expected){
        assertThat(sevenSegmentDisplayParser.apply(display),equalTo(expected));
    }
    @Test
    public void testParsingOfZero(){
        String display =    " _ \n" +
                            "| |\n" +
                            "|_|\n";
        assertDisplayEqualsTo(display,"0");
    }

    @Test
    public void testParsingOfOne(){
        String display =    "   \n" +
                            "  |\n"+
                            "  |\n";

        assertDisplayEqualsTo(display,"1");
    }

    @Test
    public void testParsingOfTwo(){
        String display =    " _ \n" +
                            " _|\n" +
                            "|_ \n";

        assertDisplayEqualsTo(display,"2");
    }

    @Test
    public void testParsingOfThree(){
        String display =    " _ \n" +
                            " _|\n" +
                            " _|\n";

        assertDisplayEqualsTo(display,"3");
    }

    @Test
    public void testParsingOfFour(){
        String display =    "   \n" +
                            "|_|\n" +
                            "  |\n";

        assertDisplayEqualsTo(display,"4");
    }

    @Test
    public void testParsingOfFive(){
        String display =    " _ \n" +
                            "|_ \n" +
                            " _|\n";

        assertDisplayEqualsTo(display,"5");
    }

    @Test
    public void testParsingOfSix(){
        String display =    " _ \n" +
                            "|_ \n" +
                            "|_|\n";

        assertDisplayEqualsTo(display,"6");
    }

    @Test
    public void testParsingOfSeven(){
        String display =    " _ \n" +
                            "  |\n" +
                            "  |\n";

        assertDisplayEqualsTo(display,"7");
    }

    @Test
    public void testParsingOfEight(){
        String display =    " _ \n" +
                            "|_|\n" +
                            "|_|\n";

        assertDisplayEqualsTo(display,"8");
    }

    @Test
    public void testParsingOfNine(){
        String display =    " _ \n" +
                            "|_|\n" +
                            " _|\n";

        assertDisplayEqualsTo(display,"9");
    }

    @Test
    public void testParsingOfBrokenSegment(){
        String display =    " _ \n" +
                            "|  \n" +
                            "|_|\n";

        assertDisplayEqualsTo(display,"? ILLEGAL");
    }

    @Test
    public void testComplexNumber(){
        String display =    "    _  _  _  _  _  _  _  _ \n" +
                            "  || ||_  _| _||_   | _||_|\n" +
                            "  ||_| _| _||_ |_|  | _| _|\n";
        assertDisplayEqualsTo(display,"105326739");
    }

    @Test
    public void testComplexNumberWithBrokenSegment(){
        String display =    "       _  _  _  _     _  _ \n" +
                            "|_|  |  ||_||  |_   ||_| _|\n" +
                            "  |  |  ||_| _| _|  | _||_ \n";
        assertDisplayEqualsTo(display,"4178?5192 ILLEGAL");
    }

    @Test
    public void testParsingOfDisplayWith4Lines(){
        String display =    "    _  _  _  _  _  _  _  _ \n" +
                            "  || ||_  _| _||_   | _||_|\n" +
                            "  ||_| _| _||_ |_|  | _| _|\n" +
                            "\n";
        assertDisplayEqualsTo(display,"105326739");
    }

}