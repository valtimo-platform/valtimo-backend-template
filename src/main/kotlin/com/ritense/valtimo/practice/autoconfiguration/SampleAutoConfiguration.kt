package com.ritense.valtimo.practice.autoconfiguration

import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.practice.plugin.SampleClient
import com.ritense.valtimo.practice.plugin.SamplePluginFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class SampleAutoConfiguration {
    @Bean
    fun samplePluginFactory(
        pluginService: PluginService,
        sampleClient: SampleClient,
    ): SamplePluginFactory =
        SamplePluginFactory(
            pluginService = pluginService,
            sampleClient = sampleClient,
        )
}