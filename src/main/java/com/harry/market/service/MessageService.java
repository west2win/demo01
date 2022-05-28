package com.harry.market.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.harry.market.entity.Message;
import com.harry.market.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.channels.WritePendingException;
import java.util.List;

/**
 * @author 222100209_李炎东
 */

@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    /**
     * @author 222100209_李炎东
     * @usage 获取to从from收到的信息
     * @param from 收件人
     * @param to 发件人
     * @return
     */
    public List<Message> getMsg(Long from, Long to) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("from_id",from);
        wrapper.eq("to_id",to);
        wrapper.orderByAsc("gmt_modified");

        return messageMapper.selectList(wrapper);
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

}
