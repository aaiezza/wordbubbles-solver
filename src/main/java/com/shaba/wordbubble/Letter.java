package com.shaba.wordbubble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shaba.wordbubble.utils.Coordinate;

/**
 * @author Alex Aiezza
 *
 */
public class Letter
{
    private static final String NOT_A_LETTER_EXCEPTION_FORMAT = "Character '%s' must be a letter [A-Z]";

    /* * * */

    static final char           BLANK_CHAR                    = '.';

    public static final Letter  BLANK                         = new Letter( BLANK_CHAR, -1, -1 )
                                                              {
                                                                  @Override
                                                                  public Letter link( Letter letter )
                                                                  {
                                                                      return this;
                                                                  }
                                                              };

    private final char          letter;

    private final List<Letter>  linkedLetters;

    private final Coordinate    coordinate;

    public Letter( final char letter, final int row, final int col )
    {
        if ( !Character.isLetter( letter ) && letter != BLANK_CHAR )
            throw new IllegalArgumentException( String.format( NOT_A_LETTER_EXCEPTION_FORMAT,
                letter ) );

        this.letter = Character.isLowerCase( letter ) ? Character.toUpperCase( letter ) : letter;
        linkedLetters = new ArrayList<Letter>( 3 );

        coordinate = new Coordinate( row, col );
    }

    public char getLetterChar()
    {
        return letter;
    }

    public String getLetter()
    {
        return letter + "";
    }

    public Letter link( final Letter letter )
    {
        if ( letter != BLANK && !linkedLetters.contains( letter ) )
        {
            linkedLetters.add( letter );
            letter.link( this );
        }
        return this;
    }

    public List<Letter> getLinkedLetters()
    {
        return Collections.unmodifiableList( linkedLetters );
    }

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public String print()
    {
        final StringBuilder out = new StringBuilder();

        out.append( String.format( "%s : [ ", letter ) );

        for ( int l = 0; l < linkedLetters.size(); l++ )
        {
            out.append( "\n  " ).append( linkedLetters.get( l ).letter );

            if ( l + 1 >= linkedLetters.size() )
                out.append( "\n" );
        }

        out.append( "]" );

        return out.toString();
    }

    @Override
    public String toString()
    {
        return letter + "";
    }
}
