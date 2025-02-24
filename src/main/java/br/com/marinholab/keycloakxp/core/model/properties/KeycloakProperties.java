package br.com.marinholab.keycloakxp.core.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties("keycloak")
public class KeycloakProperties {

    private String server;
    private String realm;
    private Map<String, KeycloakClientProperties> clients;

    public KeycloakClientProperties getDefaultClient() {
        return this.clients.get("default");
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public Map<String, KeycloakClientProperties> getClients() {
        return clients;
    }

    public void setClients(Map<String, KeycloakClientProperties> clients) {
        this.clients = clients;
    }
}
