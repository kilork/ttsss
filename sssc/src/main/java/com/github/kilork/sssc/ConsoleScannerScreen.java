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

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Facade for input/output operations in terms of this Application.
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
class ConsoleScannerScreen extends AbstractAppenderScreen {

    private final Scanner scanner;

    ConsoleScannerScreen(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public int readInt() {
        return scanner.nextInt();
    }

    @Override
    public String readString() {
        return scanner.next();
    }

    @Override
    public String readLine() {
        return scanner.next() + scanner.nextLine();
    }

    @Override
    protected PrintStream getAppender() {
        return System.out;
    }

}
