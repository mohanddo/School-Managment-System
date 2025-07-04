package com.mohand.SchoolManagmentSystem.response.chapter;

import com.mohand.SchoolManagmentSystem.response.course.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
public class Resource {
    @Getter
    private Long id;

    @Getter
    private String title;

    @Getter
    private String downloadUrl;

    @Getter
    private String dateOfCreation;

    @Getter
    private Boolean isFinished;

    @Getter
    private Boolean free;

    @Getter
    private List<Comment> comments;

    @Getter
    private Integer duration;

    @Getter
    private Integer videoProgress;
}
