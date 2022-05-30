package com.harry.market.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.harry.market.controller.vo.MsgVO;
import com.harry.market.entity.Message;
import com.harry.market.entity.UserDetails;
import com.harry.market.mapper.MessageMapper;
import com.harry.market.mapper.UserDetailsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.channels.WritePendingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 222100209_李炎东
 */

@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserDetailsMapper userDetailsMapper;

    /**
     * @author 222100209_李炎东
     * @usage 获取to从from收到的信息
     * @param from 收件人
     * @param to 发件人
     * @return
     */
    public List<MsgVO> getMsg(Long from, Long to) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("from_id",from);
        wrapper.eq("to_id",to);
        wrapper.orderByAsc("gmt_modified");
        UserDetails u1 = userDetailsMapper.selectById(from);
        UserDetails u2 = userDetailsMapper.selectById(to);
        String fromName = u1.getNickname();
        String fromHead = u1.getHead();
        String toName = u2.getNickname();
        String toHead = u2.getHead();

        List<Message> messages = messageMapper.selectList(wrapper);
        ArrayList<MsgVO> res = new ArrayList<>();
        for (Message message : messages) {
            MsgVO msg = new MsgVO();
            msg.setFromName(fromName);
            msg.setFromHead(fromHead);
            msg.setToName(toName);
            msg.setToHead(toHead);
//            System.out.println("===================");
//            System.out.println(toHead);
            msg.setMsg(message.getMsg());
            res.add(msg);
        }
        return res;
    }

    /**
     * @author 222100209_李炎东
     * @usage 获得信息总数
     * @param from 收件人
     * @param to 发件人
     * @return
     */
    public Long getMsgCount(Long from, Long to) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("from_id",from);
        wrapper.eq("to_id",to);
        return messageMapper.selectCount(wrapper);
    }

    /**
     * @author 222100209_李炎东
     * @usage 发送系统消息
     * @param msg 要发送的系统消息
     * @param toId 发送至
     */
    public void sendSys(String msg,Long toId) {
        Message message = new Message();
        message.setMsg(msg);
        message.setTo_id(toId);
        message.setFrom_id(0L);
        messageMapper.insert(message);
    }

    /**
     * @author 222100209
     * @usage 接收系统消息
     * @param id 用户Id
     * @return 消息内容
     */
    public List<Message> getSys(Long id) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("from_id",0L);
        return messageMapper.selectList(wrapper);
    }

    /**
     * @author 222100209_李炎东
     * @param from 发送者
     * @param to 接收者
     * @param msg 信息
     */
    public void sendMsg(Long from,Long to,String msg) {
        Message message = new Message();
        message.setMsg(msg);
        message.setTo_id(to);
        message.setFrom_id(from);
        messageMapper.insert(message);
    }

}
