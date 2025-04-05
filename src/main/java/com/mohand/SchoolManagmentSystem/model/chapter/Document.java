package com.mohand.SchoolManagmentSystem.model.chapter;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Document extends Resource {
    public Document(String title, String downloadUrl, Chapter chapter) {
        super(title, downloadUrl, chapter);
    }
}
