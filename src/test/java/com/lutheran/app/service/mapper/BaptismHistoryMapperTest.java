package com.lutheran.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BaptismHistoryMapperTest {

    private BaptismHistoryMapper baptismHistoryMapper;

    @BeforeEach
    public void setUp() {
        baptismHistoryMapper = new BaptismHistoryMapperImpl();
    }
}
