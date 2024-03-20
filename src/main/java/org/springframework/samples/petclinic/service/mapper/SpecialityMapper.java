package org.springframework.samples.petclinic.service.mapper;

import org.mapstruct.*;
import org.springframework.samples.petclinic.domain.Speciality;
import org.springframework.samples.petclinic.service.dto.SpecialityDTO;

/**
 * Mapper for the entity {@link Speciality} and its DTO {@link SpecialityDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialityMapper extends EntityMapper<SpecialityDTO, Speciality> {}
