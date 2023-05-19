package com.lutheran.app.service.mapper;

import com.lutheran.app.domain.BaptismHistory;
import com.lutheran.app.service.dto.BaptismHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BaptismHistory} and its DTO {@link BaptismHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface BaptismHistoryMapper extends EntityMapper<BaptismHistoryDTO, BaptismHistory> {}
