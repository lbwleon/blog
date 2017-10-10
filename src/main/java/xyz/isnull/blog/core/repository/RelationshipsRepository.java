package xyz.isnull.blog.core.repository;

import org.springframework.stereotype.Repository;
import xyz.isnull.blog.core.entity.Relationships;
import xyz.isnull.blog.core.repository.expand.ExpandJpaRepository;


@Repository
public interface RelationshipsRepository extends ExpandJpaRepository<Relationships, Long>{

    void deleteByCidAndType(Long cid, String type);

    void deleteByCid(Long id);

}
