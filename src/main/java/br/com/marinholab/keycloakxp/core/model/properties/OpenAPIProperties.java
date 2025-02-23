package br.com.marinholab.keycloakxp.core.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("openapi.info")
public class OpenAPIProperties {

    private String title;
    private String description;
    private String version;
    private String developer;
    private String developerUrl;
    private String developerContact;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDeveloperUrl() {
        return developerUrl;
    }

    public void setDeveloperUrl(String developerUrl) {
        this.developerUrl = developerUrl;
    }

    public String getDeveloperContact() {
        return developerContact;
    }

    public void setDeveloperContact(String developerContact) {
        this.developerContact = developerContact;
    }
}
