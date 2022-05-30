package com.harry.market.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 222100209_李炎东
 * @apiNote 聊天室
 */

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
     * @auther: 222100209_李炎东
     * @date: 2019/8/14 16:10
     */
//    @PostMapping("/sendMsg")
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
     * @author 222100209_李炎东
     * @usage 发送系统消息
     * @param msg 发送的消息
     * @param toId 发送至
     * @return
     */
    @PostMapping("/sendSysMsg/{toId}")
    @ApiOperation("发送系统消息")
    public Result sendSysMsg(@RequestParam String msg,@PathVariable Long toId) {
        User u = userService.getUserById(toId);
        if (u==null) {
            return Result.error(Constants.CODE_400,"不存在该用户");
        } else {
            messageService.sendSys(msg,toId);
            return Result.success(msg);
        }
    }

    /**
     * @author 222100209_李炎东
     * @usage 获取系统消息
     * @param id 用户Id
     * @return
     */
    @GetMapping("/getSysMsg/{id}")
    @ApiOperation("获取系统消息")
    public Result getSysMeg(@PathVariable Long id) {
        User u = userService.getUserById(id);
        if (u==null) {
            return Result.error(Constants.CODE_400,"不存在该用户");
        } else {
            List<Message> msgs = messageService.getSys(id);
            Integer count = msgs.size();
            return Result.success(msgs,count.toString());
        }
    }

    /**
     * @auther 222100209_李炎东
     * @usage 向指定用户发送消息
     * @param msg 消息内容
     */
    @PostMapping("/sendMsgByUser/{toId}/{fromId}")
    @ApiOperation("fromId给toId发送消息")
    public Result sendMsgByUser(@RequestParam String msg, @PathVariable Long toId, @PathVariable Long fromId){
        User u1 = userService.getUserById(fromId);
        User u2 = userService.getUserById(toId);
        if (u1==null||u2==null) {
            return Result.error(Constants.CODE_400,"不存在该用户");
        } else {
            messageService.sendMsg(fromId,toId,msg);
            return Result.success(msg);
        }
    }

    /**
     * @author 222100209_李炎东
     * @param fromId 发送者Id
     * @param toId 接收者Id
     * @return
     */
    @GetMapping("/getMsg/{fromId}/{toId}")
    @ApiOperation("获取fromId给toId发送的消息")
    public Result getMsg(@PathVariable Long fromId,@PathVariable Long toId) {
        User u1 = userService.getUserById(fromId);
        User u2 = userService.getUserById(toId);
        if (u1==null||u2==null) {
            return Result.error(Constants.CODE_400,"不存在该用户");
        }
        return Result.success(messageService.getMsg(fromId,toId),messageService.getMsgCount(fromId,toId).toString());
    }
}
