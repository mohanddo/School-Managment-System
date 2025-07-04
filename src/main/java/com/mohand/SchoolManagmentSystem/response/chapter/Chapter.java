package com.mohand.SchoolManagmentSystem.response.chapter;

import com.mohand.SchoolManagmentSystem.enums.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Chapter {
    private Long id;
    private String title;
    private LocalDateTime dateOfCreation;
    private List<Resource> resources;
}
