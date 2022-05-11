package com.harry.market;

import com.google.common.primitives.UnsignedInteger;
import com.harry.market.entity.Item;
import com.harry.market.entity.UserDetails;
import com.harry.market.service.GoodService;

import com.harry.market.service.MailService;
import com.harry.market.service.UserService;
import com.harry.market.utils.ExcelUtill;
import com.harry.market.utils.VerCodeGenerateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.Random;

@SpringBootTest
class MarketApplicationTests {

    @Autowired
    GoodService goodService;

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        int i = (int) (Math.random() * (400000000 - 100000000)) + 100000000;
        System.out.println(i);
    }

    @Test
    void testCrawler() throws IOException {
        String url = "https://search.jd.com/Search?keyword=日用家居";
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
            Long nid = new Long(id);
            item.setId(nid);
            item.setName(pname);
            item.setPhoto(img);
            BigDecimal nprice = new BigDecimal(price);
            item.setPrice(nprice);
            Random seed = new Random();
            Long sid = Long.valueOf(seed.nextInt(10));
            item.setSeller_id(sid);
            item.setKind("日用家居");
            System.out.println(item);
        }
    }

    @Test
    void crawlerInsert() throws IOException {
//        goodService.insertCrawlerItem("服饰鞋包");
//        goodService.insertCrawlerItem("美妆护肤");
//        goodService.insertCrawlerItem("二手书籍");
//        goodService.insertCrawlerItem("日用家居");
        goodService.insertCrawlerItem("零食饮品");
    }

    @Test
    void excelInsert() {
        userService.insertExcelUser(1,15);
    }

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private MailService mailService;

    @Test
    void mailTest() throws MessagingException {
        String code = VerCodeGenerateUtil.getSecurityCode();
        System.out.println(code);
        //调用service方法，通过邮箱服务进行发送
        boolean isSend = mailService.sendMail("3146476973@qq.com", code);
    }

}
