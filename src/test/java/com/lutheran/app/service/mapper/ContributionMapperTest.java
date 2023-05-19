package com.lutheran.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContributionMapperTest {

    private ContributionMapper contributionMapper;

    @BeforeEach
    public void setUp() {
        contributionMapper = new ContributionMapperImpl();
    }
}
