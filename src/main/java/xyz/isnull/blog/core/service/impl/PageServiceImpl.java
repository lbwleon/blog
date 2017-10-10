package xyz.isnull.blog.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import xyz.isnull.blog.core.entity.Content;
import xyz.isnull.blog.core.model.PageQueryQo;
import xyz.isnull.blog.core.repository.ContentRepository;
import xyz.isnull.blog.core.service.PageService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class PageServiceImpl implements PageService{

    @Autowired
    private ContentRepository contentRepository;

    @Override
    public Page<Content> findContentByTag(PageQueryQo pageQueryQo) {

        Pageable pageable = new PageRequest(pageQueryQo.getPage(), pageQueryQo.getSize(), new Sort(Sort.Direction.DESC,"id"));

        String sql = "SELECT b.* FROM t_relationships a, t_content b WHERE a.cid = b.id AND a.mid = ? ";

        return contentRepository.findAllByNativeSql(sql, pageable, Content.class, new Object[]{pageQueryQo.getTag()});
    }

    @Override
    public Set<Content> getRecentArticles() {
        String sql = "SELECT t.* FROM t_content t ORDER BY id LIMIT 4 ";
        List<Content> resultList = contentRepository.findAllByNativeSql(sql, Content.class, new Object[]{});
        Set<Content> resultSet = new HashSet<Content>(resultList);
        return resultSet;
    }
}
