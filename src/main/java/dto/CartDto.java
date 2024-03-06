package dto;

import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartDto {
	private ConcurrentHashMap<Long, OldItemsDto> itemsDtoMap;
	private String email;
}
