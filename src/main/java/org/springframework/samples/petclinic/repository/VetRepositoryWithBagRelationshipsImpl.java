package org.springframework.samples.petclinic.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.samples.petclinic.domain.Vet;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class VetRepositoryWithBagRelationshipsImpl implements VetRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String VETS_PARAMETER = "vets";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Vet> fetchBagRelationships(Optional<Vet> vet) {
        return vet.map(this::fetchSpecialities);
    }

    @Override
    public Page<Vet> fetchBagRelationships(Page<Vet> vets) {
        return new PageImpl<>(fetchBagRelationships(vets.getContent()), vets.getPageable(), vets.getTotalElements());
    }

    @Override
    public List<Vet> fetchBagRelationships(List<Vet> vets) {
        return Optional.of(vets).map(this::fetchSpecialities).orElse(Collections.emptyList());
    }

    Vet fetchSpecialities(Vet result) {
        return entityManager
            .createQuery("select vet from Vet vet left join fetch vet.specialities where vet.id = :id", Vet.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Vet> fetchSpecialities(List<Vet> vets) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, vets.size()).forEach(index -> order.put(vets.get(index).getId(), index));
        List<Vet> result = entityManager
            .createQuery("select vet from Vet vet left join fetch vet.specialities where vet in :vets", Vet.class)
            .setParameter(VETS_PARAMETER, vets)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
