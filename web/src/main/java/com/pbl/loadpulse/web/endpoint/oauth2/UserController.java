package com.pbl.loadpulse.web.endpoint.oauth2;

import com.pbl.loadpulse.auth.service.impl.Oauth2;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class UserController {

//    private final OAuth2AuthorizedClientService authorizedClientService;
//
//    // Constructor injection
//    public UserController(OAuth2AuthorizedClientService authorizedClientService) {
//        this.authorizedClientService = authorizedClientService;
//    }
//    private final RestTemplate restTemplate;
//
//    public UserController(RestTemplateBuilder restTemplateBuilder) {
//        this.restTemplate = restTemplateBuilder.build();
//    }
//    private final OAuth2AuthorizedClientService authorizedClientService;

    private final Oauth2 oauth2;

    public UserController(Oauth2 oauth2) {
        this.oauth2 = oauth2;
    }

    //    public UserController(OAuth2AuthorizedClientService authorizedClientService) {
//        this.authorizedClientService = authorizedClientService;
//    }
    @GetMapping("/user")
    public Map<String,String> home(@AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken",oauth2.getAccessToken(oidcUser));
//        if (authentication != null) {
//            OAuth2AuthorizedClient client = getAuthorizedClient(authentication);
//            if (client != null) {
//                String accessToken = client.getAccessToken().getTokenValue();
//                // Store the access token in the map
//                tokenMap.put("accessToken", accessToken);
//            }
//        }

        return tokenMap;
    }

//    private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
//        return authorizedClientService.loadAuthorizedClient(
//                authentication.getAuthorizedClientRegistrationId(),
//                authentication.getName());
//    }

}
