package com.lutheran.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dependant.class);
        Dependant dependant1 = new Dependant();
        dependant1.setId(1L);
        Dependant dependant2 = new Dependant();
        dependant2.setId(dependant1.getId());
        assertThat(dependant1).isEqualTo(dependant2);
        dependant2.setId(2L);
        assertThat(dependant1).isNotEqualTo(dependant2);
        dependant1.setId(null);
        assertThat(dependant1).isNotEqualTo(dependant2);
    }
}
