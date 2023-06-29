package com.kingname.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor @Getter
@AllArgsConstructor
public class Notification {

    private Long id;
    private LocalDateTime notifyAt;
    private String title;
    private User writer;
    private LocalDateTime createdAt;
}
