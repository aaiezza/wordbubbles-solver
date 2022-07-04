package com.shaba.wordbubble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Solution implements Cloneable
{
    private final Set<String>        solution;

    private final Configuration      configuration;

    private final List<List<String>> guide;

    public Solution( final Configuration configuration )
    {
        this.configuration = configuration;

        solution = new LinkedHashSet<String>();

        guide = new ArrayList<List<String>>( configuration.getRows() );

        IntStream.range( 0, configuration.getRows() ).forEach( i -> {
            final List<String> row = new ArrayList<String>( configuration.getCols() );
            IntStream.range( 0, configuration.getCols() ).forEach( j -> row.add( "" ) );
            guide.add( row );
        } );
    }

    private Solution(
        final Configuration configuration,
        final Set<String> solution,
        final List<List<String>> guide )
    {
        this.configuration = configuration;
        this.solution = solution;
        this.guide = guide;
    }

    public Set<String> getSolution()
    {
        return solution;
    }

    public boolean addWord( final String word, final List<Letter> letters )
    {
        if ( !solution.add( word ) )
            return false;

        final String id = ( (char) ( size() - 1 + 'A' ) ) + "";

        IntStream.range( 0, letters.size() ).forEach(
            i -> {
                guide.get( letters.get( i ).getCoordinate().col ).set(
                    letters.get( i ).getCoordinate().row, id + ( i + 1 ) );
            } );

        return true;
    }

    public boolean removeWord( final String word )
    {
        return solution.remove( word );
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public List<List<String>> getGuide()
    {
        return guide;
    }

    public int size()
    {
        return solution.size();
    }

    public boolean contains( final String word )
    {
        return solution.contains( word );
    }

    public boolean containsAll( final Collection<String> words )
    {
        return solution.containsAll( words );
    }

    public String printGuide()
    {
        final StringBuilder out = new StringBuilder();

        for ( int r = 0; r < configuration.getRows(); r++ )
        {
            for ( int c = 0; c < configuration.getCols(); c++ )
                out.append( String.format( "%4s", guide.get( r ).get( c ) ) );

            if ( r + 1 < configuration.getRows() )
                out.append( "\n" );
        }

        return out.toString();
    }


    @Override
    public boolean equals( final Object obj )
    {
        if ( ! ( obj instanceof Solution ) )
            return false;

        return solution.equals( ( (Solution) obj ).solution );
    }

    @Override
    public int hashCode()
    {
        return solution.hashCode();
    }

    @Override
    public String toString()
    {
        return solution.toString();
    }

    @Override
    protected Solution clone()
    {
        final List<List<String>> guideClone = new ArrayList<List<String>>();

        for ( int i = 0; i < guide.size(); i++ )
            guideClone.add( new ArrayList<String>( guide.get( i ) ) );

        final Solution clSol = new Solution( configuration, new LinkedHashSet<String>( solution ),
                guideClone );

        return clSol;
    }
}
