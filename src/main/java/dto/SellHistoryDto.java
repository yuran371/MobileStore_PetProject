package dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class SellHistoryDto {

	private Long sellId;
	private final Long itemId;
	private final String email;
	private final Integer quantity;
	private final OffsetDateTime sellDate;
}
