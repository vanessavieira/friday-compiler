package Parser;

import Lexer.*;
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
        this.lookahead = this.tokens.getFirst();

        // first production
        program();

        if (lookahead.getTokenCategory() != Token.TokenCategory.EOF) {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void program() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_VOID) {
            this.nextToken();
            if (lookahead.getTokenCategory() == Token.TokenCategory.PR_MAIN) {
                this.nextToken();
                if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
                    
                }
            } else {
                function();
                main();
            }

        }


    }


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
