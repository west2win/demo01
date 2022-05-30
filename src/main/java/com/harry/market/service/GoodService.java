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
import com.harry.market.controller.vo.UserVO;
import com.harry.market.entity.Item;
import com.harry.market.entity.User;
import com.harry.market.entity.UserDetails;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.mapper.UserDetailsMapper;
import com.harry.market.utils.Crawler;
import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class GoodService extends ServiceImpl<GoodsMapper, Item> {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsMapper userDetailsMapper;

    /**
     * @author HarryGao
     * @param good_id
     * @return
     */
    public Item getGood(Long good_id) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("id",good_id);
        wrapper.eq("is_audit",2);
        return goodsMapper.selectOne(wrapper);
    }

    /**
     * @author 222100209_李炎东
     * @usage 插入爬虫爬取的数据到数据库
     * @param kind
     * @throws IOException
     */
    public void insertCrawlerItem(String kind) throws IOException {
        Crawler crawler = new Crawler();
        List<Item> items = crawler.getCrawlerItem(kind);
        for (Item item : items) {
            goodsMapper.insert(item);
        }
    }

    /**
     * @author 222100209
     * @usage 上传商品
     * @param itemDTO
     * @return
     */
    public Long uploadItem(ItemDTO itemDTO) {
        Item saveItem = new Item();
        saveItem.setId(IdWorker.getId(saveItem));
        saveItem.setPhoto(itemDTO.getPhoto());
        saveItem.setKind(itemDTO.getKind());
        saveItem.setName(itemDTO.getName());
        saveItem.setIntroduction(itemDTO.getIntro());
        saveItem.setPrice(itemDTO.getPrice());
        saveItem.setNumber(itemDTO.getNumber());
        saveItem.setSeller_id(itemDTO.getSeller_id());
        goodsMapper.insert(saveItem);
        return saveItem.getId();
    }

    /**
     * @author 222100209_李炎东
     * @usage 修改商品信息
     * @param itemDTO
     */
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

    /**
     * @author 222100209_李炎东
     * @usgae 获取审核相关信息
     * @param type 筛选参数
     * @param itemType 商品类型参数
     * @param pageNum 第?页
     * @param pageSize 一页有?条数据
     * @return
     */
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
        wrapper.last("limit "+pageNum*pageSize+","+pageSize);
        List<Item> items = goodsMapper.selectList(wrapper);
        ArrayList<ItemVO> itemVOS = new ArrayList<>();
        for (Item i:items) {
            ItemVO itemVO = new ItemVO();
            itemVO.setNumber(i.getNumber());
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            itemVO.setSdf(sdf.format(i.getGmt_modified()));
            itemVO.setGmt_modified(i.getGmt_modified());

            itemVOS.add(itemVO);
        }
        return itemVOS;
    }

    /**
     * @author 222100209_李炎东
     * @param type 筛选参数
     * @param itemType 商品类型
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
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
//        wrapper.last("limit "+pageNum+","+pageSize);
        return goodsMapper.selectCount(wrapper);
    }

    /**
     * @author 222100209_李炎东
     * @param itemId 商品Id
     * @return
     */
    public ItemVO getItemById(Long itemId) {
        return goodsMapper.selectItembyId(itemId);
    }

    /**
     * @author 222100209_李炎东
     * @param id 商品Id
     * @return ItemVO
     */
    public List<ItemVO> findGoods(Long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        ArrayList<ItemVO> res = new ArrayList<>();
        List<ItemVO> goods = goodsMapper.findGoods(id);
        for (ItemVO good : goods) {
            good.setSdf(sdf.format(good.getGmt_modified()));
            res.add(good);
        }

        return res;
    }

    /**
     * @author 222100209_李炎东
     * @usage 获取某用户买的商品信息
     * @param seller_id 用户Id
     * @param sort 筛选参数
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    public List<ItemVO> getInfoBySort(Long seller_id,Integer sort,Integer pageNum,Integer pageSize) {
        String sta;
        QueryWrapper<Item> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("seller_id",seller_id);
//        itemQueryWrapper.eq("is_audit",2);
        if (sort==1) {
            sta = "已售出";
            itemQueryWrapper.eq("number",0);
        } else if (sort==0) {
            sta = "未售出";
            itemQueryWrapper.ne("number",0);
        } else {
            sta = "全部";
        }
        itemQueryWrapper.last("limit "+pageNum*pageSize+","+pageSize);
        List<Item> items = goodsMapper.selectList(itemQueryWrapper);
        ArrayList<ItemVO> itemVOS = new ArrayList<>();
        for (Item item : items) {
            ItemVO i = new ItemVO();
            i.setId(item.getId().toString());
            i.setKind(item.getKind());
            i.setPhoto(item.getPhoto());
            i.setName(item.getName());
            i.setNumber(item.getNumber());
            UserInfoVO seller = userDetailsMapper.getUserInfo(item.getSeller_id());
            i.setSellerHead(seller.getHead());
            i.setSellerName(seller.getNickName());
            i.setIntroduction(item.getIntroduction());
            i.setPrice(item.getPrice());
            i.setGmt_modified(item.getGmt_modified());
            i.setStatus(sta);
            if (item.getIs_audit()==0) {
                i.setIs_audit("未审核");
            } else if (item.getIs_audit()==1) {
                i.setIs_audit("不合格");
            } else if (item.getIs_audit()==2) {
                i.setIs_audit("合格");
            } else {
                i.setIs_audit("我不到啊？出bug了？");
            }
            if (i.getNumber()==0) {
                i.setIsSelled("已售出");
            } else {
                i.setIsSelled("未售出");
            }

            itemVOS.add(i);
        }
        return itemVOS;
    }

    /**
     * @author 222100209_李炎东
     * @usage 获取某用户卖的商品总数
     * @param seller_id
     * @param sort
     * @return
     */
    public Long getCountBySort(Long seller_id,Integer sort) {
        QueryWrapper<Item> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("seller_id",seller_id);
        itemQueryWrapper.eq("is_audit",2);
        if (sort==1) {
            itemQueryWrapper.eq("number",0);
        } else if (sort==0) {
            itemQueryWrapper.ne("number",0);
        }
        return goodsMapper.selectCount(itemQueryWrapper);
    }

    /**
     * @author 222100209
     * @usage 模糊查询
     * @param nName 模糊查询
     * @param sort 筛选参数
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    public List<ItemVO> search(String nName,Integer sort,Integer pageNum,Integer pageSize) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.like("name",nName);
        wrapper.eq("is_audit",2);
        wrapper.gt("number",0);
        if (sort==0) {
//            wrapper.last("order by number desc");
            wrapper.orderByDesc("number");
        } else if (sort==1) {
//            wrapper.last("order by price ASC");
            wrapper.orderByAsc("price");
        } else if (sort==2) {
//            wrapper.last("order by price DESC");
            wrapper.orderByDesc("price");
        } else if (sort==3) {
//            wrapper.last("order by gmt_modified ASC");
            wrapper.orderByAsc("gmt_modified");
        }
//        System.out.println("=====================");
//        System.out.println(sort);
        wrapper.last("limit "+ pageNum*pageSize+","+pageSize);
        List<Item> items = goodsMapper.selectList(wrapper);
        ArrayList<ItemVO> res = new ArrayList<>();
        for (Item i : items) {
            ItemVO itemVO = new ItemVO();
            itemVO.setId(i.getId().toString());
            itemVO.setKind(i.getKind());
            itemVO.setName(i.getName());
            itemVO.setPhoto(i.getPhoto());
            itemVO.setIntroduction(i.getIntroduction());
            itemVO.setPrice(i.getPrice());
            itemVO.setNumber(i.getNumber());
            itemVO.setGmt_modified(i.getGmt_modified());

            UserDetails user = userDetailsMapper.selectById(i.getSeller_id());
            itemVO.setSellerHead(user.getHead());
            itemVO.setSellerName(user.getNickname());
            if (i.getNumber()==0) {
                itemVO.setIsSelled("已出售");
            } else {
                itemVO.setIsSelled("未售出");
            }
            itemVO.setIs_audit("合格");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            itemVO.setSdf(sdf.format(i.getGmt_modified()));

            res.add(itemVO);
        }


        return res;
    }

    /**
     * @author 222100209_李炎东
     * @usgae 模糊查询(审核页面用)
     * @param nName 查询参数
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    public List<ItemVO> searchForAudit(String nName,Integer pageNum,Integer pageSize) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.like("name",nName);
        wrapper.last("limit "+pageNum*pageSize+","+pageSize);
        List<Item> items = goodsMapper.selectList(wrapper);
        ArrayList<ItemVO> res = new ArrayList<>();
        for (Item i : items) {
            ItemVO itemVO = new ItemVO();
            itemVO.setId(i.getId().toString());
            itemVO.setKind(i.getKind());
            itemVO.setName(i.getName());
            itemVO.setPhoto(i.getPhoto());
            itemVO.setIntroduction(i.getIntroduction());
            itemVO.setPrice(i.getPrice());
            itemVO.setNumber(i.getNumber());
            itemVO.setGmt_modified(i.getGmt_modified());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            itemVO.setSdf(sdf.format(i.getGmt_modified()));
            if (i.getIs_audit()==0) {
                itemVO.setIs_audit("未审核");
            } else if (i.getIs_audit()==1) {
                itemVO.setIs_audit("不合格");
            } else if (i.getIs_audit()==2) {
                itemVO.setIs_audit("合格");
            }

            UserDetails user = userDetailsMapper.selectById(i.getSeller_id());
            itemVO.setSellerName(user.getUsername());
            itemVO.setSellerHead(user.getHead());

            res.add(itemVO);
        }
        return res;
    }

    /**
     * @author 222100209_李炎东
     * @usage 获取查询到的数量
     * @param nName 模糊查询参数
     * @param sort 筛选参数
     * @return
     */
    public Long getSearchCount(String nName,Integer sort) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.like("name",nName);
        wrapper.eq("is_audit",2);
        if (sort==0) {
            wrapper.orderByDesc("number");
        } else if (sort==1) {
            wrapper.orderByAsc("price");
        } else if (sort==2) {
            wrapper.orderByDesc("price");
        } else if (sort==3) {
            wrapper.orderByAsc("gmt_modified");
        }
        return goodsMapper.selectCount(wrapper);
    }

    /**
     * @author 222100209_李炎东\
     * @usgae 获取查询到的数量[审核页面用]
     * @param nName 查询参数
     * @return
     */
    public Long getSearchForAuditCount(String nName) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.like("name",nName);
        return goodsMapper.selectCount(wrapper);
    }

}
