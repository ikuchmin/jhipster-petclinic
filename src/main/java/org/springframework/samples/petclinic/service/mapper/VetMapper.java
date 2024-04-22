package org.springframework.samples.petclinic.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import org.springframework.samples.petclinic.domain.Speciality;
import org.springframework.samples.petclinic.domain.Vet;
import org.springframework.samples.petclinic.service.dto.SpecialityDTO;
import org.springframework.samples.petclinic.service.dto.VetDTO;

/**
 * Mapper for the entity {@link Vet} and its DTO {@link VetDTO}.
 */
@Mapper(componentModel = "spring")
public interface VetMapper extends EntityMapper<VetDTO, Vet> {
    @Mapping(target = "specialities", source = "specialities", qualifiedByName = "specialityIdSet")
    VetDTO toDto(Vet s);

    @Mapping(target = "removeSpecialities", ignore = true)
    Vet toEntity(VetDTO vetDTO);

    @Named("specialityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SpecialityDTO toDtoSpecialityId(Speciality speciality);

    @Named("specialityIdSet")
    default Set<SpecialityDTO> toDtoSpecialityIdSet(Set<Speciality> speciality) {
        return speciality.stream().map(this::toDtoSpecialityId).collect(Collectors.toSet());
    }
}
