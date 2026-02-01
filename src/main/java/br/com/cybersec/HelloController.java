package br.com.cybersec;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/public")
    public String pub() {
        return "OK (public)";
    }

    @GetMapping("/private")
    public String priv(Principal principal) {
        return "Hello (OIDC login), " + principal.getName();
    }

    @GetMapping("/token")
    public String token(@RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient client) {
        return client.getAccessToken().getTokenValue();
    }

    @GetMapping("/api/public")
    public String apiPub() {
        return "OK (API public)";
    }

    @GetMapping("/api/private")
    public String apiPriv(Authentication auth) {
        return "Hello (API JWT), " + auth.getName();
    } 

}
