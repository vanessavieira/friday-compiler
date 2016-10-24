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
    private LinkedList<String> output = new LinkedList<String>();

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
            output.add("<program> ::= " + this.lookahead.getSequence() + " <program_aux>");
            this.nextToken();
            this.program_aux();
        } else {
            output.add("<program> ::= <function_list> <main>");
            this.function_list();
            this.main();
        }
    }

    private void program_aux() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_MAIN) {
            String prod = "<program_aux> ::= " + lookahead.getSequence() + " AB_PAR FEC_PAR <scope> ";
            this.nextToken();
            if (this.lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
                this.nextToken();
                if (this.lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                    output.add(prod);
                    this.nextToken();
                    this.scope();
                } else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");
                }
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            output.add("<program_aux> ::= <function_list> <main>");
            this.function_list();
            this.main();
        }
    }

    private void function_list() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_VOID) {
            output.add("<function_list> ::= " + lookahead.getSequence() + " <function_declaration> <function_list>");
            this.nextToken();
            this.function_declaration();
            this.function_list();
        } else if (this.lookahead.getTokenCategory() == Token.TokenCategory.TYPE_VALUE) {
            output.add("<function_list> ::= " + lookahead.getSequence() + " <function_declaration> <function_list>");
            this.nextToken();
            this.function_declaration();
            this.function_list();
        } else {
            output.add("<function_list> ::= empty");
        }
    }

    private void scope() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
            String prod = "<scope> ::= " + lookahead.getSequence() + " <commands> FEC_CH SP";
            nextToken();
            this.commands();
            if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                nextToken();
                if (lookahead.getTokenCategory() == Token.TokenCategory.SP) {
                    output.add(prod);
                    nextToken();
                }  else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");
                }
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void main() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.PR_VOID) {
            nextToken();
            if (lookahead.getTokenCategory() == Token.TokenCategory.PR_MAIN) {
                nextToken();
                if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
                    nextToken();
                    if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                        output.add("<main> ::= PR_VOID PR_MAIN AB_PAR FEC_PAR <scope>");
                        nextToken();
                        this.scope();
                    } else {
                        throw new ParserException("Unexpected symbol " + lookahead + " found!");
                    }
                } else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");
                }
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void function_declaration() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            nextToken();
            if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
                nextToken();
                this.parameters();
                if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                    nextToken();
                    this.scope();
                } else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");
                }
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void commands() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.TYPE_VALUE) {
            String prod = "<commands> ::= " + lookahead.getSequence() + " ID <declaration> <commands>";
            nextToken();
            if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
                output.add(prod);
                nextToken();
                this.declaration();
                this.commands();
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        }
    }

    private void declaration() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.SP) {
            output.add("<declaration> ::= SP");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.VECTOR_AUX) {
            String prod = "<declaration> ::= " + lookahead.getSequence() + " CTE_INT <declaration_aux>";
            nextToken();
            if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_INT) {
                output.add(prod);
                nextToken();
                this.declaration_aux();
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            this.attribution();
        }
    }

    private void declaration_aux() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.SP) {
            output.add("<declaration_aux> ::= SP");
            nextToken();
        } else {
            output.add("<declaration_aux> ::= <attribution>");
            this.attribution();
        }
    }

    private void attribution() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.OP_ATR) {
            nextToken();
            this.value();
            if (lookahead.getTokenCategory() == Token.TokenCategory.SP) {
                nextToken();
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void value() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
            output.add("<value> ::= AB_CH <array>");
            nextToken();
            this.array();
        } else {
            output.add("<value> ::= <expression>");
            this.expression();
        }
    }

    private void array() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
            output.add("<array> ::= FEC_CH");
            nextToken();
        } else {
            this.elements();
            if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                nextToken();
                output.add("<array> ::= <elements> FEC_CH");
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        }
    }

    private void elements() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            output.add("<elements> ::= ID");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_STR) {
            output.add("<elements> ::= CTE_STR");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_FLOAT) {
            output.add("<elements> ::= CTE_FLOAT");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_INT) {
            output.add("<elements> ::= CTE_INT");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.SP) {
            output.add("<elements> ::= SP <elements>");
            nextToken();
            this.elements();
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void parameters() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.TYPE_VALUE) {
            String prod = "<parameters>:: " + lookahead.getSequence() + " ID";
            nextToken();
            if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
                output.add(prod);
                nextToken();
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.SP) {
            String prod = "<parameters>:: " + lookahead.getSequence() + " TYPE_VALUE ID <parameters>";
            nextToken();
            if (lookahead.getTokenCategory() == Token.TokenCategory.TYPE_VALUE) {
                nextToken();
                if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
                    output.add(prod);
                    nextToken();
                    this.parameters();
                } else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");
                }
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            output.add("<parameters>:: empty");
        }
    }

    private void expression() {
        
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
