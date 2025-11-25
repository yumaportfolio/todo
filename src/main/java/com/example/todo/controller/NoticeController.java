package com.example.todo.controller;

import com.example.todo.controller.form.NoticeForm;
import com.example.todo.controller.form.NoticeSearchForm;
import com.example.todo.domain.Notice;
import com.example.todo.domain.NoticeCategory;
import com.example.todo.service.NoticeService;
import com.example.todo.service.query.NoticeSearchCondition;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * お知らせ管理機能のコントローラー
 * 一覧表示、検索、登録、更新、削除の処理を提供
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService service;

    public NoticeController(NoticeService service) {
        this.service = service;
    }

    /**
     * お知らせ区分の選択肢をモデルに追加
     */
    @ModelAttribute("categoryOptions")
    public Iterable<NoticeCategory> categoryOptions() {
        return NoticeCategory.options();
    }

    /**
     * お知らせ区分コードとラベルのマップをモデルに追加
     */
    @ModelAttribute("categoryLabels")
    public java.util.Map<String, String> categoryLabels() {
        return NoticeCategory.labelMap();
    }

    /**
     * お知らせ一覧画面を表示（検索機能付き）
     */
    @GetMapping
    public String list(@ModelAttribute("searchForm") NoticeSearchForm form, Model model, HttpServletRequest request) {
        form.normalizePaging();
        if (shouldStripQuery(request, form)) {
            return ControllerConstants.REDIRECT_NOTICE_LIST;
        }
        NoticeSearchCondition condition = form.toCondition();
        boolean runSearch = form.shouldSearch();

        Page<Notice> result = runSearch
                ? service.search(condition, form.toPageable())
                : Page.empty(form.toPageable());

        form.refreshFrom(result);
        form.setSearched(runSearch);
        model.addAttribute("page", result);
        model.addAttribute("showResults", runSearch);
        return ControllerConstants.VIEW_NOTICE_MAIN;
    }

    /**
     * 新規登録フォームを表示
     */
    @GetMapping("/new")
    public String createForm(@ModelAttribute(ControllerConstants.ATTR_NOTICE_FORM) NoticeForm form, Model model) {
        populateFormModel(model, ControllerConstants.MODE_CREATE);
        return ControllerConstants.VIEW_NOTICE_FORM;
    }

    /**
     * 更新フォームを表示
     * @param id 更新対象のお知らせID
     */
    @GetMapping("/edit")
    public String editForm(@RequestParam Long id, Model model) {
        Notice notice = service.findById(id);
        model.addAttribute(ControllerConstants.ATTR_NOTICE_FORM, NoticeForm.from(notice));
        populateFormModel(model, ControllerConstants.MODE_EDIT);
        return ControllerConstants.VIEW_NOTICE_FORM;
    }

    /**
     * お知らせを新規登録
     */
    @PostMapping
    public String create(@Valid @ModelAttribute(ControllerConstants.ATTR_NOTICE_FORM) NoticeForm form,
                         BindingResult bindingResult,
                         RedirectAttributes ra,
                         Model model) {
        return handleFormSubmission(form, bindingResult, ra, model, ControllerConstants.MODE_CREATE,
                () -> service.create(form.toNotice()));
    }

    /**
     * お知らせを更新
     */
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute(ControllerConstants.ATTR_NOTICE_FORM) NoticeForm form,
                         BindingResult bindingResult,
                         RedirectAttributes ra,
                         Model model) {
        return handleFormSubmission(form, bindingResult, ra, model, ControllerConstants.MODE_EDIT,
                () -> service.update(form.toNotice()));
    }

    /**
     * お知らせを削除
     */
    @PostMapping("/delete")
    public String delete(@RequestParam Long selectedId,
                         @ModelAttribute("searchForm") NoticeSearchForm form,
                         RedirectAttributes ra) {
        service.deleteById(selectedId);
        addCompletedMessage(ra);
        ra.addFlashAttribute(ControllerConstants.ATTR_RESULT_TYPE, ControllerConstants.RESULT_TYPE_DELETED);
        form.copyQueryParamsTo(ra);
        return ControllerConstants.REDIRECT_NOTICE_LIST;
    }

    /**
     * フォーム送信処理を統一的に処理
     * バリデーション → ビジネス処理 → リダイレクトの共通フロー
     */
    private String handleFormSubmission(NoticeForm form,
                                        BindingResult bindingResult,
                                        RedirectAttributes ra,
                                        Model model,
                                        String mode,
                                        Runnable action) {
        validateDateRange(form, bindingResult);
        if (bindingResult.hasErrors()) {
            populateFormModel(model, mode);
            return ControllerConstants.VIEW_NOTICE_FORM;
        }
        action.run();
        addCompletedMessage(ra);
        ra.addFlashAttribute(ControllerConstants.ATTR_RESULT_TYPE, mode + "d");
        return ControllerConstants.REDIRECT_NOTICE_LIST;
    }

    /**
     * フォーム画面に必要なモデル属性を設定
     */
    private void populateFormModel(Model model, String mode) {
        model.addAttribute(ControllerConstants.ATTR_MODE, mode);
    }

    /**
     * 適用期間の日付範囲をバリデーション（開始日 ≦ 終了日）
     */
    private void validateDateRange(NoticeForm form, BindingResult bindingResult) {
        LocalDate start = form.startDateValue();
        LocalDate end = form.endDateValue();
        if (start != null && end != null && start.isAfter(end)) {
            bindingResult.reject("EXXX3",
                    new Object[]{"適用期間", "開始日 ≦ 終了日"},
                    "適用期間は開始日が終了日より前になるよう入力してください。");
        }
    }

    /**
     * 処理完了メッセージをリダイレクト先に渡す
     */
    private void addCompletedMessage(RedirectAttributes ra) {
        ra.addFlashAttribute(ControllerConstants.ATTR_COMPLETED_MESSAGE, ControllerConstants.MSG_PROCESS_COMPLETED);
    }

    /**
     * クエリ文字列があるが検索条件が空の場合にリダイレクトが必要かチェック
     */
    private boolean shouldStripQuery(HttpServletRequest request, NoticeSearchForm form) {
        return request.getQueryString() != null && !form.shouldSearch();
    }
}
