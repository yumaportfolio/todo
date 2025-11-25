package com.example.todo.controller.validation;

/**
 * バリデーショングループ定義
 * 検証順序を制御するためのマーカーインターフェース
 */
public final class ValidationGroups {
    private ValidationGroups() {}

    /** 必須入力チェック用グループ */
    public interface Required {}

    /** 形式チェック用グループ（必須チェック後に実行） */
    public interface Format {}
}
