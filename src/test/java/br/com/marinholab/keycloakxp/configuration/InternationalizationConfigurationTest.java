package br.com.marinholab.keycloakxp.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InternationalizationConfigurationTest {

    private InternationalizationConfiguration internationalizationConfiguration;

    @BeforeEach
    void init() {
        this.internationalizationConfiguration = new InternationalizationConfiguration();
    }

    @Test
    @DisplayName("Should configure message source")
    void shouldConfigureMessageSource() {
        ResourceBundleMessageSource messageSource = this.internationalizationConfiguration.messageSource();
        assertNotNull(messageSource);
        assertTrue(messageSource.getBasenameSet().contains("internationalization/messages"));
    }

    @Test
    @DisplayName("Should return a valid locale based on accept language header")
    void shouldReturnValidLocaleBasedOnAcceptLanguageHeader() {
        HttpServletRequest httpServletRequestAsMock = mock(HttpServletRequest.class);

        when(httpServletRequestAsMock.getHeader("Accept-Language")).thenReturn("br");

        Locale locale = this.internationalizationConfiguration.resolveLocale(httpServletRequestAsMock);
        assertNotNull(locale);
    }

    @Test
    @DisplayName("Should return a valid locale when accept language header is null")
    void shouldReturnValidLocaleWhenAcceptLanguageHeaderIsNull() {
        HttpServletRequest httpServletRequestAsMock = mock(HttpServletRequest.class);

        Locale locale = this.internationalizationConfiguration.resolveLocale(httpServletRequestAsMock);
        assertNotNull(locale);
    }

    @Test
    @DisplayName("Should return a valid locale when accept language header is empty")
    void shouldReturnValidLocaleWhenAcceptLanguageHeaderIsEmpty() {
        HttpServletRequest httpServletRequestAsMock = mock(HttpServletRequest.class);
        when(httpServletRequestAsMock.getHeader("Accept-Language")).thenReturn("");

        Locale locale = this.internationalizationConfiguration.resolveLocale(httpServletRequestAsMock);
        assertNotNull(locale);
    }
}