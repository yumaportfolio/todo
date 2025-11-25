package com.example.todo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ルートパスのコントローラー
 * トップページからお知らせ一覧へリダイレクト
 */
@Controller
public class TodoController {
    @GetMapping("/")
    public String index() {
        return "redirect:/notice";
    }
}
