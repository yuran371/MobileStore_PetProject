package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ImportantStatisticEntity {

    @Id
    @Setter(AccessLevel.NONE)
    private final Long id = 1L;
    @Builder.Default
    private Long allUsersCounter = 0L;
    @Builder.Default
    private Long premiumUsersCounter = 0L;
    @Builder.Default
    private Long salesCounter = 0L;
    @Builder.Default
    private Long itemsCounter = 0L;
}
