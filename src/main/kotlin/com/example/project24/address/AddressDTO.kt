package com.example.project24.address

data class AddressDTO(
    val id: Long,
    val street: String,
    val houseNumber: String,
    val city: String,
    val postalCode: String,
    val userId: Long?,
    val storeId: Long?,
)

fun mapToAddressDTO(address: Address): AddressDTO {
    return AddressDTO(
        id = address.id,
        street = address.street,
        houseNumber = address.houseNumber,
        city = address.city,
        postalCode = address.postalCode,
        userId = address.user?.id,
        storeId = address.store?.id,
    )
}