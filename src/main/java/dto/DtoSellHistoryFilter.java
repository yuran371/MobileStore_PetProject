package dto;

import entity.ItemsEntity;
import entity.PersonalAccountEntity;

public record DtoSellHistoryFilter(ItemsEntity items, PersonalAccountEntity personalAccount, Integer quantity) {

}
