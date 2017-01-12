package org.ykc.bitpro;

import org.apache.commons.lang3.math.NumberUtils;

public class XpressionSolver {
    private final char NUMBER = '8'; // let '8' represent "a NUMBER"
    private final char RIGHT_SHIFT = '7';
    private final char LEFT_SHIFT = '6';
    private boolean full = false;
    private Token buffer;
    private String x =  "";
    private int idx = 0;

    private Token  getToken() throws Exception
    {
        if (full)
        {       // do we already have a Token ready?
                // remove token from buffer
            full = false;
            return buffer;
        }
        if(idx >= x.length())
        {
            //error("Parse error");
            return new Token(' ');
        }
        char ch;
        /*Get a char from input text stream*/
        ch = x.charAt(idx); // note that >> skips whitespace (space, newline, tab, etc.)

        switch (ch)
        {
            case '(':
            case ')':
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
            case '~':
            case '|':
            case '&':
            case '^':
                idx++;
                return new Token(ch);        // let each character represent itself
            case ',':
                return new Token(' ');
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                String a = "";
                Long val = 0L;
                /*Check for hex/binary inputs*/
                String type = "";
                if ((idx + 2) < x.length())
                {
                    type =  GenericUtils.getSubString(x, idx, 2);
                }

                if((type == "0x")|| (type == "0X"))
                {
                    idx = idx + 2;
                    while ((idx < x.length()) && ((GenericUtils.is_char_hex(x.charAt(idx)) == true)))
                    {
                        a = a + x.charAt(idx);
                        idx++;
                    }
                    val = GenericUtils.getUnsignedInt(Integer.parseInt(a, 16));

                }
                else if(type == "0b" || (type == "0B"))
                {
                    idx = idx + 2;
                    while ((idx < x.length()) && ((GenericUtils.is_char_binary(x.charAt(idx)) == true)))
                    {
                        a = a + x.charAt(idx);
                        idx++;
                    }
                    val = GenericUtils.getUnsignedInt(Integer.parseInt(a, 2));
                }
                else
                {
                	a = "";
                    a += x.charAt(idx);
                    idx++;
                    while ((idx < x.length()) && ((NumberUtils.isNumber(Character.toString(x.charAt(idx))) == true)))
                    {
                        a = a + x.charAt(idx);
                        idx++;
                    }
                    val = GenericUtils.getUnsignedInt(Integer.parseInt(a, 10));
                }
                return new Token(NUMBER, val);
            case '>':
                {
                    if ((idx + 2) < x.length())
                    {
                        String temp = GenericUtils.getSubString(x, idx, 2);
                        if (temp == ">>")
                        {
                            idx = idx + 2;
                            return new Token(RIGHT_SHIFT, 0);
                        }
                    }
                    throw new Exception("Bad Token");
                }
            case '<':
                {
                    if ((idx + 2) < x.length())
                    {
                        String temp = GenericUtils.getSubString(x, idx, 2);
                        if (temp == "<<")
                        {
                            idx = idx + 2;
                            return new Token(LEFT_SHIFT, 0);
                        }
                    }
                    throw new Exception("Bad Token");
                }
            case ' ':
                idx++;
                return getToken();
            default:
            	throw new Exception("Bad Token");
        }
    }
    void putbackToken(Token t) throws Exception
    {
        if (full){
        	throw new Exception("putback() into a full buffer");
        }
        buffer = t;       // copy t to buffer
        full = true;      // buffer is now full
    }

    public boolean x_solve(String input, String output, Integer index)
    {
        try
        {
            x = input;
            full = false;
            idx = 0;
            output = Long.toString(expression());
            if(buffer.kind == ' ')
            {
                index = idx;
            }
            else
            {
                index = idx - 1;
            }
            return true;
        }
        catch (Exception e)
        {
            output = e.toString();
            return false;
        }
    }


    // deal with numbers and parentheses
    private Long primary() throws Exception
    {
        Token t = getToken();
        switch (t.kind)
        {
            case '(':    // handle '(' expression ')'
                {
                    Long d = expression();
                    t = getToken();
                    if (t.kind != ')'){
                    	throw new Exception("')' expected");
                    }
                    return d;
                }
            case NUMBER:           
                return t.value;  // return the NUMBER's value
            case '-':
            	return GenericUtils.getUnsignedInt((-GenericUtils.getUsignedIntFromLong(primary())));
            case '+':
                return primary();
            case '~':
            	return GenericUtils.getUnsignedInt((~GenericUtils.getUsignedIntFromLong(primary())));
            default:
                throw new Exception("primary expected");
        }
    }

    //------------------------------------------------------------------------------

    // deal with *, /, and %
    Long term() throws Exception
    {
        Long left = primary();
        Token t = getToken();        // get the next token from token stream

        while (true)
        {
            switch (t.kind)
            {
                case '*':
                    left = GenericUtils.getUnsignedInt(GenericUtils.getUsignedIntFromLong((left * primary())));
                    t = getToken();
                    break;
                case '/':
                    {
                        Long d = primary();
                        if (d == 0){
                        	throw new Exception("divide by zero");
                        }
                        left /= d;
                        t = getToken();
                        break;
                    }
                case '%':
                    {
                        Long d = term();
                        return (left % d);
                    }
                default:
                    putbackToken(t);     // put t back into the token stream
                    return left;
            }
        }
    }

    // deal with + and -
    Long expression() throws Exception
    {
        Long left = term();      // read and evaluate a Term
        Token t = getToken();        // get the next token from token stream

        while (true)
        {
            switch (t.kind)
            {
                case '+':
                	left = GenericUtils.getUnsignedInt(GenericUtils.getUsignedIntFromLong((left + term())));  // evaluate Term and add   
                    t = getToken();
                    break;
                case '-':
                	left = (long) GenericUtils.getUsignedIntFromLong((left - term()));
                    t = getToken();
                    break;
                case '|':
                    left |= term();
                    t = getToken();
                    break;
                case '&':
                    left &= term();
                    t = getToken();
                    break;
                case '^':
                    left ^= term();
                    t = getToken();
                    break;
                case RIGHT_SHIFT:
                	left = GenericUtils.getUnsignedInt(GenericUtils.getUsignedIntFromLong((left >> term())));
                    t = getToken();
                    break;
                case LEFT_SHIFT:
                	left = GenericUtils.getUnsignedInt(GenericUtils.getUsignedIntFromLong((left << term())));
                    t = getToken();
                    break;
                default:
                    putbackToken(t);     // put t back into the token stream
                    return left;       // finally: no more + or -: return the answer
            }
        }
    }
    
    
}
