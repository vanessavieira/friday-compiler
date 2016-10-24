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
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            String prod = "<commands> ::= " + lookahead.getSequence() + " <attribution_or_function_call> <commands>";
            nextToken();
            this.attribution_or_function_call();
            this.commands();
        }
    }

    private void attribution_or_function_call() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.VECTOR_AUX) {
            String prod = "<attribution_or_function_call> ::= " + lookahead.getSequence();
            nextToken();
            if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_INT) {
                prod += "CTE_INT <attribution>";
                output.add(prod);
                nextToken();
                this.attribution();
            } else if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
                prod += "ID <attribution>";
                output.add(prod);
                nextToken();
                this.attribution();
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
            String prod = "<attribution_or_function_call> ::= " + lookahead.getSequence() + " <parameters_call> FEC_PAR SP";
            nextToken();
            this.parameters_call();
            if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                nextToken();
                if (lookahead.getTokenCategory() == Token.TokenCategory.SP) {
                    output.add(prod);
                    nextToken();
                } else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");
                }
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            output.add("<attribution_or_function_call> ::= <attribution>");
            this.attribution();
        }
    }

    private void parameters_call() {
        if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            output.add("<parameters_call> ::= ID");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_STR) {
            output.add("<parameters_call> ::= CTE_STR");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_FLOAT) {
            output.add("<parameters_call> ::= CTE_FLOAT");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_INT) {
            output.add("<parameters_call> ::= CTE_INT");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.SP) {
            output.add("<parameters_call> ::= SP <parameters_call>");
            nextToken();
            this.parameters_call();
        } else {
            output.add("<parameters_call> ::= empty");
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
            } else if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
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

    private void expression() throws ParserException {
        output.add("<expression>:: <eq_expression> <expression_aux>");
        this.eq_expression();
        this.expression_aux();
    }

    private void expression_aux() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.OP_AND) {
            output.add("<expression_aux>:: OP_AND <expression>");
            nextToken();
            this.expression();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.OP_OR) {
            output.add("<expression_aux>:: OP_OR <expression>");
            nextToken();
            this.expression();
        } else {
            output.add("<expression_aux>:: empty");
        }
    }

    private void eq_expression() throws ParserException {
        output.add("<eq_expression>:: <comparative_exp> <eq_expression_aux>");
        this.comparative_exp();
        this.eq_expression_aux();
    }

    private void eq_expression_aux() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.OP_REL2) {
            output.add("<eq_expression_aux>:: OP_REL2 <eq_expression>");
            nextToken();
            this.eq_expression();
        } else {
            output.add("<eq_expression_aux>:: empty");
        }
    }

    private void comparative_exp() throws ParserException {
        output.add("<comparative_exp>:: <add_exp> <comparative_exp_aux>");
        this.add_exp();
        this.comparative_exp_aux();
    }

    private void comparative_exp_aux() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.OP_REL1) {
            output.add("<comparative_exp_aux>:: OP_REL1 <comparative_exp>");
            nextToken();
            this.comparative_exp();
        } else {
            output.add("<comparative_exp_aux>:: empty");
        }
    }

    private void add_exp() throws ParserException {
        output.add("<add_exp>:: <mult_exp> <add_exp_aux>");
        this.mult_exp();
        this.add_exp_aux();
    }

    private void add_exp_aux() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.OP_AD) {
            output.add("<add_exp_aux>:: OP_AD <add_exp>");
            nextToken();
            this.add_exp();
        } else {
            output.add("<add_exp_aux>:: empty");
        }
    }

    private void mult_exp() throws ParserException {
        output.add("<mult_exp>:: <neg_exp> <mult_exp_aux>");
        this.neg_exp();
        this.mult_exp_aux();
    }

    private void mult_exp_aux() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.OP_MULT) {
            output.add("<mult_exp_aux>:: OP_MULT <mult_exp>");
            nextToken();
            this.mult_exp();
        } else {
            output.add("<mult_exp_aux>:: empty");
        }
    }

    private void neg_exp() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.OP_NOT) {
            output.add("<neg_exp>:: OP_NOT <exp_aux>");
            nextToken();
            this.exp_aux();
        } else {
            output.add("<neg_exp>:: <exp_aux>");
            this.exp_aux();
        }
    }

    private void exp_aux() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
            nextToken();
            this.atom_exp();
            if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                output.add("<exp_aux>:: AB_PAR <atom_exp> FEC_PAR");
                nextToken();
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            this.atom_exp();
        }
    }

    private void atom_exp() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            output.add("<atom_exp> ::= ID");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_STR) {
            output.add("<atom_exp> ::= CTE_STR");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_FLOAT) {
            output.add("<atom_exp> ::= CTE_FLOAT");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_INT) {
            output.add("<atom_exp> ::= CTE_INT");
            nextToken();
        } else if (lookahead.getTokenCategory() == Token.TokenCategory.BOOL_VALUE) {
            output.add("<atom_exp> ::= BOOL_VALUE");
            nextToken();
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
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
