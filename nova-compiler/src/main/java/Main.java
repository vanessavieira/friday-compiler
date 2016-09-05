/**
 * Created by rubenspessoa on 04/09/16.
 */

import Lexer.*;

public class Main {

    public static void main(String[] args) {

        String fibonacci = "int fibonacci(int n) {\n" +
                "    int f1 = 0;\n" +
                "    int f2 = 1;\n" +
                "    int fi = 0;\n" +
                "\n" +
                "    intOut(0);\n" +
                "    stringOut(\", \");\n" +
                "    intOut(1);\n" +
                "\n" +
                "    if (n == 0 or n == 1) {\n" +
                "      shoot 1;\n" +
                "    }\n" +
                "\n" +
                "    while(fi < n) {\n" +
                "      fi = f1 + f2;\n" +
                "      f1 = f2;\n" +
                "      f2 = fi;\n" +
                "      stringOut(\",\");\n" +
                "      intOut(fi);\n" +
                "    }\n" +
                "\n" +
                "    shoot fi;\n" +
                "  }\n" +
                "\n" +
                "  void main() {\n" +
                "    int n;\n" +
                "    intIn(n);\n" +
                "\n" +
                "    int fib = fibonacci(n);\n" +
                "    # do something with fib\n" +
                "  }";

        String shellsort = "void main() {\n" +
                "    int size;\n" +
                "    intIn(size);\n" +
                "\n" +
                "    int vet::size;\n" +
                "    for (int i = 0; i < size; i = i + 1) {\n" +
                "      int x;\n" +
                "      intIn(x);\n" +
                "      add(vet, x);\n" +
                "    }\n" +
                "\n" +
                "    int value;\n" +
                "    int gap = 1;\n" +
                "    while(gap < size) {\n" +
                "      gap = 3 * gap + 1;\n" +
                "    }\n" +
                "\n" +
                "    while(gap > 1) {\n" +
                "      gap = gap / 3;\n" +
                "      for (int i = gap; i < size; i = i + 1) {\n" +
                "        value = getValue(vet, i);\n" +
                "        int j = i - gap;\n" +
                "\n" +
                "        while(j >= 0 and value < getValue(vet, j)) {\n" +
                "          setValue(vet, j + gap, getValue(vet, j));\n" +
                "          j = j - gap;\n" +
                "        }\n" +
                "\n" +
                "        setValue(vet, j + gap, value);\n" +
                "      }\n" +
                "    }\n" +
                "  }";

        try {
            Lexer lexer = Lexer.getLexer();
            lexer.lex(fibonacci);
            System.out.println(lexer.getTokens());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
