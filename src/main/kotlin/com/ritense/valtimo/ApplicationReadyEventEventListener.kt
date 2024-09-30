package com.ritense.valtimo

import com.ritense.authorization.AuthorizationContext
import com.ritense.document.domain.event.DocumentDefinitionDeployedEvent
import com.ritense.document.service.impl.JsonSchemaDocumentDefinitionService
import com.ritense.form.autodeployment.FormDefinitionDeploymentService
import com.ritense.form.domain.event.FormCreatedEvent
import com.ritense.form.domain.event.FormUpdatedEvent
import com.ritense.valtimo.contract.utils.SecurityUtils
import mu.KotlinLogging
import org.kohsuke.github.GHRepository
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
    @Value("\${REPO}") val repo: String,
    @Value("\${OAUTHTOKEN}") val oAuthToken: String
) {

    private val gitHub: GitHub = GitHubBuilder().withOAuthToken(oAuthToken).build()
    private val repository: GHRepository = gitHub.getRepository(repo)

    @EventListener(FormCreatedEvent::class)
    fun handle(event: FormCreatedEvent) {
        logger.info { "FormCreatedEvent. $event" }
        if (SecurityUtils.getCurrentUserLogin() != null) {
            logger.info { "User created a new form" }
            val filename = "resources/config/form/${event.formDefinition.name}.json"
            repository.createContent()
                .path(filename)
                .content(event.formDefinition.formDefinition.toPrettyString())
                .message("Creating new form file via API, triggered by UI change")
                .commit()
            logger.info { "File created successfully." }
        }
    }

    @EventListener(FormUpdatedEvent::class)
    fun handle(event: FormUpdatedEvent) {
        logger.info { "FormUpdatedEvent. $event" }
        if (SecurityUtils.getCurrentUserLogin() != null) {
            logger.info { "User updated a form" }
            val filename = "resources/config/form/${event.formDefinition.name}.json"
            val existingFile = repository.getFileContent(filename)
            existingFile.update(
                event.formDefinition.formDefinition.toPrettyString(),
                "Updating form JSON file via API"
            )
            logger.info { "File updated successfully." }
        }
    }

    @EventListener(DocumentDefinitionDeployedEvent::class)
    fun handle(event: DocumentDefinitionDeployedEvent) {
        logger.info { "DocumentDefinitionDeployedEvent. $event" }
        if (SecurityUtils.getCurrentUserLogin() != null) {
            logger.info { "User created a new document-definition" }
            val filename = "resources/config/document/definition/${event.documentDefinition().id().name()}.json"
            repository.createContent()
                .path(filename)
                .content(event.documentDefinition().schema().toPrettyString())
                .message("Creating new document-definition file via API, triggered by UI change")
                .commit()
            logger.info { "File created successfully." }
        }
    }

    @EventListener(ApplicationReadyEvent::class)
    fun handle(event: ApplicationReadyEvent) {
        val formDirectory = repository.getDirectoryContent("resources/config/form")
        formDirectory.listIterator().forEach {
            logger.info { "${it.name}" }
            val form = repository.getFileContent(it.path)
            logger.info { "File content ${form.content}" }
            AuthorizationContext.runWithoutAuthorization {
                formDefinitionDeploymentService.deploy(it.name, form.content, false)
            }
        }

        val caseDefinitionDirectory = repository.getDirectoryContent("resources/config/document/definition")
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