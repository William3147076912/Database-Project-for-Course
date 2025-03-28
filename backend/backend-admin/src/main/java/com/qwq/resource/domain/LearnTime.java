package com.qwq.resource.domain;

import lombok.Data;

@Data
public class LearnTime {
    Long studentId;
    Long resourceId;
    Long learnTime;
    boolean Finished;
}
