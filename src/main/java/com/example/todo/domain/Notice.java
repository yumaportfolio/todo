package com.example.todo.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * お知らせエンティティ
 * t_noticeテーブルと対応
 */
@Entity
@Table(name = "t_notice")
public class Notice {
    /** お知らせID（主キー） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** タイトル */
    @Column(length = 100, nullable = false)
    private String title;

    /** お知らせ区分コード */
    @Column(name = "category_cd", length = 4)
    private String categoryCode;

    /** 掲載日 */
    @Column(name = "post_date")
    private LocalDate postDate;

    /** 適用開始日 */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** 適用終了日 */
    @Column(name = "end_date")
    private LocalDate endDate;

    /** 内容 */
    @Lob
    @Column(name = "content")
    private String content;

    /** 作成日時 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** 更新日時 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ===================================
    // Getters and Setters
    // ===================================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public LocalDate getPostDate() { return postDate; }
    public void setPostDate(LocalDate postDate) { this.postDate = postDate; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
