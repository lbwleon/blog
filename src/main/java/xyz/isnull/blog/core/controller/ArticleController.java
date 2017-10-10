package xyz.isnull.blog.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import xyz.isnull.blog.core.entity.Content;
import xyz.isnull.blog.core.entity.Metas;
import xyz.isnull.blog.core.obj.MetaType;
import xyz.isnull.blog.core.repository.ContentRepository;
import xyz.isnull.blog.core.repository.MetasRepository;
import xyz.isnull.blog.core.service.ArticleService;
import xyz.isnull.blog.core.utils.SystemUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequestMapping("/article")
@Controller
public class ArticleController {

    private static final Logger logger = Logger.getLogger(ArticleController.class);

    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private MetasRepository metasRepository;

    @Autowired
    private ArticleService articleService;

    @RequestMapping({"/index","/"})
    public String index(ModelMap map, HttpServletRequest request){

        Integer page = request.getParameter("page") == null ? 0 : Integer.valueOf(request.getParameter("page")) - 1;

        Pageable pageable = new PageRequest(page, PAGE_SIZE, new Sort(Sort.Direction.DESC, "id"));

        Page<Content> linkPages = contentRepository.findAll(pageable);
        List<Content> contents = linkPages.getContent();

        for(Content content : contents){
            String categoryString =  SystemUtils.metasToString(getCategoryMetas(), content.getCategories());
            content.setCategories(categoryString);
        }

        map.put("totals", linkPages.getTotalPages());
        map.put("page", page + 1);
        map.put("articles",contents);
        map.put("menu_code","mainarticle");
        return "article/index";
    }

    private List<Metas> getCategoryMetas(){
       return metasRepository.findByType("category");
    }



    @RequestMapping("/add")
    public String add(ModelMap map){
        map.put("categorys",metasRepository.findByType("category"));
        map.put("menu_code","mainarticle");
        return "article/add";
    }

    @PostMapping("/save")
    @ResponseBody
    public String save(Content content){
        articleService.save(content);
        return "1";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, ModelMap map){
        Content content = contentRepository.findOne(id);
        map.put("c", content);

        List<Metas> categorys = metasRepository.findByType(MetaType.CATEGORY.getName());
        map.put("categorys", categorys);

        String c_category = content.getCategories();
        if(StringUtils.isNotEmpty(c_category)){
            String[] c_categiryArr = c_category.split(",");
            List<String> c_categoryList = Arrays.asList(c_categiryArr);
            map.put("categories", c_categoryList);
        }else{
            map.put("categories", new ArrayList<String>());
        }

        return "article/edit";
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id){
        articleService.delete(id);
        return "1";
    }

}
