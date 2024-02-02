package dto;

import lombok.Value;

@Value
public class AttributesFilter {
    int internalMemory;
    int ram;
    String brand;
    String os;
}
