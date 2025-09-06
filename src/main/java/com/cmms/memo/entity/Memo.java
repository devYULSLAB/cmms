package com.cmms.memo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "memo")
@IdClass(MemoIdClass.class)
@DynamicInsert
@DynamicUpdate
public class Memo {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "memo_id", length = 10, nullable = false)
    private String memoId;

    @Column(name = "memo_name", length = 100, nullable = false)
    private String memoName;

    @Column(name = "isPinned", length = 1, nullable = false)
    private String isPinned;

    @Column(name = "view_cnt", nullable = false)
    private Integer viewCnt;

    @Lob
    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @Column(name = "author_id", length = 5, nullable = false)
    private String authorId;

    @Column(name = "file_group_id", length = 10)
    private String fileGroupId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MemoComment> commentList = new ArrayList<>();
}
