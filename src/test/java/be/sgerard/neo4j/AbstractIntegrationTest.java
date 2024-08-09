package be.sgerard.neo4j;

import be.sgerard.test.neo4j.Neo4jTestConfiguration;
import be.sgerard.test.neo4j.helper.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = SpringNeo4jHandsOnApplication.class)
@AutoConfigureMockMvc
@Import(Neo4jTestConfiguration.class)
@ComponentScan({"be.sgerard.neo4j", "be.sgerard.test.neo4j"})
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @Autowired
    protected DataTestHelper data;

    @Autowired
    protected CompanyTestHelper company;

    @Autowired
    protected TeamTestHelper team;

    @Autowired
    protected ServiceTestHelper service;

    @Autowired
    protected PersonTestHelper person;

    @Autowired
    protected ProjectTestHelper project;

    @Autowired
    protected IdTestHelper id;

    @BeforeEach
    protected void setup() {
        id.reset();

        data.deleteAll();

        service.defaultDataSet();
        company.defaultDataSet();
    }

}
