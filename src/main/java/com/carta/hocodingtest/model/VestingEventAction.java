package com.carta.hocodingtest.model;

import java.math.BigDecimal;
import java.util.Map;

public enum VestingEventAction {
    VEST {
        @Override
        public BigDecimal apply(BigDecimal b1, BigDecimal b2) {
            return b1.add(b2);
        }
    },
    CANCEL {
        @Override
        public BigDecimal apply(BigDecimal b1, BigDecimal b2) {
            return b1.subtract(b2);
        }
    };

    private static Map<String, VestingEventAction> ENUM_BY_NAME = Map.of("VEST", VEST, "CANCEL", CANCEL);

    public static VestingEventAction byName(String name) {
        return ENUM_BY_NAME.get(name);
    }

    public abstract BigDecimal apply(BigDecimal b1, BigDecimal b2);
}
