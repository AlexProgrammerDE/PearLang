package net.pistonmaster.pearlang.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PearNumberUtils {
    public static Number add(Number num1, Number num2) {
        if (num1 instanceof BigDecimal || num2 instanceof BigDecimal) {
            return new BigDecimal(num1.toString()).add(new BigDecimal(num2.toString()));
        } else if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() + num2.doubleValue();
        } else if (num1 instanceof Float || num2 instanceof Float) {
            return num1.floatValue() + num2.floatValue();
        } else if (num1 instanceof Long || num2 instanceof Long) {
            return num1.longValue() + num2.longValue();
        } else {
            return num1.intValue() + num2.intValue();
        }
    }

    public static Number subtract(Number num1, Number num2) {
        if (num1 instanceof BigDecimal || num2 instanceof BigDecimal) {
            return new BigDecimal(num1.toString()).subtract(new BigDecimal(num2.toString()));
        } else if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() - num2.doubleValue();
        } else if (num1 instanceof Float || num2 instanceof Float) {
            return num1.floatValue() - num2.floatValue();
        } else if (num1 instanceof Long || num2 instanceof Long) {
            return num1.longValue() - num2.longValue();
        } else {
            return num1.intValue() - num2.intValue();
        }
    }

    public static Number multiply(Number num1, Number num2) {
        if (num1 instanceof BigDecimal || num2 instanceof BigDecimal) {
            return new BigDecimal(num1.toString()).multiply(new BigDecimal(num2.toString()));
        } else if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() * num2.doubleValue();
        } else if (num1 instanceof Float || num2 instanceof Float) {
            return num1.floatValue() * num2.floatValue();
        } else if (num1 instanceof Long || num2 instanceof Long) {
            return num1.longValue() * num2.longValue();
        } else {
            return num1.intValue() * num2.intValue();
        }
    }

    public static Number divide(Number num1, Number num2) {
        if (num1 instanceof BigDecimal || num2 instanceof BigDecimal) {
            return new BigDecimal(num1.toString()).divide(new BigDecimal(num2.toString()), RoundingMode.HALF_UP);
        } else if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() / num2.doubleValue();
        } else if (num1 instanceof Float || num2 instanceof Float) {
            return num1.floatValue() / num2.floatValue();
        } else if (num1 instanceof Long || num2 instanceof Long) {
            return num1.longValue() / num2.longValue();
        } else {
            return num1.intValue() / num2.intValue();
        }
    }

    public static Number modulo(Number num1, Number num2) {
        if (num1 instanceof BigDecimal || num2 instanceof BigDecimal) {
            return new BigDecimal(num1.toString()).remainder(new BigDecimal(num2.toString()));
        } else if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() % num2.doubleValue();
        } else if (num1 instanceof Float || num2 instanceof Float) {
            return num1.floatValue() % num2.floatValue();
        } else if (num1 instanceof Long || num2 instanceof Long) {
            return num1.longValue() % num2.longValue();
        } else {
            return num1.intValue() % num2.intValue();
        }
    }

    public static boolean equals(Number number, Number number1) {
        return number.doubleValue() == number1.doubleValue();
    }

    public static boolean greaterThan(Number number, Number number1) {
        return number.doubleValue() > number1.doubleValue();
    }

    public static boolean lessThan(Number number, Number number1) {
        return number.doubleValue() < number1.doubleValue();
    }

    public static boolean greaterThanOrEqual(Number number, Number number1) {
        return number.doubleValue() >= number1.doubleValue();
    }

    public static boolean lessThanOrEqual(Number number, Number number1) {
        return number.doubleValue() <= number1.doubleValue();
    }
}
