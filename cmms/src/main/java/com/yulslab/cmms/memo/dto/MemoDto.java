package com.yulslab.cmms.memo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MemoDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdDate;
}
