// お知らせ一覧画面 固有のJavaScript

/**
 * 検索フォームのバリデーション
 */
function validateSearchForm(form) {
    const errors = [];

    // タイトル長さチェック
    const title = form.title.value || '';
    const titleError = validateMaxLength(title, 100, 'お知らせタイトル');
    if (titleError) errors.push(titleError);

    // 掲載日チェック
    const postDateVal = form.postDate.value.trim();
    if (postDateVal) {
        if (!isValidDateString(postDateVal)) {
            errors.push('掲載日はYYYY-MM-DD形式で入力してください。');
        }
    } else if (form.postDate.validity && form.postDate.validity.badInput) {
        errors.push('掲載日には正しい日付を入力してください。');
    }

    // 適用期間（開始）チェック
    const fromVal = form.from.value.trim();
    if (fromVal) {
        if (!isValidDateString(fromVal)) {
            errors.push('適用期間(開始)にはYYYY-MM-DD形式の日付を入力してください。');
        }
    } else if (form.from.validity && form.from.validity.badInput) {
        errors.push('適用期間(開始)には正しい日付を入力してください。');
    }

    // 適用期間（終了）チェック
    const toVal = form.to.value.trim();
    if (toVal) {
        if (!isValidDateString(toVal)) {
            errors.push('適用期間(終了)にはYYYY-MM-DD形式の日付を入力してください。');
        }
    } else if (form.to.validity && form.to.validity.badInput) {
        errors.push('適用期間(終了)には正しい日付を入力してください。');
    }

    // 日付範囲チェック
    if (!errors.length) {
        const rangeError = validateDateRange(fromVal, toVal);
        if (rangeError) errors.push(rangeError);
    }

    if (errors.length) {
        showErrorDialog(errors);
        return false;
    }

    return true;
}

/**
 * 行選択エラーを表示
 */
function showRowSelectionError() {
    showErrorDialog(['行を選択してください。'], '選択エラー');
}

/**
 * エラーダイアログを表示
 * @param {Array<string>} messages - エラーメッセージ配列
 * @param {string} heading - ダイアログタイトル（省略時は'入力エラー'）
 */
function showErrorDialog(messages, heading = '入力エラー') {
    const dialog = document.getElementById('errorDialog');
    const list = document.getElementById('errorList');
    const title = document.getElementById('errorDialogTitle');
    title.textContent = heading;
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
 */
function closeErrorDialog() {
    document.getElementById('errorDialog').style.display = 'none';
}

/**
 * 確認ダイアログを閉じる
 */
function closeConfirmDialog() {
    document.getElementById('confirmDialog').style.display = 'none';
}

/**
 * 成功ダイアログを閉じる
 */
function closeSuccessDialog() {
    document.getElementById('successDialog').style.display = 'none';
}

/**
 * 確認ダイアログを開く
 */
function openConfirmDialog() {
    document.getElementById('confirmDialog').style.display = 'flex';
}

/**
 * 削除確認後のアクション
 */
function confirmDeleteAction() {
    document.getElementById('confirmDialog').style.display = 'none';
    document.getElementById('actionForm').submit();
}

/**
 * 成功ダイアログを表示
 * @param {string} message - 成功メッセージ
 */
function showSuccessDialog(message) {
    document.getElementById('successMessage').textContent = message;
    document.getElementById('successDialog').style.display = 'flex';
}

/**
 * 成功ダイアログを閉じて検索を再実行
 */
function closeSuccessDialogAndRerun() {
    document.getElementById('rerunSearchForm').submit();
}


