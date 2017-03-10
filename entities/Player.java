package me.owenandnoy.blackjack.entities;

import lombok.Data;
import lombok.SneakyThrows;
import me.owenandnoy.blackjack.Blackjack;
import me.owenandnoy.blackjack.gameutils.Card;
import me.owenandnoy.blackjack.gameutils.ActiveCards;

import java.util.*;

@Data
public final class Player implements Entity {

    public String name = "";
    public Integer numberOfPlayers = 0;
    private Integer money = (int)(Math.ceil(Math.random()*100));
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
            sleepToWait();
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
            money += currentActiveCards.getBetValue() * 2;
            endRound();
        }
    }

    @Override
    public void stay() {
        Blackjack.print(name + " is staying with: " + currentActiveCards.getName() + " or " + currentActiveCards.getPoints() + " points.");
        sleepToWait();
        endRound();
    }

    @SneakyThrows
    @Override
    public void sleepToWait() {
        Thread.sleep(2000);
    }

    @SneakyThrows
    Integer makeBet() {
        Scanner scanner = new Scanner(System.in);
        Integer betNumber;
        Blackjack.print("What's your bet gonna be? You have £" + money);
        String next = scanner.next();
        betNumber = Integer.parseInt(next);
        while (betNumber < 1 || betNumber > money) {
            Blackjack.print("Make sure you have enough money and the bet is a positive number! You have £" + money);
            next = scanner.next();
            betNumber = Integer.parseInt(next);
            break;
        }
        money -= betNumber;
        return betNumber;
    }

    public void settle(Integer dealerHand) {
        for (ActiveCards activeCards : activeCardsList) {
            if (Objects.equals(dealerHand, activeCards.getPoints())) {
                if (activeCards.getPoints() > 21) return;
                Blackjack.print("It's a draw!");
                money = money + activeCards.getBetValue(); // doesn't win lose
                activeCardsList = new ArrayList<>();
                return;
            }
            if (activeCards.getPoints() > 21) {
                Blackjack.print(name + " busted and lost £" + activeCards.getBetValue() + ", their cards: " + activeCards.getAllCards());
            } else if (dealerHand < activeCards.getPoints() || dealerHand > 21) {
                if (activeCards.isAutoWin()) {
                    if (Objects.equals(dealerHand, activeCards.getPoints())) return;
                    Blackjack.print(name + " has won automatically and won " + ((2 * activeCards.getBetValue())) + "!");
                    money += activeCards.getBetValue() * 2;
                    activeCardsList = new ArrayList<>();
                    return;
                }
                Blackjack.print(name + " beat the dealer and won £" + activeCards.getBetValue() * 2);
                money += activeCards.getBetValue() * 2;
            } else {
                Blackjack.print(name + " has lost against the dealer!");
                // no need to minus money as they've already placed their bet which deducts it anyway
            }
        }
        if (money <= 0) quit();
        activeCardsList = new ArrayList<>();
    }

    private void updateCHand() {
        this.currentActiveCards = null;
        activeCardsList.stream().filter(ActiveCards::isActive).forEach(h -> this.currentActiveCards = h);
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
