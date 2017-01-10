package me.owenandnoy.blackjack;

import lombok.SneakyThrows;
import me.owenandnoy.blackjack.entities.Dealer;
import me.owenandnoy.blackjack.entities.Player;
import java.util.Scanner;

final class Game {

    private Player[] players;
    private Dealer dealer;

    @SneakyThrows
    void start() {
        Blackjack.print("Welcome to blackjack!");
        Scanner scanner = new Scanner(System.in);
        String input;
        Blackjack.print("How many players? (1-4)");
        input = scanner.next();
        int playerNum = Integer.parseInt(input);
        while (playerNum < 1 || playerNum > 4) {
            Blackjack.print("A number between 1 and 4.");
            input = scanner.next();
            playerNum = Integer.parseInt(input);
        }
        Blackjack.print("We'll be starting with " + playerNum + " players.");
        players = new Player[playerNum];
        String playerName;
        for (int i = 0; i < players.length; i++) {
            Blackjack.print("What is player " + (i + 1) + "'s name?");
            playerName = scanner.next();
            while (playerName.equals("")) {
                Blackjack.print("Give me a name");
                playerName = scanner.next();
            }
            players[i] = new Player(playerName, i);
        }
        if (players.length == 1) {
            Blackjack.print("Only you? Alright then! Hey " + players[0].name + "!");
        } else {
            Blackjack.print("Okay so here are our players: ");
            for (int i = 0; i < players.length; i++) {
                if (i == players.length - 1) {
                    Blackjack.print("and " + players[i].name + ".\n");
                } else {
                    Blackjack.print(players[i].name + ", ");
                }
            }
        }
        Blackjack.print("Alright! Let's look for a dealer!");
        dealer = new Dealer();
        Blackjack.print("Hi, my name is " + dealer.getName() + "! I have been working at this casino for "+ ((int) (Math.random()*723)+1) + " years! Can't wait for the promotion!");
    }

    @SneakyThrows
    void play() {
        Scanner scanner = new Scanner(System.in);
        Blackjack.print("Are you ready to play? (y/n)");
        String response = scanner.next();
        while (!getYes(response)) {
            Blackjack.print("Are you ready now? (y/n)");
            response = scanner.next();
        }
        Blackjack.print("Okay let's start!");
        while(numPlayersPlaying() > 0) {
            playHand();
        }
        Blackjack.print("Game Over!");
    }

    private void playHand() {
        for (Player p : players) {
            if (p.isActive()) dealer.deal(p);
        }
        dealer.dealToDealer();
        showAll();
        while(numPlayersActive() > 0) {
            for (Player player : players) {
                while (player.hasAnActiveHand()) dealer.listen(player);
            }
            dealer.play();
            showAll();

        }
        clearTable();
        Blackjack.print("Round over!");
    }

    private int numPlayersPlaying() { // if one person busted for example
        int num = 0;
        for (Player player : players) {
            if (player.isActive()) num++;
        }
        return num;
    }

    private int numPlayersActive() {
        int num = 0;
        for (Player player : players) {
            if (player.hasAnActiveHand()) num++;
        }
        return num;
    }

    private void clearTable() {
        for (Player p : players) {
            if (p.isActive()) p.settle(dealer.getBestHand());
        }
    }

    private void showAll() {
        for (Player player : players) {
            player.show();
        }
        dealer.showShowing();
    }

    private boolean getYes(String s) {
        return s.toLowerCase().equals("y") || s.toLowerCase().equals("yes");
    }
}
