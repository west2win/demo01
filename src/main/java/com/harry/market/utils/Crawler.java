package com.harry.market.utils;

import com.harry.market.entity.Item;
import com.harry.market.mapper.GoodsMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crawler {

    public List<Item> getCrawlerItem(String kind) throws IOException {
        List<Item> items = new ArrayList<Item>();

        for (int i = 1; i < 20; i+=2) {
            String url = "https://search.jd.com/Search?keyword="+kind+"&page="+i;
            Document document = Jsoup.parse(new URL(url), 30000);
            Element goodsList = document.getElementById("J_goodsList");
//        Elements elements = element.getElementsByTag("li");
            Elements elements = goodsList.getElementsByClass("gl-item");
            for (Element el : elements) {
                String id = el.attr("data-sku");
                String pname = el.getElementsByClass("p-name").get(0).getElementsByTag("em").get(0).text();
                String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
                String price = el.getElementsByClass("p-price").get(0).getElementsByTag("i").get(0).text();

                Item item = new Item();
                BigInteger nid = new BigInteger(id);
                item.setId(nid);
                item.setName(pname);
                item.setPhoto(img);
                BigDecimal nprice = new BigDecimal(price);
                item.setPrice(nprice);
                Random seed = new Random();
                BigInteger sid = BigInteger.valueOf(seed.nextInt(10));
                item.setNumber(seed.nextInt(999));
                item.setSeller_id(sid);
                item.setKind(kind);
                items.add(item);
            }
        }
        return items;
    }
}
