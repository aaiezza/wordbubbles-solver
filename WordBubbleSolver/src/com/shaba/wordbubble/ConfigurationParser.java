package com.shaba.wordbubble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Alex Aiezza
 *
 */
public class ConfigurationParser
{
    public Configuration parse( final String file ) throws FileNotFoundException
    {
        return parse( new File( file ) );
    }

    /**
     * 
     * @param file
     * @throws FileNotFoundException
     */
    public Configuration parse( final File file ) throws FileNotFoundException
    {
        final List<List<Character>> letters = new ArrayList<List<Character>>();

        try ( final Scanner sc = new Scanner( file ) )
        {
            while ( sc.hasNextLine() )
            {
                final StringTokenizer letterTokens = new StringTokenizer( sc.nextLine() );
                final List<Character> letterRow = new ArrayList<Character>();
                letters.add( letterRow );

                while ( letterTokens.hasMoreTokens() )
                    letterRow.add( letterTokens.nextToken().charAt( 0 ) );
            }
        }

        final Configuration config = new Configuration( letters.size(), letters.get( 0 ).size() );

        for ( int r = 0; r < config.getRows(); r++ )
            for ( int c = 0; c < config.getCols(); c++ )
                config.add( letters.get( c ).get( r ), c, r );

        return config;
    }

    public static void parseRow( final Configuration config, final int rowIndex, final String row )
    {
        final StringTokenizer letterTokens = new StringTokenizer( row );

        int col = 0;
        while ( letterTokens.hasMoreTokens() )
            config.add( letterTokens.nextToken().toUpperCase().charAt( 0 ), rowIndex, col++ );
    }

    public static void parse( final Configuration config, final String strConfig )
    {
        final AtomicInteger col = new AtomicInteger(), row = new AtomicInteger();
        strConfig.chars().map( Character::toUpperCase ).forEach( letter -> {
            if ( col.get() >= config.getCols() )
            {
                col.set( 0 );
                row.incrementAndGet();
            }

            config.add( (char) letter, row.get(), col.getAndIncrement() );
        } );

    }
}
