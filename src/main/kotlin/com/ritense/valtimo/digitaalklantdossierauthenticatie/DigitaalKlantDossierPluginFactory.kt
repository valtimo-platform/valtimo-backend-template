package com.ritense.valtimo.digitaalklantdossierauthenticatie

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.digitaalklantdossierauthenticatie.DigitaalKlantDossierPlugin


class DigitaalKlantDossierPluginFactory(
    pluginService: PluginService
): PluginFactory<DigitaalKlantDossierPlugin>(pluginService) {

    override fun create(): DigitaalKlantDossierPlugin = DigitaalKlantDossierPlugin()
}