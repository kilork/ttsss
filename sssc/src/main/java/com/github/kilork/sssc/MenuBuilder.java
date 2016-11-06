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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Simple menu builder.
 * 
 * @author Alexander Korolev <kilork at yandex.ru>
 */
final class MenuBuilder {

    private final List<MenuItem> items = new ArrayList<>();

    static MenuBuilder create() {
        return new MenuBuilder();
    }

    public MenuBuilder submenu(String label, Consumer<Screen> entryAction, Consumer<MenuBuilder> submenuBuilderConsumer) {
        MenuBuilder submenuBuilder = create();
        submenuBuilderConsumer.accept(submenuBuilder);
        Menu submenu = submenuBuilder.build();
        return item(label, entryAction.andThen(submenu::show));
    }

    public MenuBuilder item(String label, Consumer<Screen> actionRunner) {
        MenuItem menuItem = new MenuItem(label, actionRunner);
        items.add(menuItem);
        return this;
    }

    public Menu build() {
        return new Menu(items);
    }

}
