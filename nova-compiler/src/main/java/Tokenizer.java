import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Created by rubenspessoa on 04/09/16.
 */
public class Tokenizer {

    /**
     * Internal class holding the information of a token type.
     */
    private class TokenInfo {

        public final Pattern regex;
        public final int token;

        /**
         * Construct TokenInfo within its values.
         * @param regex Token Regex Pattern
         * @param token Token Category.
         */
        public TokenInfo(Pattern regex,
                         int token) {
            super();
            this.regex = regex;
            this.token = token;
        }

    }

    private LinkedList<TokenInfo> tokenInfos;
    private LinkedList<Token> tokens;
    private static Tokenizer lexer = null;

    /**
     * @return the lexer for Nova programming language.
     */
    public static Tokenizer getLexer() {
        if (lexer == null) {
            lexer = createLexer();
        }
        return lexer;
    }

    private static Tokenizer createLexer() {
        Tokenizer lexer = new Tokenizer();

        // TODO: Create regex for every token category.

        return lexer;
    }

    /**
     * Add a regular expression and a token id to the internal list of recognized tokens
     * @param regex regular expression to match against
     * @param token tokenCategory that the regular expression is linked to
     */
    public void add(String regex, int token) {
        tokenInfos.add(
                new TokenInfo(
                        Pattern.compile("ˆ(" + regex + ")"),
                        token
                )
        );
    }

    /**
     * Tokenize an input string.
     * The result can be acessed via getTokens().
     * @param inputString
     */
    public void tokenize(String inputString) {

    }

    /**
     * Get the tokens generated in the last call of tokenize.
     * @return a list of tokens to be fed to Parser
     */
    public LinkedList<Token> getTokens() {
        return this.tokens;
    }

}
