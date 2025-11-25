package com.example.todo.controller;

/**
 * コントローラー層で使用する定数を定義
 * ページング、モデル属性名、ビュー名などの共通定数を一元管理
 */
public final class ControllerConstants {

    private ControllerConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    // ===================================
    // ページング関連定数
    // ===================================
    /** デフォルトページ番号（0始まり） */
    public static final int DEFAULT_PAGE_NUMBER = 0;

    /** デフォルトページサイズ */
    public static final int DEFAULT_PAGE_SIZE = 100;

    /** 最小ページサイズ */
    public static final int MIN_PAGE_SIZE = 1;

    /** 最大ページサイズ */
    public static final int MAX_PAGE_SIZE = 100;

    // ===================================
    // モデル属性名
    // ===================================
    /** モード属性名（create/edit） */
    public static final String ATTR_MODE = "mode";

    /** お知らせフォーム属性名 */
    public static final String ATTR_NOTICE_FORM = "noticeForm";

    /** 完了メッセージ属性名 */
    public static final String ATTR_COMPLETED_MESSAGE = "completedMessage";

    /** 結果タイプ属性名 */
    public static final String ATTR_RESULT_TYPE = "resultType";

    /** 削除結果タイプ値 */
    public static final String RESULT_TYPE_DELETED = "deleted";

    // ===================================
    // モード値
    // ===================================
    /** 登録モード */
    public static final String MODE_CREATE = "create";

    /** 更新モード */
    public static final String MODE_EDIT = "edit";

    // ===================================
    // メッセージ
    // ===================================
    /** 処理完了メッセージ */
    public static final String MSG_PROCESS_COMPLETED = "処理が完了しました。";

    // ===================================
    // リダイレクトURL
    // ===================================
    /** お知らせ一覧へのリダイレクト */
    public static final String REDIRECT_NOTICE_LIST = "redirect:/notice";

    // ===================================
    // ビュー名
    // ===================================
    /** お知らせ一覧画面 */
    public static final String VIEW_NOTICE_MAIN = "notice-main";

    /** お知らせフォーム画面 */
    public static final String VIEW_NOTICE_FORM = "notice-form";
}
