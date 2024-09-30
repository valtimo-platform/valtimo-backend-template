package com.ritense.valtimo

import com.ritense.authorization.AuthorizationContext
import com.ritense.document.service.impl.JsonSchemaDocumentDefinitionService
import com.ritense.form.autodeployment.FormDefinitionDeploymentService
import mu.KotlinLogging
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class ApplicationReadyEventEventListener(
    val formDefinitionDeploymentService: FormDefinitionDeploymentService,
    val jsonSchemaDocumentDefinitionService: JsonSchemaDocumentDefinitionService,
    @Value("\${repo}") val repo: String
) {

    @EventListener(ApplicationReadyEvent::class)
    fun handle(event: ApplicationReadyEvent) {
        val gitHub: GitHub = GitHubBuilder().build()
        val repository = gitHub.getRepository(repo)
        val formDirectory = repository.getDirectoryContent("/resources/config/form")
        formDirectory.listIterator().forEach {
            logger.info { "${it.name}" }
            val form = repository.getFileContent(it.path)
            logger.info { "File content ${form.content}" }
            AuthorizationContext.runWithoutAuthorization {
                formDefinitionDeploymentService.deploy(it.name, form.content, false)
            }
        }

        val caseDefinitionDirectory = repository.getDirectoryContent("/resources/config/document/definition")
        caseDefinitionDirectory.listIterator().forEach {
            val documentDefinition = repository.getFileContent(it.path)
            logger.info { "${it.name}" }
            logger.info { "File content ${documentDefinition.content}" }
            AuthorizationContext.runWithoutAuthorization {
                jsonSchemaDocumentDefinitionService.deploy(documentDefinition.content)
            }
        }
    }

    private companion object {
        val logger = KotlinLogging.logger {}
    }
}