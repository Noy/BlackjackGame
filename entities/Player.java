package me.owenandnoy.blackjack.entities;

import lombok.Data;
import lombok.SneakyThrows;
import me.owenandnoy.blackjack.Blackjack;
import me.owenandnoy.blackjack.gameutils.Card;
import me.owenandnoy.blackjack.gameutils.ActiveCards;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@Data
public final class Player implements Entity {

    public String name = "";
    public Integer numberOfPlayers = 0;
    private Integer money = 2000;
    private List<ActiveCards> activeCardsList = new ArrayList<>();
    private ActiveCards currentActiveCards;
    public boolean active = true;

    public Player(String s, Integer n) {
        this.name = s;
        this.numberOfPlayers = n;
        active = true;
    }

    void addCard(ActiveCards activeCards) {
        if (activeCards.getPoints() == 21) {
            Blackjack.print(name + " got 21! " + activeCards.getName() + "!");
            activeCards.active = false;
        }
        this.activeCardsList.add(activeCards);
        this.updateCHand();
    }

    public boolean hasAnActiveHand() {
        for (ActiveCards ac : activeCardsList) {
            if (ac.isActive()) return true;
        }
        return false;
    }

    public void show() {
        for (ActiveCards ac : activeCardsList) {
            Blackjack.print(name + " has: " + ac.getPoints());
        }
    }

    private boolean choiceCorrect(String input) {
        return input.startsWith("h") || input.startsWith("s");
    }

    Integer getChoice() {
        Scanner scanner = new Scanner(System.in);
        Blackjack.print(this.currentActiveCards.getName() + ": hit or stay?");
        String input;
        input = scanner.next();
        while (!choiceCorrect(input)) {
            Blackjack.print("Say hit to hit, stay to stay!");
            input = scanner.next();
        }
        if (input.equalsIgnoreCase("exit")) System.exit(0);
        if (input.startsWith("s")) return 0;
        if (input.startsWith("h")) return 1;
        return null;
    }

    @Override
    public void hit(Card card) {
        Blackjack.print("Adding " + card.getValueName() + " to " + this.currentActiveCards.getName());
        currentActiveCards.addCard(card);
        if (currentActiveCards.getPoints() > 21) {
            Blackjack.print(name + " is busted!");
            endRound();
        } else if (currentActiveCards.getPoints() == 21) {
            Blackjack.print("Awesome, you got 21!");
            endRound();
        }
    }

    @Override
    public void stay() {
        Blackjack.print(name + " is staying with: " + currentActiveCards.getName() + " and " + currentActiveCards.getPoints() + " points.");
        endRound();
    }

    @SneakyThrows
    Integer makeBet() {
        Scanner scanner = new Scanner(System.in);
        Integer betNumber;
        Blackjack.print("What's your bet gonna be? You have " + money);
        String next = scanner.next();
        betNumber = Integer.parseInt(next);
        while (betNumber < 1 || betNumber > money) {
            Blackjack.print("Make sure you have enough money and the bet is a positive number! You have " + money);
            next = scanner.next();
            betNumber = Integer.parseInt(next);
        }
        money -= betNumber;
        return betNumber;
    }

    public void settle(Integer dealerHand) {
        for (ActiveCards h : activeCardsList) {
            if (Objects.equals(dealerHand, h.getPoints())) {
                Blackjack.print("It's a draw!");
                money = money + h.getBetValue(); // doesn't win lose
                return;
            }
            if (h.getPoints() > 21) {
                Blackjack.print(name + " busted and lost " + h.getBetValue() + ", their cards: " + h.getAllCards());
            } else if (dealerHand < h.getPoints() || dealerHand > 21) {
                if (h.isAutoWin()) {
                    Blackjack.print(name + " has won automatically and won " + ((2 * h.getBetValue())) + "!");
                    money += (10 * h.getBetValue()) / 5;
                }
                Blackjack.print(name + " beat the dealer and got back " + h.getBetValue() * 2);
                money += h.getBetValue() * 2;
            } else {
                Blackjack.print(name + " has lost against the dealer!");
                money += money - h.getBetValue();
            }
        }
        if (money <= 0) quit();
        activeCardsList = new ArrayList<>();
    }

    private void updateCHand() {
        this.currentActiveCards = null;
        for (ActiveCards h : activeCardsList) {
            if (h.isActive()) this.currentActiveCards = h;
        }
    }

    private void endRound() {
        if (this.currentActiveCards == null) return;
        currentActiveCards.active = false;
        this.updateCHand();
    }

    private void quit() {
        this.active = false;
    }
}
