package com.example.todo.controller.form;

import com.example.todo.controller.ControllerConstants;
import com.example.todo.service.query.NoticeSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * お知らせ検索フォームのデータバインディング用クラス
 * 検索条件とページング情報を保持
 */
public class NoticeSearchForm {

    /** 日付フォーマット定数（ISO 8601形式） */
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    /** 検索条件：タイトル（部分一致） */
    private String title = "";

    /** 検索条件：お知らせ区分コード */
    private String category = "";

    /** 検索条件：掲載日 */
    private String postDate = "";

    /** 検索条件：適用期間開始日 */
    private String from = "";

    /** 検索条件：適用期間終了日 */
    private String to = "";

    /** ページ番号（0始まり） */
    private int page = ControllerConstants.DEFAULT_PAGE_NUMBER;

    /** ページサイズ */
    private int size = ControllerConstants.DEFAULT_PAGE_SIZE;

    /** 検索実行フラグ */
    private boolean searched;

    /**
     * フォーム入力値から検索条件オブジェクトを生成
     */
    public NoticeSearchCondition toCondition() {
        return new NoticeSearchCondition(
                blankToNull(title),
                blankToNull(category),
                parseDate(postDate),
                parseDate(from),
                parseDate(to)
        );
    }

    /**
     * ページング情報を生成（降順: 掲載日, ID）
     */
    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.Direction.DESC, "postDate", "id");
    }

    /**
     * ページング値を正常範囲に正規化
     */
    public void normalizePaging() {
        page = Math.max(page, ControllerConstants.DEFAULT_PAGE_NUMBER);
        size = Math.clamp(size, ControllerConstants.MIN_PAGE_SIZE, ControllerConstants.MAX_PAGE_SIZE);
    }

    /**
     * ページ検索結果からページング値を更新
     */
    public void refreshFrom(Page<?> result) {
        page = result.getNumber();
        size = result.getSize();
    }

    /**
     * 検索を実行すべきかチェック
     */
    public boolean shouldSearch() {
        return searched || hasCriteria();
    }

    /**
     * 検索条件が入力されているかチェック
     */
    public boolean hasCriteria() {
        return hasText(title) || hasText(category) || hasText(postDate) || hasText(from) || hasText(to);
    }

    // ===================================
    // Getters and Setters
    // ===================================
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = defaultString(title); }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = defaultString(category); }

    public String getPostDate() { return postDate; }
    public void setPostDate(String postDate) { this.postDate = defaultString(postDate); }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = defaultString(from); }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = defaultString(to); }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public boolean isSearched() { return searched; }
    public void setSearched(boolean searched) { this.searched = searched; }

    // ===================================
    // Helper Methods
    // ===================================
    /**
     * リダイレクト時に検索条件とページング情報をクエリパラメータとして渡す
     */
    public void copyQueryParamsTo(RedirectAttributes ra) {
        ra.addAttribute("title", defaultString(title));
        ra.addAttribute("category", defaultString(category));
        ra.addAttribute("postDate", defaultString(postDate));
        ra.addAttribute("from", defaultString(from));
        ra.addAttribute("to", defaultString(to));
        ra.addAttribute("page", Math.max(page, ControllerConstants.DEFAULT_PAGE_NUMBER));
        ra.addAttribute("size", Math.clamp(size, ControllerConstants.MIN_PAGE_SIZE, ControllerConstants.MAX_PAGE_SIZE));
        ra.addAttribute("searched", shouldSearch());
    }

    /**
     * 文字列に値が設定されているかチェック
     */
    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * 空白文字列をnullに変換
     */
    private String blankToNull(String value) {
        return hasText(value) ? value : null;
    }

    /**
     * nullを空文字列に変換
     */
    private String defaultString(String value) {
        return value == null ? "" : value;
    }

    /**
     * 文字列をLocalDateに変換（パースエラー時はnullを返す）
     */
    private LocalDate parseDate(String value) {
        if (!hasText(value)) {
            return null;
        }
        try {
            return LocalDate.parse(value, ISO_DATE);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
