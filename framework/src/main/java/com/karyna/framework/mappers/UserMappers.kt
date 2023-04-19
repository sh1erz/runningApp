package com.karyna.framework.mappers

import com.karyna.framework.dto.LocalUser
import com.karyna.framework.dto.RemoteUser
import com.karyna.core.domain.User as DomainUser

fun localUserToDomain(user: LocalUser) = with(user) {
    DomainUser(
        id = id,
        email = email,
        name = name,
        avatarUrl = avatarUrl,
        weight = weight
    )
}

fun domainUserToLocal(user: DomainUser) = with(user) {
    LocalUser(
        id = id,
        email = email,
        name = name,
        avatarUrl = avatarUrl,
        weight = weight
    )
}

fun remoteUserToDomain(user: RemoteUser) = with(user) {
    DomainUser(
        id = id,
        email = email,
        name = name,
        avatarUrl = avatarUrl,
        weight = weight
    )
}