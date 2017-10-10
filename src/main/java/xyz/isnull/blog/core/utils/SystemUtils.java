package xyz.isnull.blog.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import xyz.isnull.blog.core.entity.Metas;

import java.util.*;


public class SystemUtils {

    private static final Logger logger = Logger.getLogger(SystemUtils.class);

    public static String markdownToHtml(String markdown){
        if(StringUtils.isEmpty(markdown)) return "";
        markdown = markdown.replace("<!--more-->", "\r\n");


        List<Extension> extensions = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
        String content = renderer.render(document);


        // 支持网易云音乐输出
        if ( content.contains("[mp3:")) {
            content = content.replaceAll("\\[mp3:(\\d+)\\]", "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=350 height=106 src=\"//music.163.com/outchain/player?type=2&id=$1&auto=0&height=88\"></iframe>");
        }
        // 支持gist代码输出
        if ( content.contains("https://gist.github.com/")) {
            content = content.replaceAll("&lt;script src=\"https://gist.github.com/(\\w+)/(\\w+)\\.js\">&lt;/script>", "<script src=\"https://gist.github.com/$1/$2\\.js\"></script>");
        }
        return content;
    }

    public static String metasToString(List<Metas> metas, String metaStr){
        if(StringUtils.isEmpty(metaStr)){
            return "";
        }

        Map<String, String > map = new HashMap<String, String>();
        for (Metas m : metas){
            map.put(String.valueOf(m.getId()), m.getName());
        }
        String[] categoriesArr = metaStr.split(",");
        for (int i = 0; i < categoriesArr.length; i++) {
            categoriesArr[i] = map.get(categoriesArr[i]);
        }
        return StringUtils.join(categoriesArr,",");
    }

}
