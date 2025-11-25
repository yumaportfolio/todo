package com.example.todo.service;

import com.example.todo.domain.Notice;
import com.example.todo.repository.NoticeRepository;
import com.example.todo.service.query.NoticeSearchCondition;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * お知らせ管理のビジネスロジックを提供するサービスクラス
 * 検索、登録、更新、削除の処理を実装
 */
@Service
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository repo;

    public NoticeService(NoticeRepository repo) {
        this.repo = repo;
    }

    /**
     * お知らせを検索
     * @param condition 検索条件
     * @param pageable ページング情報
     * @return 検索結果ページ
     */
    public Page<Notice> search(NoticeSearchCondition condition, Pageable pageable) {
        return repo.findAll(buildSpecification(condition), pageable);
    }

    /**
     * 検索条件からJPA Specificationを構築
     */
    private Specification<Notice> buildSpecification(NoticeSearchCondition condition) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            addLikePredicate(predicates, condition.title(), root.get("title"), cb);
            addEqualsPredicate(predicates, condition.categoryCode(), root.get("categoryCode"), cb);
            addEqualsPredicate(predicates, condition.postDate(), root.get("postDate"), cb);
            addDateRangePredicate(predicates, condition.effectiveFrom(), root.get("startDate"), cb, true);
            addDateRangePredicate(predicates, condition.effectiveTo(), root.get("endDate"), cb, false);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * LIKE検索条件を追加（部分一致、大文字小文字区別なし）
     */
    private void addLikePredicate(List<Predicate> predicates, String value,
                                   jakarta.persistence.criteria.Path<String> path,
                                   jakarta.persistence.criteria.CriteriaBuilder cb) {
        if (value != null && !value.isBlank()) {
            predicates.add(cb.like(cb.lower(path), "%" + value.toLowerCase() + "%"));
        }
    }

    /**
     * 等価検索条件を追加
     */
    private <T> void addEqualsPredicate(List<Predicate> predicates, T value,
                                        jakarta.persistence.criteria.Path<T> path,
                                        jakarta.persistence.criteria.CriteriaBuilder cb) {
        if (value != null) {
            predicates.add(cb.equal(path, value));
        }
    }

    /**
     * 日付範囲検索条件を追加
     * @param isStart true:開始日以降, false:終了日以前
     */
    private void addDateRangePredicate(List<Predicate> predicates, LocalDate date,
                                       jakarta.persistence.criteria.Path<LocalDate> path,
                                       jakarta.persistence.criteria.CriteriaBuilder cb,
                                       boolean isStart) {
        if (date != null) {
            predicates.add(isStart
                ? cb.greaterThanOrEqualTo(path, date)
                : cb.lessThanOrEqualTo(path, date));
        }
    }

    /**
     * お知らせを削除
     * @param id お知らせID
     */
    @Transactional
    public void deleteById(Long id) {
        if (id != null) {
            repo.deleteById(id);
        }
    }

    /**
     * お知らせを新規作成
     * @param notice 作成するお知らせ
     * @return 作成されたお知らせ
     */
    @Transactional
    public Notice create(Notice notice) {
        LocalDateTime now = LocalDateTime.now();
        notice.setCreatedAt(now);
        notice.setUpdatedAt(now);
        return repo.save(notice);
    }

    /**
     * IDでお知らせを取得
     * @param id お知らせID
     * @return お知らせ
     * @throws IllegalArgumentException IDがnullまたは存在しない場合
     */
    public Notice findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Notice id is required");
        }
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found: " + id));
    }

    /**
     * お知らせを更新
     * @param notice 更新内容
     * @return 更新されたお知らせ
     * @throws IllegalArgumentException IDがnullまたは存在しない場合
     */
    @Transactional
    public Notice update(Notice notice) {
        if (notice.getId() == null) {
            throw new IllegalArgumentException("Notice id is required");
        }
        Notice existing = repo.findById(notice.getId())
                .orElseThrow(() -> new IllegalArgumentException("Notice not found: " + notice.getId()));

        updateNoticeFields(existing, notice);
        existing.setUpdatedAt(LocalDateTime.now());
        return repo.save(existing);
    }

    /**
     * お知らせのフィールドを更新
     */
    private void updateNoticeFields(Notice target, Notice source) {
        target.setTitle(source.getTitle());
        target.setCategoryCode(source.getCategoryCode());
        target.setPostDate(source.getPostDate());
        target.setStartDate(source.getStartDate());
        target.setEndDate(source.getEndDate());
        target.setContent(source.getContent());
    }
}
