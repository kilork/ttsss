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
package com.github.kilork.sss.gbce.memory;

import com.github.kilork.sss.StockPrice;
import com.github.kilork.sss.StockValue;
import java.math.BigDecimal;

/**
 * Preferred Stock.
 * 
 * @author Alexander Korolev <kilork at yandex.ru>
 */
class PreferredMemoryGBCEStock extends CommonMemoryGBCEStock {

    private final StockValue fixedDivident;

    PreferredMemoryGBCEStock(String name, int lastDivident, int fixedDivident, int parValue) {
        super(name, lastDivident, parValue);
        this.fixedDivident = new StockValue(BigDecimal.valueOf(fixedDivident).divide(BigDecimal.TEN.pow(2)));
    }

    @Override
    public StockValue calculateDividendYield(StockPrice price) {
        return getCalculations().dividendYieldPreferred(fixedDivident, getParValue(), price);
    }

    @Override
    public String toString() {
        return "Preferred Stock {" + "Symbol=" + symbol() + ", Last Dividend=" + getLastDividend() + ", Fixed Dividend=" + fixedDivident + ", Par Value=" + getParValue() + '}';
    }
    
}
