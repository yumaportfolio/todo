package com.example.todo.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * お知らせ区分の列挙型
 * コードとラベルの対応を管理
 */
public enum NoticeCategory {
    INFO("0", "情報"),
    IMPORTANT("1", "重要");

    private final String code;
    private final String label;

    NoticeCategory(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    /**
     * すべての区分を不変リストとして取得（キャッシュ済み）
     */
    private static final List<NoticeCategory> OPTIONS = Collections.unmodifiableList(Arrays.asList(values()));

    /**
     * コードからラベルへの変換マップ（キャッシュ済み）
     */
    private static final Map<String, String> LABELS = Collections.unmodifiableMap(
            OPTIONS.stream().collect(Collectors.toMap(NoticeCategory::getCode, NoticeCategory::getLabel))
    );

    /**
     * すべての区分選択肢を取得
     */
    public static List<NoticeCategory> options() {
        return OPTIONS;
    }

    /**
     * コードとラベルのマップを取得
     */
    public static Map<String, String> labelMap() {
        return LABELS;
    }
}

