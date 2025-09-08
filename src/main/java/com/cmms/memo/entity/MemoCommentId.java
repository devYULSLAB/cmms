package com.cmms.memo.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoCommentId implements Serializable {
    private String companyId;
    private String memoId;
    private String commentId;
}