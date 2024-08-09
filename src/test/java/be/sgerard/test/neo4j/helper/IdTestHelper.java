package be.sgerard.test.neo4j.helper;

import be.sgerard.neo4j.model.company.CompanyEntity;
import be.sgerard.neo4j.model.person.PersonEntity;
import be.sgerard.neo4j.model.project.ProjectEntity;
import be.sgerard.neo4j.model.service.ServiceEntity;
import be.sgerard.neo4j.model.service.ServiceLinkEntity;
import be.sgerard.neo4j.model.team.TeamEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.emptyMap;

@Component
public class IdTestHelper {

    private final Map<Class<?>, Map<String, Object>> ids = new HashMap<>();

    @SuppressWarnings("UnusedReturnValue")
    public IdTestHelper reset() {
        ids.clear();

        return this;
    }

    public IdTestHelper register(Class<?> type, String hint, Object value) {
        ids.putIfAbsent(type, new HashMap<>());

        ids.get(type).put(hint, value);

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public IdTestHelper registerCompany(String hint, String value) {
        return register(CompanyEntity.class, hint, value);
    }

    @SuppressWarnings("UnusedReturnValue")
    public IdTestHelper registerTeam(String hint, String value) {
        return register(TeamEntity.class, hint, value);
    }

    @SuppressWarnings("UnusedReturnValue")
    public IdTestHelper registerService(String hint, String value) {
        return register(ServiceEntity.class, hint, value);
    }

    @SuppressWarnings("UnusedReturnValue")
    public IdTestHelper registerPerson(String hint, String value) {
        return register(PersonEntity.class, hint, value);
    }

    @SuppressWarnings("UnusedReturnValue")
    public IdTestHelper registerProject(String hint, String value) {
        return register(ProjectEntity.class, hint, value);
    }

    @SuppressWarnings("UnusedReturnValue")
    public IdTestHelper registerServiceLink(String hint, long value) {
        return register(ServiceLinkEntity.class, hint, value);
    }

    public Object getId(Class<?> type, String hint) {
        return ids.getOrDefault(type, emptyMap()).get(hint);
    }

    public String getCompanyId(String hint) {
        return Objects.toString(getId(CompanyEntity.class, hint));
    }

    public String getTeamId(String hint) {
        return Objects.toString(getId(TeamEntity.class, hint));
    }

    public String getServiceId(String hint) {
        return Objects.toString(getId(ServiceEntity.class, hint));
    }

    public String getPersonId(String hint) {
        return Objects.toString(getId(PersonEntity.class, hint));
    }

    public String getProjectId(String hint) {
        return Objects.toString(getId(ProjectEntity.class, hint));
    }
}
