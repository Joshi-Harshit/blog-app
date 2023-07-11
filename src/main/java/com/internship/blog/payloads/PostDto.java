package com.internship.blog.payloads;


import com.internship.blog.entities.Category;
import com.internship.blog.entities.Comment;
import com.internship.blog.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private int id;

    @NotEmpty
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Post must have a minimum length")
    private String content;
    private String imageName;
    private Date addedDate;
    private CategoryDto category;
    private UserDto user;

    private Set<CommentDto> comments = new HashSet<>();


}
