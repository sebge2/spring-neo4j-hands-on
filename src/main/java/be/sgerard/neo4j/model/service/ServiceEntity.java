package be.sgerard.neo4j.model.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node(value = "Service")
@Getter
@Setter
@Accessors(chain = true)
public class ServiceEntity {

    @Id
    @GeneratedValue
    private String id;

    @Property
    private String name;

    @Property
    private ServiceType type;
}
