package com.qwq.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Teacher extends User{
    Long courseCount;
    String teachingQuality="良好";
}
