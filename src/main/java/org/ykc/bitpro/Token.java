package org.ykc.bitpro;

public class Token {
    public char kind;        
    public long value;     
    public Token(char ch)
    {
        kind = ch;
        value = 0;
    }

    public Token(char ch, long val)
    {
        kind = ch;
        value = val;
    }
}
