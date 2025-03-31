package com.qwq.user.domain;

import com.qwq.course.domain.Course;
import lombok.Data;

import javax.crypto.interfaces.PBEKey;
import java.util.List;

@Data
public class User {
    public Long userId;
    public String userName;

    public String userRole;

    List<Course> courses;

}
