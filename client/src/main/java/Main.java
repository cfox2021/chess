import chess.*;
import client.Repl;

public class Main {

    static Repl repl = new Repl("http://localhost:8080");

    public static void main(String[] args) {

        System.out.println("♕ 240 Chess Client:");
        repl.run();
    }
}