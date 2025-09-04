package com.yulslab.cmms.memo.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "memo")
@Getter
@Setter
public class Memo {

    @EmbeddedId
    private MemoId id;

    private String memoName;
    private char isPinned;
    private int viewCnt;
    private String content;
    private String authorId;
    private String fileGroupId;
}
