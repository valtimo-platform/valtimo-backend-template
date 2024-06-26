package com.ritense.valtimo.digitaalklantdossierauthenticatie

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService

class DigitaalKlantDossierPluginFactory(
    pluginService: PluginService,
) : PluginFactory<DigitaalKlantDossierPlugin>(pluginService) {
    override fun create(): DigitaalKlantDossierPlugin = DigitaalKlantDossierPlugin()
}