package com.internship.blog.payloads;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    private int categoryId;

    @NotEmpty(message = "You must give a category title")
    @Size(min = 10,message = "Title must be at least 10 characters")
    private String title;

    @NotEmpty
    @Size(max = 50, message = "Description must not exceed 30 characters")
    private String description;

}
