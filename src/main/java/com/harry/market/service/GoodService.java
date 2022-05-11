package com.harry.market.service;

import cn.hutool.Hutool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.dto.ItemDTO;
import com.harry.market.controller.vo.ItemVO;
import com.harry.market.controller.vo.UserInfoVO;
import com.harry.market.entity.Item;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.utils.Crawler;
import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class GoodService extends ServiceImpl<GoodsMapper, Item> {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserService userService;


    public Item getGood(Long good_id) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("id",good_id);
        wrapper.eq("is_audit",2);
        return goodsMapper.selectOne(wrapper);
    }

    public void insertCrawlerItem(String kind) throws IOException {
        Crawler crawler = new Crawler();
        List<Item> items = crawler.getCrawlerItem(kind);
        for (Item item : items) {
            goodsMapper.insert(item);
        }
    }

    public Long uploadItem(ItemDTO itemDTO) {
        Item saveItem = new Item();
        saveItem.setId(IdWorker.getId(saveItem));
        saveItem.setKind(itemDTO.getKind());
        saveItem.setName(itemDTO.getName());
        saveItem.setIntroduction(itemDTO.getIntro());
        saveItem.setPrice(itemDTO.getPrice());
        saveItem.setNumber(itemDTO.getNumber());
        saveItem.setSeller_id(itemDTO.getSeller_id());
        goodsMapper.insert(saveItem);
        return saveItem.getId();
    }

    public void chgItemInfo(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(item.getId());
        item.setKind(itemDTO.getKind());
        item.setName(itemDTO.getName());
        item.setIntroduction(itemDTO.getIntro());
        item.setPrice(itemDTO.getPrice());
        item.setNumber(itemDTO.getNumber());
        goodsMapper.updateById(item);
    }

    public List<ItemVO> getAuditInfo(Short type,String itemType,Integer pageNum,Integer pageSize) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("gmt_modified");
        if (type==0) {
            wrapper.eq("is_audit",0);
        } else if (type==1) {
            wrapper.eq("is_audit",1);
        } else if (type==2) {
            wrapper.eq("is_audit",2);
        }
        if (itemType!=null) {
            wrapper.eq("kind",itemType);
        }
        wrapper.last("limit "+pageNum+","+pageSize);
        List<Item> items = goodsMapper.selectList(wrapper);
        ArrayList<ItemVO> itemVOS = new ArrayList<>();
        for (Item i:items) {
            ItemVO itemVO = new ItemVO();
            itemVO.setId(i.getId().toString());
            itemVO.setKind(i.getKind());
            itemVO.setName(i.getName());
            itemVO.setPhoto(i.getPhoto());
            UserInfoVO sellerInfo = userService.getUserInfoById(i.getSeller_id());
            itemVO.setSellerName(sellerInfo.getNickName());
            itemVO.setSellerHead(sellerInfo.getHead());
            itemVO.setIntroduction(i.getIntroduction());
            itemVO.setPrice(i.getPrice());
            if (type==0) {
                itemVO.setIs_audit("未审核");
            } else if (type==1) {
                itemVO.setIs_audit("不合格");
            } else if (type==2) {
                itemVO.setIs_audit("合格");
            }
            itemVO.setGmt_modified(i.getGmt_modified());

            itemVOS.add(itemVO);
        }
        return itemVOS;
    }

    public Long getAuditCount(Short type, String itemType,Integer pageNum,Integer pageSize) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("gmt_modified");
        if (type==0) {
            wrapper.eq("is_audit",0);
        } else if (type==1) {
            wrapper.eq("is_audit",1);
        } else if (type==2) {
            wrapper.eq("is_audit",2);
        }

        if (itemType!=null) {
            wrapper.eq("kind",itemType);
        }
        wrapper.last("limit "+pageNum+","+pageSize);
        return goodsMapper.selectCount(wrapper);
    }

    public ItemVO getItemById(Long itemId) {
        return goodsMapper.selectItembyId(itemId);
    }

}
