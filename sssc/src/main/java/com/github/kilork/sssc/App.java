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
import com.github.kilork.sss.StockTradePeriod;
import com.github.kilork.sss.StockTradeTimestamp;
import com.github.kilork.sss.StockValue;
import com.github.kilork.sss.StocksService;
import com.github.kilork.sss.gbce.GBCEStock;
import com.github.kilork.sss.gbce.GBCEStockTrade;
import com.github.kilork.sss.gbce.GBCEStocksService;
import com.github.kilork.sss.gbce.GBCEStocksServiceProvider;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Scanner;

/**
 * Super Simple Stocks console application.
 *
 * <pre>
 * Usage:
 * 1. Select stock to operate
 *  1a. get devidend yield
 *  1b. calculate P/E ratio
 *  1c. record a trade
 *  1d. calculate Volume Weighted Stock Price based on trades in past 5 minutes
 * 2. Calculate the GBCE All Share Index
 * </pre>
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public class App {

    public static void main(String[] args) {
        App app = new App();
        app.runInConsole();
    }

    private static final String CLIENT_VERSION = "0.0.1";

    private final GBCEStocksService stockService;
    private final Menu menuMain;
    private GBCEStock selectedStock;

    App() {
        menuMain = MenuBuilder.create()
                .submenu("Stock operations", this::doSelectStock, $ -> $
                        .item("Dividend Yield", this::doCalculateDividendYield)
                        .item("P/E Ratio", this::doCalculatePriceEarningsRatio)
                        .item("Record a trade", this::doRecordTrade)
                        .item("Volume Weighted Stock Price", this::doCalculateVolumeWeightedStockPrice)
                )
                .item("All Share Index", this::doAllShareIndex)
                .build();

        stockService = StocksService.getInstance(GBCEStocksServiceProvider.inMemory());
    }

    public void runInConsole() {
        try (Scanner scanner = createScanner()) {
            Screen screen = createScreen(scanner);
            run(screen);
        }
    }

    public void run(Screen screen) {
        screen.message(String.format("The Global Beverage Corporation Exchange Stock Market (client: %s, server: %s)", CLIENT_VERSION, stockService.version()));
        doMenu(screen);
    }

    Screen createScreen(final Scanner scanner) {
        return new ConsoleScannerScreen(scanner);
    }

    Scanner createScanner() {
        return new Scanner(System.in);
    }

    private void doMenu(Screen screen) {
        menuMain.show(screen);
    }

    private void doSelectStock(Screen screen) {
        Optional<GBCEStock> stock;
        do {
            screen.message("Known stocks:");
            stockService.getStocks()
                    .stream()
                    .map(GBCEStock::symbol)
                    .sorted()
                    .forEach(screen::message);
            screen.message("Enter stock symbol:");
            screen.printCursor();
            String stockSymbol = screen.readString();
            stock = stockService.stockBySymbol(stockSymbol);
            if (!stock.isPresent()) {
                screen.message(String.format("Stock with symbol %s not found. Try again.", stockSymbol));
            }
        } while (!stock.isPresent());
        this.selectedStock = stock.get();
        screen.message(String.format("Stock info: %s", this.selectedStock));
    }

    private void doCalculateDividendYield(Screen screen) {
        StockPrice stockPrice = inputStockPrice(screen);
        StockValue dividentYield = selectedStock.calculateDividendYield(stockPrice);
        screen.message(String.format("Calculated Dividend Yield: %s", dividentYield.toString()));
    }

    private void doCalculatePriceEarningsRatio(Screen screen) {
        StockPrice stockPrice = inputStockPrice(screen);
        StockValue priceEarningsRatio = selectedStock.calculatePERatio(stockPrice);
        screen.message(String.format("Calculated P/E Ratio: %s", priceEarningsRatio.toString()));
    }

    private void doRecordTrade(Screen screen) {
        StockTradeTimestamp timestamp = inputStockTradeTimestamp(screen);
        int quantity = inputStockTradeQuantity(screen);
        boolean sell = inputStockTradeSellBuy(screen);
        StockPrice stockPrice = inputStockPrice(screen);
        GBCEStockTrade stockTrade;
        if (sell) {
            stockTrade = selectedStock.create().sell(timestamp, quantity, stockPrice);
        } else {
            stockTrade = selectedStock.create().buy(timestamp, quantity, stockPrice);
        }
        selectedStock.add(stockTrade);
        screen.message(String.format("Stock Trade recorded (%s)", stockTrade));
    }

    private void doCalculateVolumeWeightedStockPrice(Screen screen) {
        StockPrice volumeWeightedStockPrice = selectedStock.calculateVolumeWeightedStockPrice(StockTradePeriod.lastNMinutes(5));
        screen.message(String.format("Volume Weighted Stock Price in last 5 minutes: %s", volumeWeightedStockPrice));
    }

    private void doAllShareIndex(Screen screen) {
        StockPrice allShareIndex = stockService.allShareIndex();
        screen.message(String.format("All Share Index in last 5 minutes: %s", allShareIndex));
    }

    private StockPrice inputStockPrice(Screen screen) {
        do {
            screen.message("Enter stock price:");
            screen.printCursor();
            String stockPriceInput = screen.readString();
            try {
                return validateStockPrice(StockPrice.valueOf(stockPriceInput));
            } catch (NumberFormatException ex) {
                screen.message("Wrong number format. Try again.");
            } catch (IllegalArgumentException ex) {
                screen.message(String.format("%s. Try again.", ex.getMessage()));
            }
        } while (true);
    }

    private StockTradeTimestamp inputStockTradeTimestamp(Screen screen) {
        do {
            screen.message("Enter timestamp (RFC-1123, '-' for now):");
            screen.printCursor();
            String stockTradeTimestampInput = screen.readLine();
            if ("-".equals(stockTradeTimestampInput)) {
                return StockTradeTimestamp.now();
            }
            try {
                return validateStockTradeTimestamp(StockTradeTimestamp.valueOf(stockTradeTimestampInput));
            } catch (DateTimeParseException ex) {
                screen.message(String.format("Wrong timestamp '%s'. Try again.", stockTradeTimestampInput));
            } catch (IllegalArgumentException ex) {
                screen.message(String.format("%s. Try again.", ex.getMessage()));
            }
        } while (true);
    }

    private int inputStockTradeQuantity(Screen screen) {
        do {
            screen.message("Enter quantity:");
            screen.printCursor();
            int stockTradeQuantity = screen.readInt();
            try {
                return validateStockTradeQuantity(stockTradeQuantity);
            } catch (IllegalArgumentException ex) {
                screen.message(String.format("%s. Try again.", ex.getMessage()));
            }
        } while (true);
    }

    private boolean inputStockTradeSellBuy(Screen screen) {
        do {
            screen.message("Enter trade indicator (s)ell/(b)uy:");
            screen.printCursor();
            String stockTradeSellBuy = screen.readString().toUpperCase();
            if (null != stockTradeSellBuy) {
                switch (stockTradeSellBuy) {
                    case "S":
                        return true;
                    case "B":
                        return false;
                    default:
                        screen.message(String.format("Wrong trade indicator '%s'. Try again.", stockTradeSellBuy));
                        break;
                }
            }
        } while (true);
    }

    private StockPrice validateStockPrice(StockPrice stockPrice) {
        if (StockPrice.ZERO.compareTo(stockPrice) != -1) {
            throw new IllegalArgumentException("Stock Price must be positive decimal number");
        }
        return stockPrice;
    }

    private StockTradeTimestamp validateStockTradeTimestamp(StockTradeTimestamp stockTradeTimestamp) {
        if (StockTradeTimestamp.now().compareTo(stockTradeTimestamp) != 1) {
            throw new IllegalArgumentException("Stock Trade Timestamp must be less than now");
        }
        return stockTradeTimestamp;
    }

    private int validateStockTradeQuantity(int stockTradeQuantity) {
        if (stockTradeQuantity <= 0) {
            throw new IllegalArgumentException("Stock Trade Quantity must be positive");
        }
        return stockTradeQuantity;
    }

}
