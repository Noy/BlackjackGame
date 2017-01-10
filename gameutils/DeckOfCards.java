package me.owenandnoy.blackjack.gameutils;

import me.owenandnoy.blackjack.Blackjack;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public final class DeckOfCards {

    private Stack<Card> cards = new Stack<>();

    public DeckOfCards() {
        for (int i = 0; i < 52; i++) {
            cards.push(new Card(i));
        }
        Collections.shuffle(cards); // just shuffling my deck over here yo
    }

    public Card getCard() {
        try {
            return cards.pop();
        }catch (EmptyStackException e) {
            Blackjack.print("decks, man");
            return null;
        }
    }

    public Integer getSizeOfCard() {
        return cards.size();
    }
}
