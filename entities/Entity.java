package me.owenandnoy.blackjack.entities;

import me.owenandnoy.blackjack.gameutils.Card;

public interface Entity {
    void hit(Card card);
    void stay();
}
