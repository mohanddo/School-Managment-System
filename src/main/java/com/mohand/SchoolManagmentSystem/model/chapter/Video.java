package com.mohand.SchoolManagmentSystem.model.chapter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Video extends Resource {


    public Video(String title, String downloadUrl, Chapter chapter, Integer duration, Boolean isFree) {
        super(title, downloadUrl, chapter, isFree);
        this.duration = duration;
    }

    @Column(nullable = false)
    private Integer duration;

}
