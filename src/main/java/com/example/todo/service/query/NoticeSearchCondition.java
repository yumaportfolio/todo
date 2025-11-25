package com.example.todo.service.query;

import java.time.LocalDate;

/**
 * お知らせ検索条件
 * @param title タイトル（部分一致検索）
 * @param categoryCode お知らせ区分コード（完全一致検索）
 * @param postDate 掲載日（完全一致検索）
 * @param effectiveFrom 適用期間開始日（以降の検索）
 * @param effectiveTo 適用期間終了日（以前の検索）
 */
public record NoticeSearchCondition(
        String title,
        String categoryCode,
        LocalDate postDate,
        LocalDate effectiveFrom,
        LocalDate effectiveTo
) {
    /**
     * すべての検索条件が空かチェック
     * @return すべて空の場合true
     */
    @SuppressWarnings("unused") // 将来の拡張性のため保持
    public boolean isEmpty() {
        return isBlank(title) && isBlank(categoryCode) && postDate == null && effectiveFrom == null && effectiveTo == null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

