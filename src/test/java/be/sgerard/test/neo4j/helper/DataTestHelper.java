package be.sgerard.test.neo4j.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataTestHelper {

    private final Neo4jClient neo4jClient;

    public DataTestHelper deleteAll() {
        var query = """
                MATCH (n)
                DETACH DELETE n
                """;

        neo4jClient.query(query).run();

        return this;
    }

}
