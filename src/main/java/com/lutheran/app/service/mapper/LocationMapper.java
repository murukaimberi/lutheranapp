package com.lutheran.app.service.mapper;

import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.Country;
import com.lutheran.app.domain.Location;
import com.lutheran.app.service.dto.CongregantDTO;
import com.lutheran.app.service.dto.CountryDTO;
import com.lutheran.app.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    @Mapping(target = "congregant", source = "congregant", qualifiedByName = "congregantSurname")
    LocationDTO toDto(Location s);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);

    @Named("congregantSurname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "surname", source = "surname")
    CongregantDTO toDtoCongregantSurname(Congregant congregant);
}
