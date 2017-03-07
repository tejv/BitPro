package org.ykc.bitpro;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.controlsfx.control.StatusBar;

import com.jfoenix.controls.JFXTextField;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class XpressionSolver {
    private final char NUMBER = '8'; // let '8' represent "a NUMBER"
    private final char RIGHT_SHIFT = '7';
    private final char LEFT_SHIFT = '6';
    private boolean full = false;
    private Token buffer;
    private String x =  "";
    private String result = "";
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
                    type =  GUtils.getSubString(x, idx, 2);
                }

                if((type.equals("0x"))|| (type.equals("0X")))
                {
                    idx = idx + 2;
                    while ((idx < x.length()) && ((GUtils.isCharHex(x.charAt(idx)) == true)))
                    {
                        a = a + x.charAt(idx);
                        idx++;
                    }
                    val = GUtils.castInttoLong((GUtils.castLongtoUInt((Long.parseUnsignedLong(a, 16)))));

                }
                else if((type.equals("0b")) || (type.equals("0B")))
                {
                    idx = idx + 2;
                    while ((idx < x.length()) && ((GUtils.isCharBinary(x.charAt(idx)) == true)))
                    {
                        a = a + x.charAt(idx);
                        idx++;
                    }
                    val = GUtils.castInttoLong((GUtils.castLongtoUInt((Long.parseUnsignedLong(a, 2)))));
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
                    val = GUtils.castInttoLong((GUtils.castLongtoUInt((Long.parseUnsignedLong(a, 10)))));
                }
                return new Token(NUMBER, val);
            case '>':
                {
                    if ((idx + 2) < x.length())
                    {
                        String temp = GUtils.getSubString(x, idx, 2);
                        if (temp.equals(">>"))
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
                        String temp = GUtils.getSubString(x, idx, 2);
                        if (temp.equals("<<"))
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

	public static TextField getTextFieldByRowColumnIndex(final int row,final int column,GridPane gridPane) {
	    TextField result = null;
	    ObservableList<Node> childrens = gridPane.getChildren();
	    int i = 0;
	    int expected_value = row*8 + column;
	    for(Node node : childrens) {
	    	try {
				TextField textField = (TextField)node;
			} catch (Exception e) {
				i++;
				continue;
			}
	    	if(i==expected_value){
	    		result = (TextField)node;
	    	}
	    	i++;
	    }
	    return result;
	}
    
    public void solve(TextField input, TextField hex, TextField binary, TextField decimal, StatusBar statusBar, GridPane gPane){
    	statusBar.setText("");
    	if(x_solve(input.getText(), 0) == true)
    	{
    		Integer output= Integer.parseUnsignedInt(result);
			hex.setText("0x" + Integer.toHexString(output).toUpperCase());
			binary.setText("0b" + Integer.toBinaryString(output));
			decimal.setText(result);
			String bString = StringUtils.leftPad(Integer.toBinaryString(output), 32, '0');
			String hString = StringUtils.leftPad(Integer.toHexString(output), 8, '0');
			for(int j = 1; j < 3; j++){
				for(int i = 0; i < 8; i++){
					TextField txtField = getTextFieldByRowColumnIndex(j, i, gPane);
					if(j == 1){
						txtField.setText(GUtils.getSubString(bString, i*4, 4));
					}
					else{
						txtField.setText(GUtils.getSubString(hString, i, 1).toUpperCase());
					}
				}
			}

    	}
    	else{
    		hex.setText(result);
    		binary.setText(result);
    		decimal.setText(result);
    	}
    }

    private boolean x_solve(String input, Integer index)
    {
        try
        {
            x = input;
            full = false;
            idx = 0;
            result = Long.toString(expression());
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
            result = "Error in Parsing";
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
            	return GUtils.castInttoLong((-GUtils.castLongtoUInt(primary())));
            case '+':
                return primary();
            case '~':
            	return GUtils.castInttoLong((~GUtils.castLongtoUInt(primary())));
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
                    left = GUtils.castInttoLong(GUtils.castLongtoUInt((left * primary())));
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
                	left = GUtils.castInttoLong(GUtils.castLongtoUInt((left + term())));  // evaluate Term and add
                    t = getToken();
                    break;
                case '-':
                	left = GUtils.castInttoLong(GUtils.castLongtoUInt((left - term())));
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
                	left = GUtils.castInttoLong(GUtils.castLongtoUInt((left >> term())));
                    t = getToken();
                    break;
                case LEFT_SHIFT:
                	left = GUtils.castInttoLong(GUtils.castLongtoUInt((left << term())));
                    t = getToken();
                    break;
                default:
                    putbackToken(t);     // put t back into the token stream
                    return left;       // finally: no more + or -: return the answer
            }
        }
    }
	public void reverseSolve(TextField txtSolveEnter, TextField txtSolveShowHex, TextField txtSolveShowBinary,
			TextField txtSolveShowDecimal, StatusBar statusBar, GridPane gPaneXsolveTab, ActionEvent event) {
		String bString = "";
		try {
			for(Integer i = 0; i < 8; i++){
				TextField txtField = getTextFieldByRowColumnIndex(1, i, gPaneXsolveTab);
				String txtString = txtField.getText();
				if(txtString.length() > 4){
					statusBar.setText("Error: Max character exceeded in " + ((Integer)(i+1)).toString() + "th box");
					return;
				}
				bString += txtString;
			}
			
			txtSolveEnter.setText("0x" + Integer.toHexString(Integer.parseUnsignedInt(bString, 2)));
			txtSolveEnter.fireEvent(event);
		} catch (NumberFormatException e) {
			statusBar.setText("Error in parsing");
		}
		
		
		
	}


}
