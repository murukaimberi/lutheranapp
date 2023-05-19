package com.lutheran.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CongregantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Congregant.class);
        Congregant congregant1 = new Congregant();
        congregant1.setId(1L);
        Congregant congregant2 = new Congregant();
        congregant2.setId(congregant1.getId());
        assertThat(congregant1).isEqualTo(congregant2);
        congregant2.setId(2L);
        assertThat(congregant1).isNotEqualTo(congregant2);
        congregant1.setId(null);
        assertThat(congregant1).isNotEqualTo(congregant2);
    }
}
