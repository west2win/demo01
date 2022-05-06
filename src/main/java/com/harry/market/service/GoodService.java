package com.harry.market.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.controller.dto.ItemDTO;
import com.harry.market.entity.Item;
import com.harry.market.entity.Order;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.mapper.OrderMapper;
import com.harry.market.utils.Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


@Service
public class GoodService extends ServiceImpl<GoodsMapper, Item> {

    @Autowired
    private GoodsMapper goodsMapper;


    public Item getGood(Long good_id) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("id",good_id);
        wrapper.eq("is_audit",0);
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
        goodsMapper.insert(saveItem);
        return saveItem.getId();
    }

    public void chgItemInfo(Long itemId,ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemId);
        item.setKind(itemDTO.getKind());
        item.setName(itemDTO.getName());
        item.setIntroduction(itemDTO.getIntro());
        item.setPrice(itemDTO.getPrice());
        item.setNumber(itemDTO.getNumber());
        goodsMapper.updateById(item);
    }


}
