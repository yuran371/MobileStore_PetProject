package dto;

import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class SellHistoryDto {	// WiP

	private Long sellId;
	private final Long itemId;
	private final String email;
	private final Integer quantity;
	private final OffsetDateTime sellDate;
}
