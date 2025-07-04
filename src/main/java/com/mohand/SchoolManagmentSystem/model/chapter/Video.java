package com.mohand.SchoolManagmentSystem.model.chapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohand.SchoolManagmentSystem.model.course.TeacherStudent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Video extends Resource {


    public Video(String title, String downloadUrl, Chapter chapter, Integer duration, Boolean isFree, int position) {
        super(title, downloadUrl, chapter, isFree, position);
        this.duration = duration;
    }

    @Column(nullable = false)
    private Integer duration;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VideoProgress> videosProgress;

}
