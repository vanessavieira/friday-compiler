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
        OP_AND(15),
        OP_OR(16),
        OP_NEG(17),
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
        SP(29),
        AB_PAR(30),
        FEC_PAR(31),
        AB_CH(32),
        FEC_CH(33),
        AB_COL(34),
        FEC_COL(35);

        private int value;

        private TokenCategory(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    TokenCategory tokenCategory;
    String sequence;
    int pos;

    /**
     * Construct the token representation within its values
     * @param tokenCategory Category Identifier int
     * @param sequence String that represents the token
     * @param pos Token position in input
     */

    public Token(TokenCategory tokenCategory,
                 String sequence,
                 int pos) {

        this.tokenCategory = tokenCategory;
        this.sequence = sequence;
        this.pos = pos;

    }
}
