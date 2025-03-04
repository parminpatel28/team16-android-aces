package com.munchies_backend.spring_boot.repository;

import com.munchies_backend.spring_boot.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
