package me.owenandnoy.blackjack.entities;

import lombok.Getter;
import lombok.SneakyThrows;
import me.owenandnoy.blackjack.Blackjack;
import me.owenandnoy.blackjack.gameutils.Card;
import me.owenandnoy.blackjack.gameutils.DeckOfCards;
import me.owenandnoy.blackjack.gameutils.ActiveCards;

public final class Dealer implements Entity {

    @Getter private String name;
    private DeckOfCards deckOfCards;
    private ActiveCards activeCards;

    public Dealer() {
        this.name = getRandomNames();
        deckOfCards = new DeckOfCards();
    }

    private String getRandomNames() {
        String[] names = {"Tyrone", "Jamal", "Lorenzo", "Dre", "Trevon", "Keisha", "GARMA'S SISTER", "Shithead", "Ted", "Luke (Avoise)"};
        return names[(int)(Math.random()*names.length)];
    }

    private Card giveCard() {
        if (deckOfCards.getSizeOfCard() == 0) deckOfCards = new DeckOfCards();
        return deckOfCards.getCard();
    }

    public void deal(Player player) { player.addCard(getNewHand(player.makeBet())); }

    public void listen(Player player) {
        Integer choice = player.getChoice();
        if(choice == 0) {
            player.stay();
        } else if (choice == 1) {
            player.hit(giveCard());
        }
    }

    public Integer getBestHand() { return activeCards.getPoints(); }

    private ActiveCards getNewHand(Integer bet) { return new ActiveCards(giveCard(), giveCard(), bet); }

    public void dealToDealer() {
        Card faceDown = giveCard();
        faceDown.setVisible(false);
        activeCards = new ActiveCards(faceDown, giveCard(), 0);
        this.activeCards.active = true;
    }

    public void showShowing() {
        Blackjack.print(activeCards.active ? name + " is waiting.." : name + " has: " + activeCards.getPoints() + " points");
        sleepToWait();
    }

    public void play() {
        while(activeCards.active) {
            if (activeCards.getPoints() > 17) {
                stay();
            } else hit(null);
        }
    }

    private void endRound() {
        activeCards.showAll(true);
        activeCards.active = false;
    }

    @Override
    public void hit(Card card) {
        activeCards.addCard(giveCard());
        Blackjack.print(name + " hit and now they have " + activeCards.getAllCards() + " or " + activeCards.getPoints() + " points.");
        sleepToWait();
        if (activeCards.getPoints() > 21) {
            Blackjack.print(name + " is busted with " + activeCards.getPoints() + "!");
            endRound();
        }
    }

    @Override
    public void stay() {
        Blackjack.print(name + " is staying with: " + activeCards.getAllCards() + "or " + activeCards.getPoints() + " points.");
        sleepToWait();
        endRound();
    }

    @SneakyThrows
    @Override
    public void sleepToWait() {
        Thread.sleep(2000);
    }
}
