package org.ykc.bitpro;

public class XSToken {
    public char kind;        
    public long value;     
    public XSToken(char ch)
    {
        kind = ch;
        value = 0;
    }

    public XSToken(char ch, long val)
    {
        kind = ch;
        value = val;
    }
}
