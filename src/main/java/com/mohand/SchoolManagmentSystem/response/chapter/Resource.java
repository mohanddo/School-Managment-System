package com.mohand.SchoolManagmentSystem.response.chapter;

import com.mohand.SchoolManagmentSystem.response.course.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Resource {
    protected Long id;
    protected String title;
    protected String downloadUrl;
    protected String dateOfCreation;
    protected Boolean isFinished;
    protected Boolean free;
    protected List<Comment> comments;
}
