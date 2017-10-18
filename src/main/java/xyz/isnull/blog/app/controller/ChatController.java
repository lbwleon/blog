package xyz.isnull.blog.app.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.isnull.blog.app.model.ChatMessage;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Log4j
public class ChatController {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

    @Autowired
    private SimpMessagingTemplate template;


    @MessageMapping("/all")
    @SendTo(value = "/topic/notice")
    public String all(String message){
        ChatMessage chatMessage = JSON.parseObject(message, ChatMessage.class);
        chatMessage.setSendTime(simpleDateFormat.format(new Date()));
        log.info(JSON.toJSONString(chatMessage));
        return JSON.toJSONString(chatMessage);
    }

    @RequestMapping("/ichat")
    public String tochat(HttpServletRequest request, ModelMap map){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem os = userAgent.getOperatingSystem();
        map.put("os",os.toString());
        return "chat/chat";
    }

}
