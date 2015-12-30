package com.shaba.wordbubble.utils;

import java.util.HashMap;

/**
 * 
 * @author Alex Aiezza
 *
 */
public class Node
{
    Node                     parent;
    boolean                  endOfWord = false;
    HashMap<Character, Node> children  = new HashMap<Character, Node>();
}
