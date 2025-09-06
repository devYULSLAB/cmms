package com.cmms.memo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "memo_comment")
@IdClass(MemoCommentIdClass.class)
@DynamicInsert
@DynamicUpdate
public class MemoComment {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "memo_id", length = 10, nullable = false)
    private String memoId;

    @Id
    @Column(name = "comment_id", length = 10, nullable = false)
    private String commentId;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "author_id", length = 5, nullable = false)
    private String authorId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false),
            @JoinColumn(name = "memo_id", referencedColumnName = "memo_id", insertable = false, updatable = false)
    })
    private Memo memo;
}
