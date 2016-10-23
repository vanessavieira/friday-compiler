package Parser;

import Lexer.Lexer;
import Lexer.Token;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rubenspessoa on 18/10/16.
 */
public class Parser {

    private LinkedList<Token> tokens;
    private Token lookahead;
    private LinkedList<String> output;

    public LinkedList<String> parse(File file) throws Exception {
        Lexer lexer = Lexer.getLexer();
        lexer.lex(file);
        parse(lexer.getTokens());
        return this.output;
    }

    private void parse(List<Token> tokens) throws ParserException {
        this.tokens = (LinkedList<Token>) tokens;
        lookahead = this.tokens.getFirst();

        // first production
        //program();

        if (lookahead.getTokenCategory() != Token.TokenCategory.EOF) {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    /*
    private void program() throws ParserException {
        output.add("<program>: <function> <main>");
        function();
        main(); // not implemented
    }

    public void function() throws ParserException {
        output.add("<function>: <function_declaration> | empty");
        functionDeclaration();
    }

    public void functionDeclaration() throws ParserException {
        // TODO: Output
        String functionDeclaration = "";

        // functionDeclarations -> type ID AB_PAR parameters FEC_PAR scope SP1
        type();

        if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            nextToken();
        }

        if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
            nextToken();
            parameters();

            if (lookahead.getTokenCategory() != Token.TokenCategory.FEC_PAR) {
                throw new ParserException("Closing brackets expected and " + lookahead + " found instead.");
            }

            nextToken();
        }

        scope();

        if (lookahead.getTokenCategory() == Token.TokenCategory.SP1) {
            nextToken();
        }
    }

    public void parameters() {

        type();

        if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            nextToken();
        }

        parameters_aux1();
    }

    public void parameters_aux1() {

        if (lookahead.getTokenCategory() == Token.TokenCategory.SP2) {
            nextToken();
        }

        parameters();
    }

    public void scope() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
            nextToken();

            commands();

            if (lookahead.getTokenCategory() != Token.TokenCategory.FEC_CH) {
                if (lookahead.getTokenCategory() != Token.TokenCategory.FEC_PAR) {
                    throw new ParserException("Closing brackets expected and " + lookahead + " found instead.");
                }
            }
        }
    }

    public void commands() {
        cmd();

        if (lookahead.getTokenCategory() == Token.TokenCategory.SP1) {
            nextToken();
        }

        commands();

    }

    public void type() {
        if (lookahead.getTokenCategory() == Token.TokenCategory.PR_INT)
            nextToken();
        else if (lookahead.getTokenCategory() == Token.TokenCategory.PR_FLOAT)
            nextToken();
        else if (lookahead.getTokenCategory() == Token.TokenCategory.PR_BOOL)
            nextToken();
        else if (lookahead.getTokenCategory() == Token.TokenCategory.PR_STRING)
            nextToken();
        else if (lookahead.getTokenCategory() == Token.TokenCategory.VOID)
            nextToken();
    }

    public void main() {
        // TODO
    } */


    /**
     * Remove the first token from the list and store the next token in lookahead
     */

    private void nextToken() {
        this.tokens.removeFirst();

        if ( tokens.isEmpty() ) {
            lookahead = new Token( Token.TokenCategory.EOF, "", -1, -1);
        } else {
            lookahead = tokens.getFirst();
        }
    }


}
