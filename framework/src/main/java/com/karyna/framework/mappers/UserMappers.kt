package com.karyna.framework.mappers

import com.karyna.framework.dto.User
import com.karyna.core.domain.User as DomainUser

fun userToDomain(user: User) = with(user) {
    DomainUser(
        email = email,
        name = name,
        avatarUrl = avatarUrl,
        weight = weight
    )
}

fun userToDto(user: DomainUser) = with(user) {
    User(
        id = 0,
        email = email,
        name = name,
        avatarUrl = avatarUrl,
        weight = weight
    )
}