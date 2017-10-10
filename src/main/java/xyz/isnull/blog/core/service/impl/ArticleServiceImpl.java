package xyz.isnull.blog.core.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.isnull.blog.core.entity.Content;
import xyz.isnull.blog.core.entity.Metas;
import xyz.isnull.blog.core.entity.Relationships;
import xyz.isnull.blog.core.obj.MetaType;
import xyz.isnull.blog.core.repository.ContentRepository;
import xyz.isnull.blog.core.repository.MetasRepository;
import xyz.isnull.blog.core.repository.RelationshipsRepository;
import xyz.isnull.blog.core.service.ArticleService;

import java.util.Date;

@Service
public class ArticleServiceImpl implements ArticleService {


    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private MetasRepository metasRepository;

    @Autowired
    private RelationshipsRepository relationshipsRepository;

    @Override
    @Transactional
    public void save(Content content) {

        Date date = new Date();
        Long cid;
        if(content.getId() == null){
            content.setCreatedate(date);
            content.setModifydate(date);
            content.setHits(0);
            content.setStatus("已发布");
            contentRepository.save(content);
            cid = content.getId();
        } else {
            Content content_old = contentRepository.findOne(content.getId());
            content_old.setModifydate(date);
            content_old.setTitle(content.getTitle());
            content_old.setTags(content.getTags());
            content_old.setCategories(content.getCategories());
            content_old.setThumb_img(content.getThumb_img());
            content_old.setFmt_type(content.getFmt_type());
            content_old.setContents(content.getContents());
            contentRepository.save(content_old);
            cid = content_old.getId();
        }

        //保存新增的标签
        relationshipsRepository.deleteByCidAndType(cid, MetaType.TAGS.getName());
        String tags = content.getTags();
        if(StringUtils.isNotEmpty(tags)){
            String[] tagArr = tags.split(",");
            for(String tag : tagArr){
                if(StringUtils.isNotEmpty(tag)){
                    Metas metas = metasRepository.findByNameAndType(tag, MetaType.TAGS.getName());
                    if(metas == null){
                        metas = new Metas();
                        metas.setName(tag);
                        metas.setType(MetaType.TAGS.getName());
                        metasRepository.save(metas);
                    }
                    //保存关联
                    relationshipsRepository.save(new Relationships(cid,metas.getId(),MetaType.TAGS.getName()));
                }
            }
        }

        //保存关联的的分类
        relationshipsRepository.deleteByCidAndType(cid, MetaType.CATEGORY.getName());
        String categorys = content.getCategories();
        if(StringUtils.isNotEmpty(categorys)){
            String[] categoryArr = categorys.split(",");
            for(String category : categoryArr){
                Metas metas = metasRepository.findOne(Long.valueOf(category));
                relationshipsRepository.save(new Relationships(cid, metas.getId(), MetaType.CATEGORY.getName()));
            }
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        //删除关联的Metas
        relationshipsRepository.deleteByCid(id);
        contentRepository.delete(id);
    }
}
