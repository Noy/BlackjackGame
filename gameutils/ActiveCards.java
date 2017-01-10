package me.owenandnoy.blackjack.gameutils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class ActiveCards {

    private List<Card> cards = new ArrayList<>();
    @Getter public Integer betValue;
    @Getter public boolean active;

    public ActiveCards(Card firstCard, Card secondCard, Integer bet) {
        cards.add(firstCard);
        cards.add(secondCard);
        active = true;
        betValue = bet;
    }

    public String getAllCards() {
        String returnValue = "";
        for (Card c : cards) {
            returnValue += c.getValueName() + " ";
        }
        return returnValue;
    }

    public String getName() { return this.getAllCards() + "£"+betValue+" "; }

    public Integer getPoints() {
        Integer aces = 0;
        Integer score = 0;
        for (Card c : cards) {
            if (c.getCardValue() == 0) aces++;
            score += c.getLowestValue();
        }
        while(aces > 0 && score <= 11) {
            score += 10;
            aces--;
        }
        return score;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void showAll(boolean bool) {
        for (Card c : cards) {
            c.setVisible(bool);
        }
    }

    public boolean isAutoWin() { return cards.size() == 2 && getPoints() == 21; }
}
