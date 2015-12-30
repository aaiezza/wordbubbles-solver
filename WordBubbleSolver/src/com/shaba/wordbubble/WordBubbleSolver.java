package com.shaba.wordbubble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import com.shaba.wordbubble.utils.Dictionary;

/**
 * @author Alex Aiezza
 *
 */
public class WordBubbleSolver
{
    private static final String ILLEGAL_WORD_SIZES_FORMAT = "Cannot have word sizes %s, which add to %d, when there are only %d letters in the given configuration.";

    public static final Dictionary GET_DEFAULT_DICTIONARY()
    {
        final Dictionary dictionary = new Dictionary();
        try ( final Scanner sc = new Scanner( new File( "resources/dictionary.txt" ) ) )
        {
            sc.forEachRemaining( word -> dictionary.insert( word.toUpperCase() ) );
        } catch ( final FileNotFoundException e )
        {
            e.printStackTrace();
        }

        try ( final Scanner sc = new Scanner( new File( "resources/google-dictionary.txt" ) ) )
        {
            sc.forEachRemaining( word -> dictionary.insert( word.toUpperCase() ) );
        } catch ( final FileNotFoundException e )
        {
            e.printStackTrace();
        }

        return dictionary;
    }

    /* * * */

    private final Dictionary dictionary;

    private final Configuration configuration;

    public WordBubbleSolver( final Configuration configuration )
    {
        this.configuration = configuration;
        this.dictionary = GET_DEFAULT_DICTIONARY();
    }

    public WordBubbleSolver( final Configuration configuration, final Dictionary dictionary )
    {
        this.configuration = configuration;
        this.dictionary = dictionary;
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public Dictionary getDictionary()
    {
        return dictionary;
    }

    /* * * */

    public Set<Set<String>> solve( final int... wordSizes )
    {
        checkRequiredLetters( wordSizes );
        final Set<Set<String>> solutions = Collections
                .synchronizedSet( new LinkedHashSet<Set<String>>() );

        configuration.getLetters().parallelStream()
                .forEach( letter -> processLetter( letter, solutions, new LinkedHashSet<String>(),
                    new Stack<Letter>(), letter.getLetter(), wordSizes, 0 ) );

        return solutions;
    }

    private void processLetter(
            final Letter letter,
            final Set<Set<String>> solutions,
            final Set<String> builtSolution,
            final Stack<Letter> seenLetters,
            final String builtWord,
            final int [] wordSizes,
            final int wordSizeIndex )
    {
        final Stack<Letter> seenLettersClone = new Stack<Letter>();
        seenLettersClone.addAll( seenLetters );
        seenLettersClone.push( letter );

        if ( builtWord.length() == wordSizes[wordSizeIndex] )
        {
            if ( dictionary.search( builtWord ) )
            {
                final Set<String> builtSolutionClone = new LinkedHashSet<String>( builtSolution );
                builtSolutionClone.add( builtWord );

                if ( wordSizeIndex + 1 < wordSizes.length )
                {
                    configuration.getLetters().stream()
                            .filter( let -> !seenLettersClone.contains( let ) )
                            .forEach( dubLetter -> processLetter( dubLetter, solutions,
                                builtSolutionClone, seenLettersClone, dubLetter.getLetter(),
                                wordSizes, wordSizeIndex + 1 ) );
                } else if ( builtSolutionClone.size() == wordSizes.length )
                {
                    solutions.add( builtSolutionClone );
                }
            }

            return;
        }

        letter.getLinkedLetters().stream().filter( let -> !seenLettersClone.contains( let ) )
                .forEach( subLetter -> processLetter( subLetter, solutions, builtSolution,
                    seenLettersClone, builtWord + subLetter.getLetter(), wordSizes,
                    wordSizeIndex ) );
    }

    private void checkRequiredLetters( final int... wordSizes )
    {
        int lettersRequired = 0;
        for ( int wS : wordSizes )
            lettersRequired += wS;
        if ( lettersRequired > configuration.numberOfLetters() )
            throw new IllegalArgumentException( String.format( ILLEGAL_WORD_SIZES_FORMAT,
                Arrays.toString( wordSizes ), lettersRequired, configuration.numberOfLetters() ) );
    }

    /* * * */

    public static void main( final String [] args ) throws FileNotFoundException
    {
        // Go into Shell Mode
        if ( args.length <= 0 /* arg.equals( "-s" ) */ )
        {
            final Dictionary dictionary = GET_DEFAULT_DICTIONARY();
            @SuppressWarnings ( "resource" )
            final Scanner sc = new Scanner( System.in );

            shellMode: while ( true )
            {
                // ask for number of rows and columns or to quit
                int rows = 0, cols = 0;
                while ( true )
                {
                    System.out.print( "\n Give rows and columns (#R #C) or (q) to quit: " );
                    final String input = sc.nextLine();

                    if ( input.equalsIgnoreCase( "q" ) )
                        break shellMode;

                    final String [] rowsCols = input.split( " " );
                    try
                    {
                        rows = Integer.parseInt( rowsCols[0] );
                        cols = Integer.parseInt( rowsCols[1] );
                        break;
                    } catch ( final NumberFormatException | ArrayIndexOutOfBoundsException e )
                    {
                        System.err.printf(
                            "%n  '%s' is not a valid number of rows and columns%n    Try again.%n",
                            input );
                    }
                }

                // Insert configuration one row at a time
                final Configuration config = new Configuration( cols, rows );

                System.out.println( "\n Give board row by row now:" );

                for ( int row = 0; row < rows; row++ )
                    ConfigurationParser.parseRow( config, row, sc.nextLine() );

                final WordBubbleSolver wbs = new WordBubbleSolver( config, dictionary );

                // ask for word sizes
                System.out.printf( "%n Configuration recieved:%n%s%n%n", config );

                Set<Set<String>> words;
                int [] wordSizes = null;

                while ( true )
                {
                    System.out.println( " List word sizes to check for:" );

                    try
                    {
                        final StringTokenizer wordSizeTokens = new StringTokenizer( sc.nextLine() );
                        if ( !wordSizeTokens.hasMoreTokens() )
                            continue;
                        final List<Integer> wordSizeList = new ArrayList<Integer>();

                        while ( wordSizeTokens.hasMoreTokens() )
                            wordSizeList.add( Integer.parseInt( wordSizeTokens.nextToken() ) );

                        wordSizes = new int [wordSizeList.size()];
                        for ( int i = 0; i < wordSizes.length; i++ )
                            wordSizes[i] = wordSizeList.get( i );

                        // give answer
                        final long t1 = System.currentTimeMillis();
                        words = wbs.solve( wordSizes );
                        final double t2 = ( System.currentTimeMillis() - t1 ) / 1000d;
                        System.out.printf( "%s%n%n %d answers in %.4f sec%n%n",
                            words.toString().replaceAll( "],", "]\n" ), t2 );

                    } catch ( final NumberFormatException e )
                    {
                        System.err.println( "%n%n Please enter a valid word size.%n%n" );
                        continue;
                    } catch ( final IllegalArgumentException e )
                    {
                        System.err.printf( " %s%n%n", e.getMessage() );
                        continue;
                    }
                    break;
                }

                // narrow solution
                final Set<String> filterPos = new HashSet<String>();
                final Set<String> filterNeg = new HashSet<String>();
                while ( true )
                {
                    System.out.printf( "  Current Pos Filter: %s%n", filterPos );
                    System.out.printf( "  Current Neg Filter: %s%n", filterNeg );
                    System.out.print(
                        " Narrow down solution (word), (!word), remove filter (R WORD), (R !WORD) or (-) to continue: " );
                    final String input = sc.nextLine().trim();

                    if ( input.equals( "-" ) )
                        break;

                    if ( input.startsWith( "R " ) )
                        if ( input.substring( 2 ).startsWith( "!" ) )
                            filterNeg.remove( input.substring( 3 ) );
                        else filterPos.remove( input.substring( 2 ) );
                    else if ( input.startsWith( "!" ) )
                        filterNeg.add( input.substring( 1 ) );
                    else filterPos.add( input );


                    Set<Set<String>> narrowWords = new LinkedHashSet<Set<String>>();
                    words.parallelStream().filter( sol -> sol.containsAll( filterPos ) )
                            .filter( sol ->
                    {
                                for ( final String fn : filterNeg )
                                {
                                    if ( sol.contains( fn ) )
                                        return false;
                                }
                                return true;
                            } ).forEach( narrowWords::add );

                    System.out.printf( "%s%n%n", narrowWords.toString().replaceAll( "],", "]\n" ) );
                }
            }
        } else
        {
            final long t1 = System.currentTimeMillis();

            final WordBubbleSolver wbs = new WordBubbleSolver(
                    new ConfigurationParser().parse( args[0] ) );

            final Set<Set<String>> words = wbs.solve( Integer.parseInt( args[1] ) );

            final double t2 = ( System.currentTimeMillis() - t1 ) / 1000d;

            System.out.printf( "%s%n%n %.4f sec%n", words, t2 );
        }
    }
}
