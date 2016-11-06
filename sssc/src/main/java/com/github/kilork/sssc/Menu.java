/*
 * Copyright (C) 2016 Alexander Korolev <kilork at yandex.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kilork.sssc;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Menu for console appender.
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
class Menu {

    private final List<MenuItem> items;

    Menu(List<MenuItem> items) {
        this.items = items;
    }

    public void show(Screen screen) {
        do {
            showItems(screen);
            MenuItem selectedItem = selectItem(screen);
            if (selectedItem == null) {
                return;
            }
            runAction(screen, selectedItem.getActionRunner());
        } while (true);
    }

    private MenuItem selectItem(Screen screen) {
        do {
            screen.printCursor();
            try {
                int selection = screen.readInt();
                if (selection <= 0 || selection > items.size()) {
                    return null;
                }
                MenuItem selectedItem = items.get(selection - 1);
                return selectedItem;
            } catch (InputMismatchException ex) {
                screen.readString(); // reread problematic token to clear state
                screen.warning("Wrong number. Try Again. To exit enter any nonspecified number.");
            }
        } while (true);
    }

    private void showItems(Screen screen) {
        screen.printBreak();
        for (int i = 0; i < items.size(); i++) {
            screen.message(String.format("%d %s", i + 1, items.get(i).getLabel()));
        }
    }

    private void runAction(Screen screen, Consumer<Screen> action) {
        try {
            action.accept(screen);
        } catch (RuntimeException ex) {
            screen.error("Error: " + Optional.ofNullable(ex.getMessage()).orElse("internal error"));
        }
    }

}
