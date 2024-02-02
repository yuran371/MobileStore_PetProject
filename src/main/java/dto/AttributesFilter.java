package dto;

import entity.enums.Attributes;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AttributesFilter {
    Attributes.BrandEnum brand;
    Attributes.OperatingSystemEnum os;
    Attributes.InternalMemoryEnum internalMemory;
    Attributes.RamEnum ram;
}
