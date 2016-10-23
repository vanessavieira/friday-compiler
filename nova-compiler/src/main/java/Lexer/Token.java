package Lexer;

/**
 * Created by rubenspessoa on 04/09/16.
 */
public class Token {

    /**
     * Internal representation of Tokens.
     */
    public enum TokenCategory {

        EOF(-1),
        ID(1),
        CTE_FLOAT(2),
        CTE_INT(3),
        CTE_STR(4),
        OP_ATR(5),
        OP_REL1(6),
        OP_REL2(7),
        OP_AD(8),
        OP_MULT(9),
        OP_MOD(10),
        COMMENT(11),
        VECTOR_AUX(12),
        OP_AND(13),
        OP_OR(14),
        OP_NOT(15),
        PR_IF(16),
        PR_ELSE(17),
        PR_SHOOT(18),
        PR_WHILE(19),
        PR_FOR(20),
        TYPE_VALUE(21),
        BOOL_VALUE(22),
        SP(23),
        AB_PAR(24),
        FEC_PAR(25),
        AB_CH(26),
        FEC_CH(27),
        AB_COL(28),
        FEC_COL(29),
        PR_VOID(30),
        PR_IO(31),
        PR_MAIN(32);

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
     * @param lin Lexer.Token line in input
     * @param col Lexer.Token column in input
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
}
