package com.ritense.valtimo.practice.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService

class SamplePluginFactory(
    pluginService: PluginService,
    val sampleClient: SampleClient,
) : PluginFactory<SamplePlugin>(pluginService) {
    override fun create(): SamplePlugin {
        return SamplePlugin(sampleClient)
    }
}