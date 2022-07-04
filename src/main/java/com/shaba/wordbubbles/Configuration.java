package com.shaba.wordbubbles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shaba.wordbubbles.utils.Coordinate;

/**
 * @author Alex Aiezza
 * 
 *         Pretty much a matrix of characters or a blank
 *
 */
public class Configuration
{
    private final static int  DEFAULT_COLS = 3, DEFAULT_ROWS = 3;

    private final Letter [][] letters;

    public Configuration()
    {
        this( DEFAULT_COLS, DEFAULT_ROWS );
    }

    public Configuration( final int cols, final int rows )
    {
        letters = new Letter [rows] [cols];
        init();
    }

    private void init()
    {
        for ( int r = 0; r < getRows(); r++ )
            Arrays.fill( letters[r], Letter.BLANK );
    }

    public int getCols()
    {
        return letters[0].length;
    }

    public int getRows()
    {
        return letters.length;
    }

    public Letter getLetter( final int col, final int row )
    {
        return letters[row][col];
    }

    public Letter getLetter( final Coordinate dimension )
    {
        return letters[dimension.row][dimension.col];
    }

    public Configuration add( final Letter letter, final int row, final int col )
    {
        letters[row][col] = letter;

        // Process linked letters
        if ( !letter.equals( Letter.BLANK ) )
        {
            for ( final Direction d : Direction.values() )
            {
                try
                {
                    letter.link( getLetter( d.alter( col, row, getCols(), getRows() ) ) );
                } catch ( ArrayIndexOutOfBoundsException e )
                {}
            }
        }
        return this;
    }

    public Configuration add( final char letter, final int row, final int col )
    {
        return add( ( letter == Letter.BLANK_CHAR || letter == ' ' ) ? Letter.BLANK : new Letter(
                letter, row, col ), row, col );
    }

    public int numberOfLetters()
    {
        int nL = 0;
        for ( int r = 0; r < getRows(); r++ )
            for ( int c = 0; c < getCols(); c++ )
                nL += letters[r][c].equals( Letter.BLANK ) ? 0 : 1;

        return nL;
    }

    public List<Letter> getLetters()
    {
        final List<Letter> letterList = new ArrayList<Letter>();

        for ( int r = 0; r < getRows(); r++ )
            for ( int c = 0; c < getCols(); c++ )
                if ( !letters[r][c].equals( Letter.BLANK ) )
                    letterList.add( letters[r][c] );

        return letterList;
    }

    @Override
    public String toString()
    {
        final StringBuilder out = new StringBuilder();

        for ( int r = 0; r < getRows(); r++ )
        {
            for ( int c = 0; c < getCols(); c++ )
                out.append( " " ).append( letters[r][c].getLetterChar() );

            if ( r + 1 < getRows() )
                out.append( "\n" );
        }

        return out.toString();
    }
}
