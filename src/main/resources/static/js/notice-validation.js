// お知らせフォームバリデーション共通処理

/**
 * 日付文字列の形式と妥当性をチェック
 * @param {string} value - チェック対象の日付文字列 (YYYY-MM-DD形式)
 * @returns {boolean} 有効な日付の場合true
 */
function isValidDateString(value) {
    if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) {
        return false;
    }
    const [y, m, d] = value.split('-').map(Number);
    const date = new Date(y, m - 1, d);
    return date.getFullYear() === y && date.getMonth() === m - 1 && date.getDate() === d;
}

/**
 * 必須入力チェック
 * @param {string} value - チェック対象の値
 * @param {string} fieldName - フィールド名
 * @returns {string|null} エラーメッセージまたはnull
 */
function validateRequired(value, fieldName) {
    if (!value || value.trim() === '') {
        return `${fieldName}を入力してください。`;
    }
    return null;
}

/**
 * 最大文字数チェック
 * @param {string} value - チェック対象の値
 * @param {number} maxLength - 最大文字数
 * @param {string} fieldName - フィールド名
 * @returns {string|null} エラーメッセージまたはnull
 */
function validateMaxLength(value, maxLength, fieldName) {
    if (value && value.length > maxLength) {
        return `${fieldName}は${maxLength}文字以内で入力してください。`;
    }
    return null;
}

/**
 * 日付フィールドのバリデーション
 * @param {HTMLInputElement} field - 日付入力要素
 * @param {string} fieldName - フィールド名
 * @returns {string|null} エラーメッセージまたはnull
 */
function validateDateField(field, fieldName) {
    const value = field.value.trim();

    if (!value) {
        if (field.validity && field.validity.badInput) {
            return `${fieldName}には正しい日付を入力してください。`;
        }
        return `${fieldName}を入力してください。`;
    }

    if (!isValidDateString(value)) {
        return `${fieldName}には正しい日付を入力してください。`;
    }

    return null;
}

/**
 * 日付範囲のバリデーション
 * @param {string} startValue - 開始日 (YYYY-MM-DD形式)
 * @param {string} endValue - 終了日 (YYYY-MM-DD形式)
 * @returns {string|null} エラーメッセージまたはnull
 */
function validateDateRange(startValue, endValue) {
    if (startValue && endValue && isValidDateString(startValue) && isValidDateString(endValue)) {
        if (new Date(startValue) > new Date(endValue)) {
            return '適用期間は開始日が終了日より前になるよう入力してください。';
        }
    }
    return null;
}

/**
 * エラーダイアログを表示
 * @param {Array<string>} messages - エラーメッセージ配列
 * @param {string} dialogId - ダイアログ要素のID
 * @param {string} listId - エラーリスト要素のID
 */
function showErrorDialog(messages, dialogId = 'errorDialog', listId = 'errorList') {
    const dialog = document.getElementById(dialogId);
    const list = document.getElementById(listId);

    list.innerHTML = '';
    messages.forEach(msg => {
        const li = document.createElement('li');
        li.textContent = msg;
        list.appendChild(li);
    });

    dialog.style.display = 'flex';
}

/**
 * エラーダイアログを閉じる
 * @param {string} dialogId - ダイアログ要素のID
 */
function closeErrorDialog(dialogId = 'errorDialog') {
    document.getElementById(dialogId).style.display = 'none';
}

/**
 * お知らせフォームの統合バリデーション
 * @param {HTMLFormElement} form - フォーム要素
 * @returns {boolean} バリデーション結果
 */
function validateNoticeForm(form) {
    const errors = [];

    // タイトル
    const title = form.title.value.trim();
    const titleError = validateRequired(title, 'タイトル') || validateMaxLength(title, 100, 'タイトル');
    if (titleError) errors.push(titleError);

    // お知らせ区分
    if (!form.category.value) {
        errors.push('お知らせ区分を選択してください。');
    }

    // 掲載日
    const postDateError = validateDateField(form.postDate, '掲載日');
    if (postDateError) errors.push(postDateError);

    // 適用開始日
    const startDateError = validateDateField(form.startDate, '適用開始日');
    if (startDateError) errors.push(startDateError);

    // 適用終了日
    const endDateError = validateDateField(form.endDate, '適用終了日');
    if (endDateError) errors.push(endDateError);

    // 日付範囲チェック
    if (!errors.length) {
        const rangeError = validateDateRange(form.startDate.value.trim(), form.endDate.value.trim());
        if (rangeError) errors.push(rangeError);
    }

    // 内容
    const content = form.content.value.trim();
    if (!content) {
        errors.push('内容を入力してください。');
    }

    if (errors.length) {
        showErrorDialog(errors);
        return false;
    }

    return true;
}

// ブラウザデフォルトのバリデーションメッセージを無効化
document.addEventListener('invalid', function (event) {
    event.preventDefault();
}, true);

