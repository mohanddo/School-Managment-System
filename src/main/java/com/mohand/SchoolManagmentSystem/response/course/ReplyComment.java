package com.mohand.SchoolManagmentSystem.response.course;

import com.mohand.SchoolManagmentSystem.response.user.UserPreview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReplyComment {
    private Long id;
    private String text;
    private String dateOfCreation;
    private UserPreview user;
    private Long upVotes;
    private Boolean hasCurrentUserUpVotedThisReplyComment;
    private Boolean userOwnsThisReplyComment;
}
