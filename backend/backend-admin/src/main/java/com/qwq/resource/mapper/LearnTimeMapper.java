package com.qwq.resource.mapper;

import com.qwq.resource.domain.LearnTime;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LearnTimeMapper {
    LearnTime selectLearnTime(LearnTime learnTime);
    int insertLearnTime(LearnTime learnTime);
    int updateLearnTime(LearnTime learnTime);
}
