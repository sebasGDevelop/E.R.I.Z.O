package dev.erizo.firewall.infrastructure.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The on-disk shape of a wave entry in the content file. */
record WaveFileDto(
        @JsonProperty("number") int number,
        @JsonProperty("groups") List<GroupFileDto> groups) {

    record GroupFileDto(
            @JsonProperty("enemyType") String enemyType,
            @JsonProperty("count") int count,
            @JsonProperty("intervalTicks") int intervalTicks,
            @JsonProperty("delayTicks") int delayTicks) {
    }
}
