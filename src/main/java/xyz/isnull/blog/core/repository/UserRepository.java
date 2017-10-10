package xyz.isnull.blog.core.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import xyz.isnull.blog.core.entity.User;
import xyz.isnull.blog.core.repository.expand.ExpandJpaRepository;

@Repository
public interface UserRepository extends ExpandJpaRepository<User,Long>{

    User findByUsername(String username);
}
