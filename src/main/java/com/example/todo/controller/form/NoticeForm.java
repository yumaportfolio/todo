package com.example.todo.controller.form;

import com.example.todo.controller.validation.ValidationGroups;
import com.example.todo.domain.Notice;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * お知らせ登録・更新フォームのデータバインディング用クラス
 * グループバリデーションにより、必須チェック→形式チェックの順で実施
 */
@GroupSequence({ValidationGroups.Required.class, ValidationGroups.Format.class, NoticeForm.class})
public class NoticeForm {
    /** お知らせID（更新時のみ使用） */
    private Long id;

    /** タイトル */
    @NotBlank(message = "タイトルを入力してください。", groups = ValidationGroups.Required.class)
    @Size(max = 100, message = "タイトルは100文字以内で入力してください。")
    private String title;

    /** お知らせ区分コード */
    @NotBlank(message = "お知らせ区分を選択してください。", groups = ValidationGroups.Required.class)
    private String category;

    /** 掲載日（文字列形式：YYYY-MM-DD） */
    @NotBlank(message = "掲載日を入力してください。", groups = ValidationGroups.Required.class)
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "掲載日には正しい日付を入力してください。", groups = ValidationGroups.Format.class)
    private String postDate;

    /** 適用開始日（文字列形式：YYYY-MM-DD） */
    @NotBlank(message = "適用開始日を入力してください。", groups = ValidationGroups.Required.class)
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "適用開始日には正しい日付を入力してください。", groups = ValidationGroups.Format.class)
    private String startDate;

    /** 適用終了日（文字列形式：YYYY-MM-DD） */
    @NotBlank(message = "適用終了日を入力してください。", groups = ValidationGroups.Required.class)
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "適用終了日には正しい日付を入力してください。", groups = ValidationGroups.Format.class)
    private String endDate;

    /** 内容 */
    @NotBlank(message = "内容を入力してください。", groups = ValidationGroups.Required.class)
    private String content;

    /**
     * NoticeエンティティからNoticeFormを生成
     */
    public static NoticeForm from(Notice notice) {
        NoticeForm form = new NoticeForm();
        form.setId(notice.getId());
        form.setTitle(notice.getTitle());
        form.setCategory(notice.getCategoryCode());
        form.setPostDate(dateToString(notice.getPostDate()));
        form.setStartDate(dateToString(notice.getStartDate()));
        form.setEndDate(dateToString(notice.getEndDate()));
        form.setContent(notice.getContent());
        return form;
    }

    /**
     * NoticeFormからNoticeエンティティを生成
     */
    public Notice toNotice() {
        Notice notice = new Notice();
        notice.setId(this.id);
        notice.setTitle(this.title);
        notice.setCategoryCode(this.category);
        notice.setPostDate(postDateValue());
        notice.setStartDate(startDateValue());
        notice.setEndDate(endDateValue());
        notice.setContent(this.content);
        return notice;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPostDate() { return postDate; }
    public void setPostDate(String postDate) { this.postDate = postDate; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    /**
     * 日付文字列をLocalDateに変換
     */
    public LocalDate postDateValue() { return parseDate(this.postDate); }
    public LocalDate startDateValue() { return parseDate(this.startDate); }
    public LocalDate endDateValue() { return parseDate(this.endDate); }

    /**
     * LocalDateを文字列に変換（nullセーフ）
     */
    private static String dateToString(LocalDate date) {
        return date != null ? date.toString() : "";
    }

    /**
     * 文字列をLocalDateに変換（nullセーフ、パースエラー時はnull）
     */
    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
