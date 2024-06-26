package com.ritense.valtimo.digitaalklantdossierauthenticatie.autoconfigure

import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.digitaalklantdossierauthenticatie.DigitaalKlantDossierPluginFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class DigitaalKlantDossierAutoConfiguration {
    @Bean
    fun digitaalKlantDossierPluginFactory(pluginService: PluginService): DigitaalKlantDossierPluginFactory =
        DigitaalKlantDossierPluginFactory(
            pluginService = pluginService,
        )
}