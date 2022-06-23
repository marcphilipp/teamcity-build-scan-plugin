package nu.studer.teamcity.buildscan.connection

import jetbrains.buildServer.serverSide.oauth.OAuthConnectionDescriptor
import jetbrains.buildServer.serverSide.oauth.OAuthProvider
import jetbrains.buildServer.web.openapi.PluginDescriptor
import spock.lang.Specification
import spock.lang.Unroll

import static nu.studer.teamcity.buildscan.connection.GradleEnterpriseConnectionConstants.ALLOW_UNTRUSTED_SERVER
import static nu.studer.teamcity.buildscan.connection.GradleEnterpriseConnectionConstants.COMMON_CUSTOM_USER_DATA_EXTENSION_VERSION
import static nu.studer.teamcity.buildscan.connection.GradleEnterpriseConnectionConstants.COMMON_CUSTOM_USER_DATA_PLUGIN_VERSION
import static nu.studer.teamcity.buildscan.connection.GradleEnterpriseConnectionConstants.GRADLE_ENTERPRISE_EXTENSION_VERSION
import static nu.studer.teamcity.buildscan.connection.GradleEnterpriseConnectionConstants.GRADLE_ENTERPRISE_PLUGIN_VERSION
import static nu.studer.teamcity.buildscan.connection.GradleEnterpriseConnectionConstants.GRADLE_ENTERPRISE_URL
import static nu.studer.teamcity.buildscan.connection.GradleEnterpriseConnectionConstants.GRADLE_PLUGIN_REPOSITORY_URL
import static nu.studer.teamcity.buildscan.connection.GradleEnterpriseConnectionConstants.INSTRUMENT_COMMAND_LINE_BUILD_STEP

class GradleEnterpriseConnectionProviderTest extends Specification {

    OAuthProvider connectionProvider

    void setup() {
        connectionProvider = new GradleEnterpriseConnectionProvider(Stub(PluginDescriptor))
    }

    @Unroll
    def "default version of #key is set"(String key) {
        when:
        def defaultProperties = connectionProvider.getDefaultProperties()

        then:
        defaultProperties.containsKey(key)

        where:
        key << [
            GRADLE_ENTERPRISE_PLUGIN_VERSION,
            COMMON_CUSTOM_USER_DATA_PLUGIN_VERSION,
            GRADLE_ENTERPRISE_EXTENSION_VERSION,
            COMMON_CUSTOM_USER_DATA_EXTENSION_VERSION
        ]
    }

    @Unroll
    def "description includes value of #parameter"(String parameter, String value, String text) {
        given:
        OAuthConnectionDescriptor connection = Stub()
        connection.getParameters() >> [(parameter): value]

        when:
        def description = connectionProvider.describeConnection(connection)

        then:
        description.contains("$text: $value")

        where:
        parameter                                 | value                         | text
        GRADLE_PLUGIN_REPOSITORY_URL              | 'https://plugins.example.com' | 'Gradle Plugin Repository URL'
        GRADLE_ENTERPRISE_URL                     | 'https://ge.example.com'      | 'Gradle Enterprise Server URL'
        ALLOW_UNTRUSTED_SERVER                    | 'true'                        | 'Allow Untrusted Server'
        GRADLE_ENTERPRISE_PLUGIN_VERSION          | '3.10.2'                      | 'Gradle Enterprise Gradle Plugin Version'
        COMMON_CUSTOM_USER_DATA_PLUGIN_VERSION    | '1.7.2'                       | 'Common Custom User Data Gradle Plugin Version'
        GRADLE_ENTERPRISE_EXTENSION_VERSION       | '1.14.3'                      | 'Gradle Enterprise Maven Extension Version'
        COMMON_CUSTOM_USER_DATA_EXTENSION_VERSION | '1.10.1'                      | 'Common Custom User Data Maven Extension Version'
        INSTRUMENT_COMMAND_LINE_BUILD_STEP        | 'true'                        | 'Instrument Command Line Build Steps'
    }

}
