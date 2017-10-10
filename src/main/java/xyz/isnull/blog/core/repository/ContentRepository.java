package xyz.isnull.blog.core.repository;

import org.springframework.stereotype.Repository;
import xyz.isnull.blog.core.entity.Content;
import xyz.isnull.blog.core.repository.expand.ExpandJpaRepository;

@Repository
public interface ContentRepository extends ExpandJpaRepository<Content, Long> {

}
