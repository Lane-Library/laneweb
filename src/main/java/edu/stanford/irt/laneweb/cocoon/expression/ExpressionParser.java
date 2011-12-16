package edu.stanford.irt.laneweb.cocoon.expression;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.irt.laneweb.LanewebException;

public class ExpressionParser {

    //TODO: don't throw IOException
    public List<Expression> parseExpression(final String expression) throws IOException {
        StringReader in = new StringReader(expression);
        LinkedList<Expression> expressions = new LinkedList<Expression>();
        StringBuffer buf = new StringBuffer();
        int ch;
        boolean inExpr = false;
        top: while ((ch = in.read()) != -1) {
            // column++;
            char c = (char) ch;
            processChar: while (true) {
                if (inExpr) {
                    if (c == '\\') {
                        ch = in.read();
                        //TODO: figure out what is happening with \ here:
                        buf.append(ch == -1 ? '\\' : (char) ch);
                    } else if (c == '}') {
                        expressions.add(new VariableExpression(buf.toString()));
                        buf.setLength(0);
                        inExpr = false;
                    } else {
                        buf.append(c);
                    }
                } else if (c == '{') {
                    ch = in.read();
                    if (ch != '{') {
                        inExpr = true;
                        if (buf.length() > 0) {
                            expressions.add(new LiteralExpression(buf.toString()));
                            buf.setLength(0);
                        }
                        buf.append((char) ch);
                        continue top;
                    }
                    buf.append(c);
                    if (ch != -1) {
                        c = (char) ch;
                        continue processChar;
                    }
                } else {
                    buf.append(c);
                }
                break;
            }
        }
        if (inExpr) {
            throw new LanewebException("Unterminated {");
        }
        if (buf.length() > 0) {
            expressions.add(new LiteralExpression(buf.toString()));
        }
        return expressions;
    }
}
