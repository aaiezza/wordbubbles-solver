package com.shaba.wordbubbles.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alex Aiezza
 * 
 *         Dictionary implemented using a Trie Tree.
 */
public class Dictionary
{
    private final HashMap<Character, Node> roots = new HashMap<Character, Node>();

    /**
     * Search through the dictionary for a word.
     * 
     * @param word
     *            The word to search for.
     * @return Whether or not the word exists in the dictionary.
     */
    public boolean search( final String word )
    {
        if ( roots.containsKey( word.charAt( 0 ) ) )
        {
            if ( word.length() == 1 && roots.get( word.charAt( 0 ) ).endOfWord )
                return true;
            else return searchFor( word.substring( 1 ), roots.get( word.charAt( 0 ) ) );
        }

        return false;
    }

    /**
     * Insert a word into the dictionary.
     * 
     * @param word
     *            The word to insert.
     */
    public void insert( final String word )
    {
        if ( !roots.containsKey( word.charAt( 0 ) ) )
            roots.put( word.charAt( 0 ), new Node() );

        insertWord( word.substring( 1 ), roots.get( word.charAt( 0 ) ) );
    }

    /**
     * Recursive method that inserts a new word into the trie tree.
     */
    private void insertWord( final String word, final Node node )
    {
        if ( word.isEmpty() )
            return;

        final Node nextChild;
        if ( node.children.containsKey( word.charAt( 0 ) ) )
        {
            nextChild = node.children.get( word.charAt( 0 ) );
        } else
        {
            nextChild = new Node();
            node.children.put( word.charAt( 0 ), nextChild );
        }

        if ( word.length() == 1 )
            nextChild.endOfWord = true;
        else insertWord( word.substring( 1 ), nextChild );
    }

    /**
     * Recursive method that searches through the Trie Tree to find the value.
     */
    private boolean searchFor( final String word, final Node node )
    {
        if ( word.length() <= 0 )
            return node.endOfWord;

        if ( node.children.containsKey( word.charAt( 0 ) ) )
            return searchFor( word.substring( 1 ), node.children.get( word.charAt( 0 ) ) );

        return false;
    }

    public int size()
    {
        final HashSet<String> words = new HashSet<String>();
        roots.forEach( ( letter, node ) -> populate( letter + "", node, words, 1 ) );
        return words.size();
    }

    /**
     * A node has children nodes mapped to characters and the character this
     * node is mapped to in it a parent node, is the character it represents and
     * can be in that setting, the end of a word.
     */
    class Node
    {
        boolean                  endOfWord = false;
        HashMap<Character, Node> children  = new HashMap<Character, Node>();
    }

    public Set<String> getWords()
    {
        final HashSet<String> words = new HashSet<String>();
        roots.forEach( ( letter, node ) -> populate( letter + "", node, words, 1 ) );
        return words;
    }

    private void populate(
            final String word,
            final Node node,
            final Set<String> words,
            final int depth )
    {
        if ( node.endOfWord )
            words.add( word );
        node.children
                .forEach( ( letter, nod ) -> populate( word + letter, nod, words, depth + 1 ) );
    }
}
