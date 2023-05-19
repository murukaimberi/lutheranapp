package com.lutheran.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BaptismHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaptismHistory.class);
        BaptismHistory baptismHistory1 = new BaptismHistory();
        baptismHistory1.setId(1L);
        BaptismHistory baptismHistory2 = new BaptismHistory();
        baptismHistory2.setId(baptismHistory1.getId());
        assertThat(baptismHistory1).isEqualTo(baptismHistory2);
        baptismHistory2.setId(2L);
        assertThat(baptismHistory1).isNotEqualTo(baptismHistory2);
        baptismHistory1.setId(null);
        assertThat(baptismHistory1).isNotEqualTo(baptismHistory2);
    }
}
