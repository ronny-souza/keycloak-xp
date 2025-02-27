package br.com.marinholab.keycloakxp.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class InternationalizationConfiguration extends AcceptHeaderLocaleResolver {

    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("internationalization/messages");
        resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        resourceBundleMessageSource.setDefaultLocale(Locale.of("br"));
        return resourceBundleMessageSource;
    }

    @Override
    public @NonNull Locale resolveLocale(HttpServletRequest request) {
        List<Locale> locales = Arrays.asList(
                Locale.of("en_US"),
                Locale.of("es"),
                Locale.of("br")
        );

        String headerLanguage = request.getHeader("Accept-Language");
        return headerLanguage == null || headerLanguage.isEmpty() ?
                Locale.getDefault() :
                Locale.lookup(Locale.LanguageRange.parse(headerLanguage), locales);
    }
}
