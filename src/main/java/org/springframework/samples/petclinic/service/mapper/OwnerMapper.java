package org.springframework.samples.petclinic.service.mapper;

import org.mapstruct.*;
import org.springframework.samples.petclinic.domain.Owner;
import org.springframework.samples.petclinic.service.dto.OwnerDTO;

/**
 * Mapper for the entity {@link Owner} and its DTO {@link OwnerDTO}.
 */
@Mapper(componentModel = "spring")
public interface OwnerMapper extends EntityMapper<OwnerDTO, Owner> {}
