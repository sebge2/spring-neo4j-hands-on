package be.sgerard.test.neo4j.helper;

import be.sgerard.neo4j.model.service.ServiceEntity;
import be.sgerard.neo4j.model.service.ServiceType;
import be.sgerard.neo4j.service.service.ServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceTestHelper {

    private final ServiceManager serviceManager;
    private final IdTestHelper idTestHelper;

    @SuppressWarnings("UnusedReturnValue")
    public ServiceTestHelper defaultDataSet() {
        services()
                .create().java().and()
                .create().angular();

        return this;
    }

    public ServicesStep services() {
        return new ServicesStep();
    }

    public class ServicesStep {

        @SuppressWarnings("UnusedReturnValue")
        public ServicesStep deleteAll() {
            serviceManager.findAll().stream()
                    .map(ServiceEntity::getId)
                    .forEach(serviceManager::deleteById);

            return this;
        }

        public ServiceCreateStep create() {
            return new ServiceCreateStep(this);
        }
    }

    @RequiredArgsConstructor
    public class ServiceCreateStep {

        private final ServicesStep servicesStep;
        private String name;
        private ServiceType type;

        public ServiceStep angular() {
            return services()
                    .create()
                    .name("Angular")
                    .type(ServiceType.LANGUAGE)
                    .save();
        }

        public ServiceStep java() {
            return services()
                    .create()
                    .name("Java")
                    .type(ServiceType.LANGUAGE)
                    .save();
        }

        public ServiceCreateStep name(String name) {
            this.name = name;
            return this;
        }

        public ServiceCreateStep type(ServiceType type) {
            this.type = type;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public ServiceStep save() {
            final ServiceEntity service = serviceManager.create(c ->
                    c
                            .setName(name)
                            .setType(type)
            );

            idTestHelper.registerService(name, service.getId());

            return new ServiceStep(servicesStep, service);
        }
    }

    @RequiredArgsConstructor
    public class ServiceStep {

        private final ServicesStep servicesStep;
        private final ServiceEntity service;

        public ServicesStep and() {
            return servicesStep;
        }

        public ServiceEntity get() {
            return service;
        }

        @SuppressWarnings("unused")
        public String getId() {
            return get().getId();
        }
    }
}
