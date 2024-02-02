package entity.enums;

public enum DiscountEnum {
    FIVE_PERCENT(5),
    TEN_PERCENT(10),
    TWENTY_PERCENT(20);

    private final Integer value;

    DiscountEnum(final int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
