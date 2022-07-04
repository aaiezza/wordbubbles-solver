package com.shaba.wordbubbles;

import java.io.FileNotFoundException;
import java.util.Set;

import org.junit.Test;

import com.sun.media.sound.InvalidFormatException;

public class WordBubbleSolverTest
{
    private WordBubbleSolver          wbs;

    private final ConfigurationParser CONFIG_PARSER = new ConfigurationParser();

    @Test
    public void testFile() throws FileNotFoundException, InvalidFormatException
    {
        wbs = new WordBubbleSolver( CONFIG_PARSER.parse( "test_resources/test.txt" ) );

        final Set<Solution> solutions = wbs.solve( 6, 7 );

        solutions.forEach( System.out::println );
    }
}
