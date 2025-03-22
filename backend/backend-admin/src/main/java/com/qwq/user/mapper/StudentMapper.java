package com.qwq.user.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper {
    public Long studentNumber();
    public Long studentLearningTime();
}
