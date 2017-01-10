package me.owenandnoy.blackjack.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public final class GameException extends Throwable {
    private final String message;
}
