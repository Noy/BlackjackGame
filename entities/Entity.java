package me.owenandnoy.blackjack.entities;

import me.owenandnoy.blackjack.gameutils.Card;

interface Entity {
    void hit(Card card);
    void stay();
    void sleepToWait() throws InterruptedException;
}
