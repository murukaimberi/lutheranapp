package com.lutheran.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarriageHistoryMapperTest {

    private MarriageHistoryMapper marriageHistoryMapper;

    @BeforeEach
    public void setUp() {
        marriageHistoryMapper = new MarriageHistoryMapperImpl();
    }
}
