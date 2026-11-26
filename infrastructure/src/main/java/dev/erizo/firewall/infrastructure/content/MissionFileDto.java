package dev.erizo.firewall.infrastructure.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The on-disk shape of a mission entry in the content file. */
record MissionFileDto(
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("timeLimitTicks") int timeLimitTicks,
        @JsonProperty("items") List<String> items) {
}
