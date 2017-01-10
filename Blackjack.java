package me.owenandnoy.blackjack;

public final class Blackjack {

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
        game.play();
    }

    @SafeVarargs
    public static <T> void print(T... args) {
        for (T t : args) {
            System.out.println(t);
        }
    }
}
