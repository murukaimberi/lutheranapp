package com.lutheran.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CongregantMapperTest {

    private CongregantMapper congregantMapper;

    @BeforeEach
    public void setUp() {
        congregantMapper = new CongregantMapperImpl();
    }
}
