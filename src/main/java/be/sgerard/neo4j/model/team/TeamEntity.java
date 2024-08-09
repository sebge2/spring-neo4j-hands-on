package be.sgerard.neo4j.model.team;

import be.sgerard.neo4j.model.company.CompanyEntity;
import be.sgerard.neo4j.model.project.ProjectEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Node(value = "Team")
@Getter
@Setter
@Accessors(chain = true)
public class TeamEntity {

    @Id
    @GeneratedValue
    private String id;

    @Relationship(type = "composedOf", direction = Relationship.Direction.INCOMING)
    private TeamEntity parentTeam; // sub-team

    @Relationship(type = "partOf", direction = Relationship.Direction.INCOMING)
    private CompanyEntity company; // root-team

    @Property
    private String name;

    @Relationship(type = "composedOf", direction = Relationship.Direction.OUTGOING)
    private Set<TeamEntity> subTeams = new HashSet<>();

    @Relationship(type = "memberOf")
    private Set<TeamMemberEntity> members = new HashSet<>();

    @Relationship(type = "workOn", direction = Relationship.Direction.OUTGOING)
    private Set<ProjectEntity> projects = new HashSet<>();

    @Property
    private String notes;

    @Override
    public String toString() {
        return "TeamEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TeamEntity that = (TeamEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
