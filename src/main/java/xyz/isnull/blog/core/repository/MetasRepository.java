package xyz.isnull.blog.core.repository;

import org.springframework.stereotype.Repository;
import xyz.isnull.blog.core.entity.Metas;
import xyz.isnull.blog.core.repository.expand.ExpandJpaRepository;

import java.util.List;

@Repository
public interface MetasRepository extends ExpandJpaRepository<Metas, Long> {

    List<Metas> findByType(String type);

    Metas findByNameAndType(String name, String type);

}
