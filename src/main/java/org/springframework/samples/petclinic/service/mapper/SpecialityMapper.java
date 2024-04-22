package org.springframework.samples.petclinic.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import org.springframework.samples.petclinic.domain.Speciality;
import org.springframework.samples.petclinic.domain.Vet;
import org.springframework.samples.petclinic.service.dto.SpecialityDTO;
import org.springframework.samples.petclinic.service.dto.VetDTO;

/**
 * Mapper for the entity {@link Speciality} and its DTO {@link SpecialityDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialityMapper extends EntityMapper<SpecialityDTO, Speciality> {
    @Mapping(target = "vets", source = "vets", qualifiedByName = "vetIdSet")
    SpecialityDTO toDto(Speciality s);

    @Mapping(target = "vets", ignore = true)
    @Mapping(target = "removeVet", ignore = true)
    Speciality toEntity(SpecialityDTO specialityDTO);

    @Named("vetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VetDTO toDtoVetId(Vet vet);

    @Named("vetIdSet")
    default Set<VetDTO> toDtoVetIdSet(Set<Vet> vet) {
        return vet.stream().map(this::toDtoVetId).collect(Collectors.toSet());
    }
}
