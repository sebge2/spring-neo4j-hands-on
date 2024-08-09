package be.sgerard.test.neo4j;

import lombok.RequiredArgsConstructor;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class Neo4jTestConfiguration {

    @Bean(destroyMethod = "close")
    @SuppressWarnings("resource")
    Neo4jContainer<?> neo4jContainer(@Value("${spring.neo4j.image}") String image) {
        final Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>(DockerImageName.parse(image))
                .withAdminPassword("somePassword");

        neo4jContainer.start();

        return neo4jContainer;
    }

    @Bean
    Driver driver(Neo4jContainer<?> container) {
        return GraphDatabase.driver(
                container.getBoltUrl(),
                AuthTokens.basic("neo4j", container.getAdminPassword())
        );
    }
}
