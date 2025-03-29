package com.qwq.resource.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LearnTime {
    Long studentId;
    Long resourceId;
    Long learnTime;
    @JsonProperty("Finished")
    Boolean Finished;
}
