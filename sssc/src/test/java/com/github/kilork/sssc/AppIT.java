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

import com.github.kilork.sss.StockPrice;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public class AppIT {

    private static final String[] SYMBOLS = {"ALE", "GIN", "JOE", "POP", "TEA"};

    @Test
    public void testAllShareIndexNoData() {
        TestScannerScreen screen = runApp(command().select(2).select(0).build());
        String lastError = screen.lastError();
        assertThat(lastError, containsString("No trades found to calculate volume weighted stock price for"));
    }

    private void testStockOperationsPERatio(String symbol) {
        TestScannerScreen screen = runApp(command()
                .select(1) // stock operations
                .chooseStock(symbol)
                .select(2) // P/E ratio
                .enterPrice("100")
                .select(0) // exit to main menu
                .select(0) // exit from application
                .build());
        String output = screen.consoleOutput();
        System.out.println(output);
        Matcher matcher = validateSelectedSymbol(output, symbol);
        String lastError = screen.lastError();
        StockPrice dividendPrice = StockPrice.valueOf(matcher.group(2));
        if (StockPrice.ZERO.compareTo(dividendPrice) == 0) {
            assertThat(lastError, containsString("Dividend must not be zero"));
        } else {
            assertThat(screen.lastError(), is(nullValue()));
        }
    }

    private Matcher validateSelectedSymbol(String output, String symbol) {
        Matcher matcher = Pattern.compile("Symbol=([^,]+).+Last Dividend=([^,]+)").matcher(output);
        assertThat(matcher.find(), is(true));
        assertThat(matcher.group(1), is(symbol));
        return matcher;
    }

    @Test
    public void testStockOperationsPERatio() {
        for (String symbol : SYMBOLS) {
            testStockOperationsPERatio(symbol);
        }
    }

    private void testStockOperationsDividendYield(String symbol) {
        TestScannerScreen screen = runApp(command()
                .select(1) // stock operations
                .chooseStock(symbol)
                .select(1) // dividend yield
                .enterPrice("100")
                .select(0) // exit to main menu
                .select(0) // exit from application
                .build());
        String output = screen.consoleOutput();
        System.out.println(output);
        validateSelectedSymbol(output, symbol);
        assertThat(screen.lastError(), is(nullValue()));
    }

    @Test
    public void testStockOperationsDividendYield() {
        for (String symbol : SYMBOLS) {
            testStockOperationsDividendYield(symbol);
        }
    }

    private CommandInputBuilder recordTrade(CommandInputBuilder builder) {
        return builder
                .select(3)
                .add("-") // current time
                .add("10") // enter quantity
                .add("s") // sell
                .enterPrice("100") // price
                ;
    }

    private void testStockOperationsRecordTrade(String symbol) {
        TestScannerScreen screen = runApp(command()
                .select(1) // stock operations
                .chooseStock(symbol)
                .update(this::recordTrade)
                .select(4) // calculate
                .select(0) // exit to main menu
                .select(0) // exit from application
                .build());
        String output = screen.consoleOutput();
        System.out.println(output);
        validateSelectedSymbol(output, symbol);
        assertThat(screen.lastError(), is(nullValue()));
        assertThat(output, containsString("Volume Weighted Stock Price in last 5 minutes: 100.00"));
    }

    @Test
    public void testStockOperationsRecordTrade() {
        for (String symbol : SYMBOLS) {
            testStockOperationsRecordTrade(symbol);
        }
    }

    @Test
    public void testAllShareIndexEqualData() {
        CommandInputBuilder commandBuilder = command();
        for (String symbol : SYMBOLS) {
            commandBuilder
                    .select(1) // stock operations
                    .chooseStock(symbol)
                    .update(this::recordTrade)
                    .select(4) // calculate
                    .select(0); // exit to main menu
        }
        commandBuilder
                .select(2) // calculate all share index
                .select(0); // exit
        TestScannerScreen screen = runApp(commandBuilder.build());
        String output = screen.consoleOutput();
        System.out.println(output);
        assertThat(screen.lastError(), is(nullValue()));
        assertThat(output, containsString("All Share Index in last 5 minutes: 100.00"));
    }

    private static CommandInputBuilder command() {
        return new CommandInputBuilder();
    }

    private static class CommandInputBuilder {

        private final List<String> commands = new ArrayList<>();

        public CommandInputBuilder add(String command) {
            commands.add(command);
            return this;
        }

        public CommandInputBuilder select(int menuIndex) {
            return add(String.valueOf(menuIndex));
        }

        public CommandInputBuilder chooseStock(String stockSymbol) {
            return add(stockSymbol);
        }

        public CommandInputBuilder enterPrice(String stockPrice) {
            return add(stockPrice);
        }

        public CommandInputBuilder update(Function<CommandInputBuilder, CommandInputBuilder> function) {
            return function.apply(this);
        }

        public String build() {
            return String.join("\n", commands);
        }
    }

    private TestScannerScreen runApp(String... inputLines) {
        TestScannerScreen screen = null;
        App instance = new App();
        try (Scanner scanner = new Scanner(String.join("\n", inputLines));) {
            screen = new TestScannerScreen(scanner);
            instance.run(screen);
            return screen;
        } catch (RuntimeException ex) {
            if (screen != null) {
                return screen;
            }
            throw ex;
        }
    }

    private static class TestScannerScreen extends ConsoleScannerScreen {

        private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        private final PrintStream printStream = new PrintStream(byteArrayOutputStream, true);
        private final Deque<String> errors = new ArrayDeque<>();
        private final Deque<String> warnings = new ArrayDeque<>();

        public TestScannerScreen(Scanner scanner) {
            super(scanner);
        }

        public String consoleOutput() {
            return byteArrayOutputStream.toString();
        }

        public String lastError() {
            return errors.peekLast();
        }

        public String lastWarning() {
            return warnings.peekLast();
        }

        @Override
        public void error(String line) {
            super.error(line);
            errors.add(line);
        }

        @Override
        public void warning(String line) {
            super.warning(line);
            warnings.add(line);
        }

        @Override
        protected PrintStream getAppender() {
            return printStream;
        }

    }

}
