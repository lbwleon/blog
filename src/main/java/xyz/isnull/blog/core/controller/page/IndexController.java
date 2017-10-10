package xyz.isnull.blog.core.controller.page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.isnull.blog.core.entity.Content;
import xyz.isnull.blog.core.entity.Metas;
import xyz.isnull.blog.core.obj.AjaxResult;
import xyz.isnull.blog.core.obj.ContentType;
import xyz.isnull.blog.core.obj.MetaType;
import xyz.isnull.blog.core.model.PageQueryQo;
import xyz.isnull.blog.core.repository.ContentRepository;
import xyz.isnull.blog.core.repository.MetasRepository;
import xyz.isnull.blog.core.service.PageService;
import xyz.isnull.blog.core.utils.SystemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/pages")
public class IndexController {

    private final static Integer PAGE_SIZE = 12;

    private static final String[] ICONS = {"bg-ico-book", "bg-ico-game", "bg-ico-note", "bg-ico-chat", "bg-ico-code", "bg-ico-image", "bg-ico-web", "bg-ico-link", "bg-ico-design", "bg-ico-lock"};

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private MetasRepository metasRepository;

    @Autowired
    private PageService pageService;

    @RequestMapping("/index")
    public String index(ModelMap map, Integer page){

        if(page == null){
            page = 0;
        }else {
            page = page - 1;
        }
        Pageable pageable = new PageRequest(page, PAGE_SIZE, new Sort(Sort.Direction.DESC,"id"));
        Page<Content> pages = contentRepository.findAll(pageable);
        List<Content> contents = pages.getContent();

        List<Metas> categorys = metasRepository.findByType(MetaType.CATEGORY.getName());
        //处理thumb_img 和 category显示
        for (Content content : contents){
            deal_category(content);
            deal_thumb(content);
        }

        map.put("totals", pages.getTotalPages());
        map.put("page", page + 1);
        map.put("articles",contents);
        map.put("icons", ICONS);

        return "pages/index";
    }

    @RequestMapping("/article/{id}")
    public String article(@PathVariable Long id, ModelMap map){
        Content content = contentRepository.findOne(id);
        if(content == null){
            return "403";
        }

        if(ContentType.MD.getName().equals(content.getFmt_type())){
            content.setContents(SystemUtils.markdownToHtml(content.getContents()));
        }

        deal_category(content);

        List tags = null;
        if(StringUtils.isNotEmpty(content.getTags())){
            tags = Arrays.asList(content.getTags().split(","));
        }else{
            tags = new ArrayList();
        }

        //最新文章

        map.put("c",content);
        map.put("tags", tags);

        return "pages/detail";
    }

    private void deal_category(Content content){
        content.setCategories(SystemUtils.metasToString(metasRepository.findByType(MetaType.CATEGORY.getName()), content.getCategories()));
    }

    private void deal_thumb(Content content){
        int cid = Integer.valueOf(String.valueOf(content.getId()));
        int size = cid % 20;
        size = size == 0 ? 1 : size;
        content.setThumb_img("/ui/pages/img/rand/" + size + ".jpg");
    }

    @RequestMapping("/tag/{tag}")
    public String tag(@PathVariable String tag, Integer page, ModelMap map){
        if(page == null){
            page = 0;
        }else{
            page = page - 1;
        }

        Metas metas = metasRepository.findByNameAndType(tag, MetaType.TAGS.getName());
        if(metas == null){
            return "403";
        }

        PageQueryQo queryQo = new PageQueryQo();
        queryQo.setPage(page);
        queryQo.setSize(PAGE_SIZE);
        queryQo.setTag(String.valueOf(metas.getId()));

        Page<Content> contents = pageService.findContentByTag(queryQo);

        map.put("keywords",tag);
        map.put("totals",contents.getTotalPages());
        map.put("page",page+1);
        map.put("type","标签");
        map.put("pageflag","tag");
        map.put("icons",ICONS);
        map.put("clists",contents.getContent());

        return "pages/page-category";
    }

    @RequestMapping(value = "re_articles", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getRecentArticles(ModelMap map){
        return new AjaxResult(pageService.getRecentArticles());
    }
}