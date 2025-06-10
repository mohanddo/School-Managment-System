package com.mohand.SchoolManagmentSystem.response.course;

import com.mohand.SchoolManagmentSystem.response.user.UserPreview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Comment {
    private Long id;
    private String text;
    private String dateOfCreation;
    private UserPreview user;
    private Long upVotes;
    private List<ReplyComment> replyComments;
    private Boolean hasCurrentUserUpVotedThisComment;
}
