package org.springframework.samples.petclinic.service.mapper;

import org.mapstruct.*;
import org.springframework.samples.petclinic.domain.Owner;
import org.springframework.samples.petclinic.domain.Pet;
import org.springframework.samples.petclinic.domain.PetType;
import org.springframework.samples.petclinic.service.dto.OwnerDTO;
import org.springframework.samples.petclinic.service.dto.PetDTO;
import org.springframework.samples.petclinic.service.dto.PetTypeDTO;

/**
 * Mapper for the entity {@link Pet} and its DTO {@link PetDTO}.
 */
@Mapper(componentModel = "spring")
public interface PetMapper extends EntityMapper<PetDTO, Pet> {
    @Mapping(target = "type", source = "type", qualifiedByName = "petTypeId")
    @Mapping(target = "owner", source = "owner", qualifiedByName = "ownerId")
    PetDTO toDto(Pet s);

    @Named("petTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PetTypeDTO toDtoPetTypeId(PetType petType);

    @Named("ownerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OwnerDTO toDtoOwnerId(Owner owner);
}
