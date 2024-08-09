package be.sgerard.neo4j.model.person;

import be.sgerard.neo4j.model.team.TeamMemberEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.*;

@Node(value = "Person")
@Getter
@Setter
@Accessors(chain = true)
public class PersonEntity {

    @Id
    @GeneratedValue
    private String id;

    @Property
    private String firstName;

    @Property
    private String lastName;

    @Property
    private String email;

    @Property
    private String notes;

    @Relationship(type = "memberOf", direction = Relationship.Direction.INCOMING)
    private TeamMemberEntity team;

    public String getName(){
        return "%s %s".formatted(firstName, lastName);
    }

    @Override
    public String toString() {
        return "PersonEntity{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", id=" + id +
                '}';
    }
}
