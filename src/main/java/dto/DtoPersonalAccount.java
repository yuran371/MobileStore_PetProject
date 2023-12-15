package dto;

public record DtoPersonalAccount(Long accountId, String email, String name, String surname, String country, String city, String address,
		String phoneNumber) {
}
