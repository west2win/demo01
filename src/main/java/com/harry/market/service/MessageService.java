package com.harry.market.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.harry.market.entity.Message;
import com.harry.market.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.channels.WritePendingException;
import java.util.List;

@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    public List<Message> getMsg(Long from, Long to) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("from_id",from);
        wrapper.eq("to_id",to);
        wrapper.orderByAsc("gmt_modified");

        return messageMapper.selectList(wrapper);
    }

    public Long getMsgCount(Long from, Long to) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("from_id",from);
        wrapper.eq("to_id",to);
        return messageMapper.selectCount(wrapper);
    }

}
