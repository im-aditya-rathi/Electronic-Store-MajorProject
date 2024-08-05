package com.asr.project.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponseMessage {

    private String imageName;
    private String message;
    private boolean success;
    private HttpStatus status;
}
