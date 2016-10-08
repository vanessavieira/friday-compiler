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
        OP_MEQ(6),
        OP_MAQ(7),
        OP_MEIGQ(8),
        OP_MAIGQ(9),
        OP_IG(10),
        OP_DIF(11),
        OP_AD(12),
        OP_SUB(13),
        OP_MULT(14),
        OP_DIV(36),
        OP_MOD(37),
        COMMENT(38),
        VECTOR_AUX(39),
        OP_AND(15),
        OP_OR(16),
        OP_NOT(17),
        PR_IF(18),
        PR_ELSE(19),
        PR_SHOOT(20),
        PR_WHILE(21),
        PR_FOR(22),
        PR_INT(23),
        PR_FLOAT(24),
        PR_BOOL(25),
        PR_STRING(26),
        PR_TRUE(27),
        PR_FALSE(28),
        SP1(29),
        AB_PAR(30),
        FEC_PAR(31),
        AB_CH(32),
        FEC_CH(33),
        AB_COL(34),
        FEC_COL(35),
        VOID(40),
        READIN(41),
        PRINTOUT(42),
        SP2(43);

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
}
