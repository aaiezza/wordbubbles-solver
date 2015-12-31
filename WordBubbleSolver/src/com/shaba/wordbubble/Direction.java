/**
 *  COPYRIGHT (C) 2015 Alex Aiezza. All Rights Reserved.
 * 
 * Licensed under the Geneopedia License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *  http://www.geneopedia.com/licenses/LICENSE-1.0 
 *  
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License. 
 */
package com.shaba.wordbubble;

import com.shaba.wordbubble.utils.Coordinate;

/**
 * @author Alex Aiezza
 *
 */
public enum Direction
{
    N( 0, -1 ), NE( 1, -1 ), E( 1, 0 ), SE( 1, 1 ), S( 0, 1 ), SW( -1, 1 ), W( -1, 0 ), NW( -1, -1 );

    private static final String OUT_OF_BOUNDS_EXCEPTION_FORMAT = "Can't go %s from [%2d,%2d]";

    /* * * */

    private final Coordinate    coordinate;

    private Direction( final int deltaX, final int deltaY )
    {
        coordinate = new Coordinate( deltaX, deltaY );
    }

    public Coordinate alter( final int x, final int y, final int xMax, final int yMax )
    {
        if ( !canGo( x, y, xMax, yMax ) )
            throw new ArrayIndexOutOfBoundsException( String.format(
                OUT_OF_BOUNDS_EXCEPTION_FORMAT, this.toString(), x, y ) );

        return new Coordinate( x + coordinate.col, y + coordinate.row );
    }

    public boolean canGo( final int x, final int y, final int xMax, final int yMax )
    {
        final int nX = x + coordinate.col, nY = y + coordinate.row;
        return ( nX >= 0 && nX < xMax && nY >= 0 && nY < yMax );
    }
}
