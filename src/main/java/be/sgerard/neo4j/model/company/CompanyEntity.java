package be.sgerard.neo4j.model.company;

import be.sgerard.neo4j.model.team.TeamEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.Objects;

@Node(value = "Company")
@Getter
@Setter
public class CompanyEntity {

    @Id
    @GeneratedValue
    private String id;

    @Property
    private String name;

    @Relationship(type = "partOf", direction = Relationship.Direction.OUTGOING)
    private TeamEntity rootTeam;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CompanyEntity that = (CompanyEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyEntity{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
