package com.asr.project.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @Size(min = 4, message = "Title must be of min 4 character !!")
    private String title;

    @NotBlank(message = "Description is required !!")
    private String description;

    @Pattern(regexp = "(.*)|(.+\\.(gif|jpe?g|png))", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid Image Name !!")
    private String coverImage;
}
