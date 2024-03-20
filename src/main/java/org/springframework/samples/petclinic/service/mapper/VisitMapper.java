package org.springframework.samples.petclinic.service.mapper;

import org.mapstruct.*;
import org.springframework.samples.petclinic.domain.Pet;
import org.springframework.samples.petclinic.domain.Vet;
import org.springframework.samples.petclinic.domain.Visit;
import org.springframework.samples.petclinic.service.dto.PetDTO;
import org.springframework.samples.petclinic.service.dto.VetDTO;
import org.springframework.samples.petclinic.service.dto.VisitDTO;

/**
 * Mapper for the entity {@link Visit} and its DTO {@link VisitDTO}.
 */
@Mapper(componentModel = "spring")
public interface VisitMapper extends EntityMapper<VisitDTO, Visit> {
    @Mapping(target = "vet", source = "vet", qualifiedByName = "vetId")
    @Mapping(target = "pet", source = "pet", qualifiedByName = "petId")
    VisitDTO toDto(Visit s);

    @Named("vetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VetDTO toDtoVetId(Vet vet);

    @Named("petId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PetDTO toDtoPetId(Pet pet);
}
