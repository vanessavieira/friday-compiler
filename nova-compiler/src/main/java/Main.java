/**
 * Created by rubenspessoa on 04/09/16.
 */

import java.io.File;
import Lexer.*;
import Parser.Parser;

public class Main {

    public static void main(String[] args) {

        String path = "/Users/rubenspessoa/Documents/workspace/nova-compiler/nova-compiler/src/main/java/";

        File helloWorld = new File(path + "CodeExamples/helloWorld.txt");
        File fibonacci = new File(path + "CodeExamples/fibonacci.txt");
        File shellsort = new File(path + "CodeExamples/shellSort.txt");

        /*
        try {
            Lexer lexer = Lexer.getLexer();

            lexer.lex(helloWorld);
            System.out.println(lexer.getTokens());

            lexer.lex(fibonacci);
            System.out.println(lexer.getTokens());

            lexer.lex(shellsort);
            System.out.println(lexer.getTokens());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } */

        Parser parser = new Parser();

        try {
            parser.parse(helloWorld);
        } catch ( Exception e ) {
            System.err.println(e.getMessage());
        }
    }
}
