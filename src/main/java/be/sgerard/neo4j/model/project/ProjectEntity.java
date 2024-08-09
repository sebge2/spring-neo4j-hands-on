package be.sgerard.neo4j.model.project;

import be.sgerard.neo4j.model.service.ServiceLinkEntity;
import be.sgerard.neo4j.model.team.TeamEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Node(value = "Project")
@Getter
@Setter
@Accessors(chain = true)
public class ProjectEntity {

    @Id
    @GeneratedValue
    private String id;

    @Property
    private String name;

    @Relationship(type = "workOn", direction = Relationship.Direction.INCOMING)
    private TeamEntity team;

    @Relationship(type = "consume")
    private Set<ServiceLinkEntity> services = new HashSet<>();

    @Property
    private String strategy;

    @Property
    private String objectives;

    @Property
    private String difficulties;

    @Property
    private String notes;

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
