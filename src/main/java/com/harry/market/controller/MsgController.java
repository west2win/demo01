package com.harry.market.controller;


import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.vo.UserInfoVO;
import com.harry.market.entity.Message;
import com.harry.market.entity.User;
import com.harry.market.handler.WebSocketPushHandler;
import com.harry.market.mapper.MessageMapper;
import com.harry.market.mapper.UserDetailsMapper;
import com.harry.market.service.MessageService;
import com.harry.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;

@RestController
@RequestMapping("/msg")
@CrossOrigin("*")
public class MsgController {

    @Autowired
    private UserDetailsMapper userDetailsMapper;
    @Resource
    private MessageMapper messageMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;


    /**
     * 功能描述:向全体广播消息
     * @param: [msg] 消息内容
     * @return: boolean
     * @auther: lyd
     * @date: 2019/8/14 16:10
     */
    @PostMapping("/sendMsg")
    public boolean sendMsg(String msg){
        System.out.println("全体广播消息 ["+msg+"]");
        TextMessage textMessage = new TextMessage(msg);
        try{
            WebSocketPushHandler.sendMessagesToUsers(textMessage);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 功能描述:向指定用户发送消息
     * @param msg 消息内容
     * @auther: lyd
     * @date: 2019/8/14 16:13
     */
    @PostMapping("/sendMsgByUser/{toId}/{fromId}")
    public Result sendMsgByUser(@RequestParam String msg, @PathVariable Long toId, @PathVariable Long fromId){
        UserInfoVO user = userDetailsMapper.getUserInfo(toId);
        Message message = new Message();
        message.setMsg(msg);
        message.setTo_id(toId);
        message.setFrom_id(fromId);
        messageMapper.insert(message);
        System.out.println("向 "+user.getNickName()+" 发送消息，消息内容为:"+msg);
//        TextMessage textMessage = new TextMessage(msg);
//        try{
//            WebSocketPushHandler.sendMessageToUser(toId,textMessage);
//        }catch (Exception e){
//            return Result.error(Constants.CODE_500,"系统错误");
//        }
        return Result.success(message);
    }

    @GetMapping("/getMsg/{fromId}/{toId}")
    public Result getMsg(@PathVariable Long fromId,@PathVariable Long toId) {
        User u1 = userService.getUserById(fromId);
        User u2 = userService.getUserById(toId);
        if (u1==null||u2==null) {
            return Result.error(Constants.CODE_400,"不存在该用户");
        }
        return Result.success(messageService.getMsg(fromId,toId),messageService.getMsgCount(fromId,toId).toString());


    }

}
