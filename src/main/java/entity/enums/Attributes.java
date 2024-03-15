package entity.enums;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Attributes {
    public enum BrandEnum {
        APPLE("Apple"),
        SAMSUNG("Samsung"),
        XIAOMI("Xiaomi"),
        HONOR("HONOR"),
        HUAWEI("HUAWEI"),
        GOOGLE("Google"),
        ONEPLUS("ONEPLUS"),
        OPPO("OPPO"),
        POCO("POCO"),
        REALME("realme"),
        TECNO("Tecno"),
        VIVO("Vivo"),
        NOTHING("Nothing");

        private String brand;

        BrandEnum(String brand) {
            this.brand = brand;
        }

        public String getBrand() {
            return brand;
        }
    }

    public enum InternalMemoryEnum {
        GB_16("16"),
        GB_32("32"),
        GB_64("64"),
        GB_128("128"),
        GB_256("256"),
        GB_512("512"),
        GB_1024("1024");

        private String internalMemory;

        InternalMemoryEnum(String internalMemory) {
            this.internalMemory = internalMemory;
        }

        public String getInternalMemory() {
            return internalMemory;
        }

    }

    public enum RamEnum {
        gb_2("2"),
        gb_3("3"),
        gb_4("4"),
        gb_6("6"),
        gb_8("8"),
        gb_12("12"),
        gb_16("16");

        private String ram;

        RamEnum(String ram) {
            this.ram = ram;
        }

        public String getRam() {
            return ram;
        }

    }

    public enum OperatingSystemEnum {
        ANDROID("Android"), IOS("iOS");
        private String os;

        OperatingSystemEnum(String os) {
            this.os = os;
        }

        public String getOs() {
            return os;
        }
    }
}

