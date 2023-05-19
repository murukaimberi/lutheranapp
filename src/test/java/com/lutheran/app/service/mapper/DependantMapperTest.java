package com.lutheran.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DependantMapperTest {

    private DependantMapper dependantMapper;

    @BeforeEach
    public void setUp() {
        dependantMapper = new DependantMapperImpl();
    }
}
