package Parser;

import Parser.Lexer.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

//("+ lookahead.getSequence() +")

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
        int finish = S();


        if (lookahead.getTokenCategory() != Token.TokenCategory.EOF) {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private int S() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_FUNGLOBALDEC ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.PR_FUN ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.PR_FUNFUN) {

            output.add("S = GlobalDecl GlobalFunctionDec LFunctionImpl");
            this.GlobalDecl();

        } else {
            output.add("S = epsilon");
            return 1;
        }
        return 0;
    }

    private void GlobalDecl() throws ParserException {

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_FUNGLOBALDEC) {
            output.add("GlobalDecl = 'FunGlobalDec' '{' Decl '}' Atrib ");
            this.nextToken();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
                this.nextToken();
                this.LDecl();

                if (this.lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                    this.nextToken();
                    this.LAtrib();
                    this.GlobalFunctionDec();

                } else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");
                }

            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }

        } else {
            output.add("GlobalDecl = epsilon");

            this.GlobalFunctionDec();
        }

    }

    private void GlobalFunctionDec() throws ParserException {

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_FUNFUN) {
            output.add("GlobalFunctionDec = 'FunFun' '{' LDeclFun '}'");
            this.nextToken();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
                this.nextToken();
                this.LDeclFun();

                if (this.lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                    this.nextToken();
                    this.LFunctionImpl();

                } else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");
                }

            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }

        } else {
            output.add("GlobalFunctionDec = epsilon");

            this.LFunctionImpl();
        }

    }

    private void LDeclFun() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_FUN) {
            output.add("LDeclFun = DeclFun LDeclFun");

            DeclFun();

        } else {
            output.add("LDeclFun = epsilon");
        }

    }

    private void DeclFun() throws ParserException {

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_FUN) {
            output.add("DeclFun = 'Fun' FunctionType 'id' AbrFecPar ';' DeclFun");

            this.nextToken();
            this.FunctionType();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.ID) {
                this.nextToken();
                this.AbrFecPar();

                if (this.lookahead.getTokenCategory() == Token.TokenCategory.SEP) {
                    this.nextToken();
                    this.LDeclFun();

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

    private void FunctionType() throws ParserException {
        output.add("FunctionType = '" + this.lookahead.getSequence() + "'");
        output.add("    FunctionType = "+ this.lookahead +" ");

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.TYPE_VALUE || this.lookahead.getTokenCategory() == Token.TokenCategory.PR_VOID) {
            this.nextToken();

        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }

    }

    private void LFunctionImpl() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_FUN) {
            output.add("LFunctionImpl = 'Fun' FunctionType MainFunctionImpl LFunctionImpl");

            nextToken();
            this.FunctionType();

            this.MainFunctionImpl();

        } else {
            output.add("LFunctionImpl = epsilon");
        }
    }

    private void MainFunctionImpl() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_MAIN) {
            output.add("MainFunctionImpl = Main");
            this.Main();

        } else {
            output.add("MainFunctionImpl = FunctionImpl");
            this.FunctionImpl();
        }
    }

    private void FunctionImpl() throws ParserException {

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            output.add("DeclFun = 'Fun' FunctionType 'id' AbrFecPar ';' DeclFun");

            this.nextToken();
            this.AbrFecPar();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
                this.nextToken();
                this.InternalDecl();
                this.LAtrib();
                this.Instruction();

                if (this.lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                    this.nextToken();
                    this.LFunctionImpl();

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

    private void Main() throws ParserException {

        output.add("Main = 'Fun' 'Integer' 'Main' AbrFecPar '{' InternalDecl Instruction '}' ");

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_MAIN) {
            this.nextToken();
            this.AbrFecPar();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
                this.nextToken();
                this.InternalDecl();
                this.LAtrib();
                this.Instruction();

                if (this.lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                    this.nextToken();
                    this.S();
                }

            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void AbrFecPar() throws ParserException {

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
            output.add("AbrFecPar = '(' Parameter ')' ");

            this.nextToken();
            this.Parameter();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                this.nextToken();

            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }

        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void Parameter() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.SEP) {
            this.nextToken();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.TYPE_VALUE) {
                output.add("Parameter = ',' VarType 'id' Parameter");

                this.VarType();

                if (this.lookahead.getTokenCategory() == Token.TokenCategory.ID) {
                    this.nextToken();
                    this.Parameter();
                }
            } else {
                output.add("Parameter = ',' Es Parameter");

                this.nextToken();
                this.Es();
            }

        } else if (this.lookahead.getTokenCategory() == Token.TokenCategory.TYPE_VALUE) {
            output.add("Parameter = VarType 'id' Parameter");

            this.VarType();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.ID) {
                this.Id();
                this.Parameter();

            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }

        } else if (this.lookahead.getTokenCategory() == Token.TokenCategory.OP_NOT ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.BOOL_VALUE ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.ID ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.CTE_INT ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.CTE_FLOAT ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.CTE_CHAR) {

            output.add("Parameter = Es Parameter");

            this.nextToken();
            this.Es();

        } else {
            output.add("Parameter = epsilon");
        }
    }

    private void InternalDecl() throws ParserException {
        output.add("InternalDecl = 'FunInternDec' '{' LDecl '}'");

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.PR_FUNINTERNDEC) {
            this.nextToken();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
                this.nextToken();
                this.LDecl();

                if (this.lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                    this.nextToken();
                }
            }
        }
    }

    private void VarType() throws ParserException {
        output.add("VarType = '" + this.lookahead.getSequence() + "'");
        output.add("    VarType = "+ this.lookahead +" ");

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.TYPE_VALUE) {
            this.nextToken();

        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void LDecl() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.TYPE_VALUE) {
            output.add("LDecl = Decl LDecl");

            this.Decl();

        } else {
            output.add("LDecl = epsilon");

        }
    }

    private void Decl() throws ParserException {
        output.add("Decl = VarType Id ';' ");
        this.VarType();

        this.Id();

        if (this.lookahead.getTokenCategory() == Token.TokenCategory.SEP) {
            this.nextToken();
            this.LDecl();
        }
    }

    private void Id() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            output.add("Id = 'id' VectorType");
            output.add("    'id' = "+ this.lookahead +" ");

            this.nextToken();
            this.VectorType();

        } else if (this.lookahead.getTokenCategory() == Token.TokenCategory.AB_COL) {
            this.VectorType();

        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void VectorType() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.AB_COL) {
            output.add("VectorType = '[' Es ']' ");
            this.nextToken();
            this.Es();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.FEC_COL) {
                this.nextToken();
            }
        } else {
            output.add("VectorType = epsilon");

        }
    }

    private void LAtrib() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.ID ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.OP_ATR ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.AB_COL) {
            output.add("LAtrib = Atrib LAtrib");

            this.Atrib();

        } else {
            output.add("LAtrib = epsilon");

        }
    }

    private void Atrib() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.OP_ATR) {
            output.add("Atrib = '=' Es ';' ");

            this.nextToken();
            this.Eb();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.SEP) {
                this.nextToken();
                this.LAtrib();

            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }

        } else {
            output.add("Atrib = Id '=' Es ';' ");

            this.Id();

            if (this.lookahead.getTokenCategory() == Token.TokenCategory.OP_ATR) {
                this.nextToken();
                this.Eb();

                if (this.lookahead.getTokenCategory() == Token.TokenCategory.SEP) {
                    this.nextToken();
                    this.LAtrib();

                } else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");

                }
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        }

    }

    private void Instruction() throws ParserException {
        if (this.lookahead.getTokenCategory() == Token.TokenCategory.ID ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.PR_IF ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.PR_WHILE ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.PR_REPEAT ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.PR_ANSWER ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.PR_IO_PRINT ||
                this.lookahead.getTokenCategory() == Token.TokenCategory.PR_IO_READ) {

            output.add("Instruction = Command Instruction");
            this.Command();

        } else {
            output.add("Instruction = epsilon");

        }
    }


    private void Command() throws ParserException {

        if (lookahead.getTokenCategory() == Token.TokenCategory.OP_ATR || lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            output.add("Command = FuncAtrib ");

            if (lookahead.getTokenCategory() == Token.TokenCategory.OP_ATR) {
                output.add("FuncAtrib = LAtrib ");
                this.LAtrib();
                this.Instruction();

            } else {
                //this.Id();
                this.nextToken();

                if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
                    output.add("FuncAtrib = FunctionCall ");
                    this.FunctionCall();
                    this.Instruction();

                } else {
                    output.add("FuncAtrib = LAtrib ");
                    this.LAtrib();
                    this.Instruction();
                }
            }

        } else if (lookahead.getTokenCategory() == Token.TokenCategory.PR_IO_READ) {
            output.add("Command = " + lookahead.getSequence() + " ");

            nextToken();

            if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
                nextToken();

                this.Read();
                this.Instruction();
            }

        } else if (lookahead.getTokenCategory() == Token.TokenCategory.PR_IO_PRINT) {
            output.add("Command = " + lookahead.getSequence() + " ");

            nextToken();

            if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
                nextToken();

                this.Print();
                this.Instruction();
            }

        } else if (lookahead.getTokenCategory() == Token.TokenCategory.PR_IF) {
            output.add("Command = If");

            this.If();
            this.Instruction();

        } else if (lookahead.getTokenCategory() == Token.TokenCategory.PR_WHILE) {
            output.add("Command = While");

            this.While();
            this.Instruction();

        } else if (lookahead.getTokenCategory() == Token.TokenCategory.PR_REPEAT) {
            output.add("Command = RepeatUntil");

            this.RepeatUntil();
            this.Instruction();


        } else if (lookahead.getTokenCategory() == Token.TokenCategory.PR_ANSWER) {
            output.add("Command = Answer");

            this.Answer();
            this.Instruction();

        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void FunctionCall() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
            output.add("FunctionCall = 'id' AbrFecPar ';' ");

            this.AbrFecPar();

            if (lookahead.getTokenCategory() == Token.TokenCategory.SEP) {
                this.nextToken();
            }
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void If() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.PR_IF) {
            output.add("If = 'If' '(' Eb ')' '{' Instruction '}' Else");

            this.nextToken();

            if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
                this.nextToken();
                this.Eb();

                if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                    this.nextToken();

                    if (lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
                        this.nextToken();
                        this.Instruction();

                        if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                            this.nextToken();
                            this.Else();

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
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void Else() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.PR_ELSE) {
            output.add("Else = 'Else' '{' Instruction '}'");

            this.nextToken();

            if (lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
                this.nextToken();
                this.Instruction();

                if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                    this.nextToken();
                }
            }

        } else {
            output.add("Else = epsilon");
        }
    }

    private void While() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.PR_WHILE) {
            output.add("While = 'While' '(' Eb ')' '{' Instruction '}'");

            this.nextToken();

            if (lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
                this.nextToken();
                this.Eb();

                if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                    this.nextToken();

                    if (lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
                        this.nextToken();
                        this.Instruction();

                        if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                            this.nextToken();

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
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }

    private void Print() throws ParserException {
        output.add("Print = 'Print' '(' Es ')' ';' ");
        this.Es();

        if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
            nextToken();

            if (lookahead.getTokenCategory() == Token.TokenCategory.SEP) {
                nextToken();

            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        } else {
            throw new ParserException("Unexpected symbol " + lookahead + " found!");
        }
    }




    private void Read() throws ParserException {
        if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            output.add("Read = 'Read' '(' Id ')' ';' ");

            Id();
            this.Es();

            if (lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                nextToken();

                if (lookahead.getTokenCategory() == Token.TokenCategory.SEP) {
                    nextToken();

                } else {
                    throw new ParserException("Unexpected symbol " + lookahead + " found!");
                }
            } else {
                throw new ParserException("Unexpected symbol " + lookahead + " found!");
            }
        }
    }


    private void Answer() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.PR_ANSWER) {
            output.add("Answer = 'Answer' Es ';' ");

            this.nextToken();
            this.Es();

            if(lookahead.getTokenCategory() == Token.TokenCategory.SEP) {
                this.nextToken();
            }

        }
    }

    private void RepeatUntil() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.PR_REPEAT) {
            output.add("RepeatUntil = 'Repeat' 'id' '=' Ea 'Until' Ea '{' Instruction '}' ");
            this.nextToken();

            if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
                this.nextToken();

                if (lookahead.getTokenCategory() == Token.TokenCategory.OP_ATR) {
                    this.nextToken();
                    this.Ea();

                    if (lookahead.getTokenCategory() == Token.TokenCategory.PR_UNTIL) {
                        this.nextToken();
                        this.Ea();

                        if(lookahead.getTokenCategory() == Token.TokenCategory.AB_CH) {
                            this.nextToken();
                            this.Instruction();

                            if(lookahead.getTokenCategory() == Token.TokenCategory.FEC_CH) {
                                this.nextToken();
                            }
                        }
                    }
                }
            }
        }

    }

    private void Es() throws ParserException {
        output.add("Es = Eb Esr");

        this.Eb();
        this.Esr();
    }

    private void Esr() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.OP_CONC) {
            output.add("Esr = '++' Eb Esr");
            this.nextToken();
            this.Eb();

        } else {
            output.add("Esr = epsilon");
        }
    }

    private void Eb() throws ParserException {
        output.add("Eb = Tb Ebr");

        this.Tb();
        this.Ebr();
    }

    private void Ebr() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.OP_OR) {
            output.add("Ebr = '||' Tb Ebr");
            this.nextToken();
            this.Tb();

        } else {
            output.add("Ebr = epsilon");
        }
    }

    private void Tb() throws ParserException {
        output.add("Tb = Fb Tbr");

        this.Fb();
        this.Tbr();
    }

    private void Tbr() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.OP_AND) {
            output.add("Tbr = '&&' Fb Tbr");
            this.nextToken();
            this.Fb();

        } else {
            output.add("Tbr = epsilon");
        }
    }

    private void Fb() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.OP_NOT) {
            output.add("Fb = '!' Ei");
            this.nextToken();
            this.Ei();

        } else {
            output.add("Fb = Ei");
            this.Ei();
        }
    }

    private void Ei() throws ParserException {
        output.add("Ei = Er Opig Er");
        this.Er();

        if (lookahead.getTokenCategory() == Token.TokenCategory.OP_REL2) {
            nextToken();
            this.Er();

        } else {
            this.Er();
        }
    }

    private void Er() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.BOOL_VALUE) {
            output.add("Er = 'cteBool' ");
            nextToken();

        } else {
            this.Ea();

            if (lookahead.getTokenCategory() == Token.TokenCategory.OP_REL1) {
                output.add("Er = Ea Oprel Ea");
                nextToken();
                this.Ea();

            } else {
                output.add("Er = Ea");
            }
        }
    }

    private void Ea() throws ParserException {
        output.add("Ea = Ta Ear");

        this.Ta();
        this.Ear();
    }

    private void Ear() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.OP_AD) {
            output.add("Ear = Opa Ta Ear");
            output.add("    'Opa = "+ this.lookahead +" ");
            this.nextToken();
            this.Ta();

        } else {
            output.add("Ear = epsilon");
        }
    }

    private void Ta() throws ParserException {
        output.add("Ta = Fa Tar");

        this.Fa();
        this.Tar();
    }

    private void Tar() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.OP_MULT) {
            output.add("Tar = Opm Fa Tar");
            output.add("    'Opm = "+ this.lookahead +" ");
            this.nextToken();
            this.Fa();

        } else {
            output.add("Tar = epsilon");
        }
    }

    private void Fa() throws ParserException {
        if(lookahead.getTokenCategory() == Token.TokenCategory.AB_PAR) {
            output.add("Fa = '(' Ea ')' ");
            this.nextToken();

            this.Ea();

            if(lookahead.getTokenCategory() == Token.TokenCategory.FEC_PAR) {
                this.nextToken();
            }

        } else if (lookahead.getTokenCategory() == Token.TokenCategory.ID) {
            output.add("Fa = Id");
            this.Id();

        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_INT) {
            output.add("Fa = 'cteInt' ");
            output.add("    cteInt = "+ this.lookahead +" ");
            this.nextToken();

        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_FLOAT) {
            output.add("Fa = 'cteFloat' ");
            output.add("    cteFloat = "+ this.lookahead +" ");
            this.nextToken();

        } else if (lookahead.getTokenCategory() == Token.TokenCategory.CTE_CHAR) {
            output.add("Fa = 'cteCharArray' ");
            output.add("    cteCharArray = "+ this.lookahead +" ");
            this.nextToken();
        } else {
            output.add("Fa = epsilon ");
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
            if (tokens.getFirst().getTokenCategory() == Token.TokenCategory.COMMENT) {
                nextToken();
            } else {
                lookahead = tokens.getFirst();
            }
        }
    }
}
