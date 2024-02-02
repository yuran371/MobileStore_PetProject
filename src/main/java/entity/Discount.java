package entity;

public enum Discount {
    FIVE_PERCENT(5),
    TEN_PERCENT(10),
    TWENTY_PERCENT(20);

    private final Integer value;

    Discount(final int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
