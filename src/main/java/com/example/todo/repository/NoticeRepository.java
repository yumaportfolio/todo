package com.example.todo.repository;

import com.example.todo.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * お知らせエンティティのリポジトリ
 * 標準CRUD操作とSpecificationによる動的検索をサポート
 */
public interface NoticeRepository extends JpaRepository<Notice, Long>, JpaSpecificationExecutor<Notice> {

}