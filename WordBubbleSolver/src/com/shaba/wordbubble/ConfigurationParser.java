package com.shaba.wordbubble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.media.sound.InvalidFormatException;

/**
 * @author Alex Aiezza
 *
 */
public class ConfigurationParser
{
    public FileConfigurationType DEFAULT_FILE_CONGIFURATION_TYPE = FileConfigurationType.FLAT;

    public Configuration parse( final String file ) throws FileNotFoundException,
            InvalidFormatException
    {
        return parse( file, DEFAULT_FILE_CONGIFURATION_TYPE );
    }

    public Configuration parse( final String file, final FileConfigurationType type )
            throws FileNotFoundException, InvalidFormatException
    {
        return parse( new File( file ), type );
    }

    public Configuration parse( final File file ) throws FileNotFoundException,
            InvalidFormatException
    {
        return parse( file, DEFAULT_FILE_CONGIFURATION_TYPE );
    }

    /**
     * 
     * @param file
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     * @throws Exception
     */
    public Configuration parse( final File file, final FileConfigurationType type )
            throws FileNotFoundException, InvalidFormatException
    {
        if ( type == FileConfigurationType.TABLE )
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
        } else if ( type == FileConfigurationType.FLAT )
        {
            try ( final Scanner sc = new Scanner( file ) )
            {
                String input = sc.nextLine().trim();

                final String [] rowsCols = input.split( " " );
                try
                {
                    final int rows = Integer.parseInt( rowsCols[0] );
                    final int cols = Integer.parseInt( rowsCols[1] );

                    final Configuration config = new Configuration( cols, rows );

                    input = sc.nextLine().trim();
                    ConfigurationParser.parse( config, input );

                    return config;
                } catch ( final NumberFormatException | ArrayIndexOutOfBoundsException e )
                {
                    throw new InvalidFormatException(
                            "Flat file no good. Need to start with number of cols and rows!" );
                }
            }
        } else return null;
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

    public enum FileConfigurationType
    {
        TABLE, FLAT;
    }
}
