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
package com.github.kilork.sss.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.closeTo;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public class MathCalculusTest {

    @Test(expected = NullPointerException.class)
    public void testGeometricMeanNullInput() {
        MathCalculus.geometricMean(null);
    }

    @Test
    public void testGeometricMeanEmptyInput() {
        BigDecimal result = MathCalculus.geometricMean(Collections.emptyList());
        BigDecimal expResult = BigDecimal.ZERO;
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void testGeometricMeanSingleValueInput() {
        BigDecimal result = MathCalculus.geometricMean(Collections.singletonList(new BigDecimal("100")));
        BigDecimal expResult = BigDecimal.valueOf(100l);
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void testGeometricMeanThreeValueInput() {
        List<BigDecimal> input = Arrays.asList(2l, 4l, 8l).stream().map(BigDecimal::valueOf).collect(Collectors.toList());
        BigDecimal result = MathCalculus.geometricMean(input);
        BigDecimal expResult = BigDecimal.valueOf(4l);
        assertThat(result, closeTo(expResult, new BigDecimal("0.0000001")));
    }

}
