package xyz.isnull.blog.core.service;

import org.springframework.data.domain.Page;
import xyz.isnull.blog.core.entity.Content;
import xyz.isnull.blog.core.model.PageQueryQo;

import java.util.Set;

public interface PageService {

    public Page<Content> findContentByTag(PageQueryQo pageQueryQo);

    public Set<Content> getRecentArticles();
}
