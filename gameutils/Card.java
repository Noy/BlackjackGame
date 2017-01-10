package me.owenandnoy.blackjack.gameutils;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.owenandnoy.blackjack.exceptions.GameException;

public final class Card {

    @Getter private Integer cardValue;
    @Getter @Setter private boolean visible;

    private final String[] valuesAsNumbers = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    @SneakyThrows
    Card(Integer cardValue) {
        if (cardValue < 0 || cardValue > 51) throw new GameException("This card could not be recognised " + cardValue);
        this.cardValue = cardValue % 13;
        visible = true;
    }

    Integer getLowestValue() {
        if (cardValue < 10) return cardValue + 1;
        return 10;
    }

    public String getValueName() { return valuesAsNumbers[cardValue]; }
}
