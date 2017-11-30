package Parser.Lexer;

public class Token {

    /**
     * Internal representation of Tokens.
     */
    public enum TokenCategory {

        EOF(0),
        ID(1),
        CTE_FLOAT(2),
        CTE_INT(3),
        CTE_CHAR(4),
        OP_ATR(5),
        OP_REL1(6),
        OP_REL2(7),
        OP_AD(8),
        OP_MULT(9),
        OP_AND(10),
        OP_OR(11),
        OP_NOT(12),
        COMMENT(13),
        VECTOR_AUX(14),
        PR_FUN(15),
        PR_FUNFUN(16),
        PR_FUNGLOBALDEC(17),
        PR_FUNINTERNDEC(18),
        PR_ANSWER(19),
        PR_IF(20),
        PR_ELSE(21),
        PR_WHILE(22),
        PR_REPEAT(23),
        PR_UNTIL(24),
        PR_IO_READ(25),
        TYPE_VALUE(26),
        BOOL_VALUE(27),
        SEP(28),
        AB_PAR(29),
        FEC_PAR(30),
        AB_CH(31),
        FEC_CH(32),
        AB_COL(33),
        FEC_COL(34),
        PR_VOID(35),
        PR_MAIN(36),
        OP_CONC(37),
        PR_IO_PRINT(38);

        private int value;

        TokenCategory(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
    }


    private TokenCategory tokenCategory;
    private String sequence;
    private int lin, col;

    /**
     * Construct the token representation within its values
     * @param tokenCategory Category Identifier int
     * @param sequence String that represents the token
     * @param lin Parser.Lexer.Token line in input
     * @param col Parser.Lexer.Token column in input
     */

    public Token(TokenCategory tokenCategory,
                 String sequence,
                 int lin,
                 int col) {

        this.tokenCategory = tokenCategory;
        this.sequence = sequence;
        this.lin = lin;
        this.col = col;
    }

    @Override
    public String toString() {
        return "(Sequence: " + sequence + ", Token Category: " + tokenCategory.getValue() + ", Position: (" + this.lin + ", " + this.col + "))\n";
    }

    public TokenCategory getTokenCategory() {
        return this.tokenCategory;
    }

    public String getSequence() {
        return this.sequence;
    }
}
