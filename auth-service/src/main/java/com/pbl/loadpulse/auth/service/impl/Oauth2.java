package com.pbl.loadpulse.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2 {


    private OAuth2AuthorizedClientService authorizedClientService;

    public String getAccessToken(OidcUser oidcUser) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oidcUser.getAuthorizedParty(),
                oidcUser.getName()
        );
        return client.getAccessToken().getTokenValue();
    }
}
