package org.http4k.connect.openai.auth.oauth

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import org.http4k.connect.storage.Storage
import org.http4k.security.AccessToken
import org.http4k.security.oauth.core.RefreshToken
import org.http4k.security.oauth.server.AccessTokens
import org.http4k.security.oauth.server.AuthorizationCode
import org.http4k.security.oauth.server.ClientId
import org.http4k.security.oauth.server.TokenRequest
import org.http4k.security.oauth.server.UnsupportedGrantType
import org.http4k.security.oauth.server.accesstoken.AuthorizationCodeAccessTokenRequest
import java.time.Clock
import java.time.Duration
import java.time.Instant

fun StorageAccessTokens(
    tokenStorage: Storage<Instant>,
    strings: SecureStrings,
    validity: Duration,
    clock: Clock
) = object : AccessTokens {
    override fun create(clientId: ClientId, tokenRequest: TokenRequest) =
        Failure(UnsupportedGrantType("client_credentials"))

    override fun create(
        clientId: ClientId,
        tokenRequest: AuthorizationCodeAccessTokenRequest,
        authorizationCode: AuthorizationCode
    ) = Success(
        AccessToken(
            strings(),
            expiresIn = validity.seconds,
            scope = tokenRequest.scopes.joinToString(" "),
            refreshToken = RefreshToken(strings())
        ).also { tokenStorage[it.value] = clock.instant() + validity }
    )
}