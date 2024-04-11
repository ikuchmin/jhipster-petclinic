package org.springframework.samples.petclinic.repository;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.function.Consumer;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.IntegrationDataJpaTest;
import org.springframework.samples.petclinic.IntegrationTest;
import org.springframework.samples.petclinic.domain.Owner;
import org.springframework.samples.petclinic.domain.Pet;
import org.springframework.samples.petclinic.domain.PetType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@IntegrationDataJpaTest
@TestPropertySource(properties = { "spring.jpa.properties.hibernate.generate_statistics=true" })
class OwnerRepositoryIT {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private Statistics statistics;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        statistics = sessionFactory.getStatistics();
        statistics.clear();
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    void shouldAllocateIdsOnlyOnceBetweenSessions() {
        // given
        Consumer<TransactionStatus> load = st -> {
            Owner owner = ownerRepository.saveAndFlush(new Owner().firstName("John").lastName("Doe"));
            PetType type = petTypeRepository.saveAndFlush(new PetType().name("Dog"));
            petRepository.saveAndFlush(new Pet().name("Max").owner(owner).type(type));

            st.setRollbackOnly();
        };

        // when
        transactionTemplate.executeWithoutResult(load);
        out.println("\nTransaction Boundaries\n");
        transactionTemplate.executeWithoutResult(load);

        // then
        //assertThat(statistics.getPrepareStatementCount()).isEqualTo(7); // (+1)
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(9); // (+1)
        //assertThat(statistics.getPrepareStatementCount()).isEqualTo(12); // (+1)
        //assertThat(statistics.getPrepareStatementCount()).isEqualTo(0);
    }
}
