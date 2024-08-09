package be.sgerard.neo4j.model.team;

import be.sgerard.neo4j.model.person.PersonEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

@RelationshipProperties
@Getter
@Setter
@Accessors(chain = true)
public class TeamMemberEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Property
    private List<TeamMemberRole> roles;

    @TargetNode
    private PersonEntity person;
}
