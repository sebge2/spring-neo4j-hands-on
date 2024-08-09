package be.sgerard.neo4j.model.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Getter
@Setter
@Accessors(chain = true)
public class ServiceLinkEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Property
    private ServiceUsageState state;

    @Property
    private String strategy;

    @Property
    private String objectives;

    @Property
    private String difficulties;

    @Property
    private String notes;

    @TargetNode
    private ServiceEntity service;

}
