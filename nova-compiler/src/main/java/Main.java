

import java.io.File;
import java.util.List;

import Parser.*;
import Parser.Lexer.Lexer;

public class Main {

    public static void main(String[] args) {

        String path = "/Users/vanessa.vieira/Documents/GitHub/friday-compiler/nova-compiler/src/main/java/";

        File helloWorld = new File(path + "CodeExamples/helloWorld.txt");
        File fibonacci = new File(path + "CodeExamples/fibonacci.txt");
        File shellsort = new File(path + "CodeExamples/shellSort.txt");

        //Parser parser = new Parser();
        //Lexer lexer;

        try {
            Lexer lexer = Lexer.getLexer();

//            lexer.lex(helloWorld);
//            System.out.println(lexer.getTokens());

//            lexer.lex(fibonacci);
//            System.out.println(lexer.getTokens());

            lexer.lex(shellsort);
            System.out.println(lexer.getTokens());

        } catch ( Exception e ) {
            System.err.println(e.getMessage());
        }

    }
}
