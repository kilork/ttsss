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

import com.github.kilork.sss.gbce.GBCEStock;

/**
 * Predefined stock creation methods.
 * @author Alexander Korolev <kilork at yandex.ru>
 */
class MemoryGBCEStockFactory {

    static GBCEStock createCommon(String name, int lastDivident, int parValue) {
        checkCommon(lastDivident, parValue);
        return new CommonMemoryGBCEStock(name, lastDivident, parValue);
    }

    static GBCEStock createPreferred(String name, int lastDivident, int fixedDivident, int parValue) {
        checkCommon(lastDivident, parValue);
        if (fixedDivident < 0) {
            throw new IllegalArgumentException("Fixed Divident can not be less than zero");
        }
        return new PreferredMemoryGBCEStock(name, lastDivident, fixedDivident, parValue);
    }

    private static void checkCommon(int lastDivident, int parValue) throws IllegalArgumentException {
        if (lastDivident < 0) {
            throw new IllegalArgumentException("Last Divident can not be less than zero");
        }
        if (parValue <= 0) {
            throw new IllegalArgumentException("Par Value must be greater than zero");
        }
    }
    
}
