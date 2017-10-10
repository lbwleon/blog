package xyz.isnull.blog.core.service;

import xyz.isnull.blog.core.entity.Content;

public interface ArticleService {

    public void save(Content content);

    public void delete(Long id);
}
