package com.qwq.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Student extends User{
    Long learningTime;
    int score;
}
