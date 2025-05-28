package com.mohand.SchoolManagmentSystem.response.chapter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resource {
    protected Long id;
    protected String title;
    protected String downloadUrl;
    protected String dateOfCreation;
    protected Boolean isFinished;
    protected Boolean free;
}
