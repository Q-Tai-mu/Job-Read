package org.QinTai.Read.tasks;

import org.QinTai.Read.pojo.ItemTs;
import org.QinTai.Read.service.ItemTsService;
import org.QinTai.Read.utlis.HttpClientUtlis;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wuangjing
 * @create 2020/10/16-14:39
 * @Description:
 */
@Component
public class ItemTsTask {

    @Autowired
    private HttpClientUtlis httpClientUtlis;
    @Autowired
    private ItemTsService itemTsService;
    private Integer c = 1;


    @Scheduled(fixedDelay = 1000 * 60)
    public void process() throws Exception {
        String url = "https://xiaoshuo.sogou.com/29_0_0_0_heat/?pageNo=";
        for (int i =101; i <201; i++) {
            //发起请求访问页面
            System.out.println("第" + (c++) + "页");
            String html = httpClientUtlis.pareHtml(url + i);
            //解析页面 保存数据到数据库
            this.parseHtml(html);
        }
        //从新赋值页码

        System.out.println("执行完成");
    }

    private void parseHtml(String html) {

        Document document = Jsoup.parse(html);
        //获取小说列表url
        Elements elements = document.select("ul");
        for (Element element : elements) {
            //获取小说列表li
            Elements elementsByClass = element.getElementsByClass("filter-ret clear");
            ////遍历小说
            for (Element byClass : elementsByClass) {
                Elements li = byClass.select("li");
                for (Element element1 : li) {
                    //判断商品是否被抓取过，可以根据sku判断
                    ItemTs param = new ItemTs();
                    //通过唯一的阅读地址
                    Element asht = element1.select("div").select(".btns").select("a").first();
                    param.setContent("https://xiaoshuo.sogou.com" + asht.attr("href"));
                    List<ItemTs> list = this.itemTsService.findAll(param);
                    //判断是否查询到结果
                    if (list.size() > 0) {
                        //如果有结果，表示商品已下载，进行下一次遍历
                        continue;
                    }

                    //创建小说对象
                    ItemTs itemTs = new ItemTs();
                    //小说名称
                    itemTs.setTitle(element1.select("h3").text());

                    String textOutn = element1.select("div").select(".d1").text();

                    //得到具体作者x,y
                    int ia = element1.select("div").select(".d1").text().indexOf("：");
                    int ib = element1.select("div").select(".d1").text().indexOf("更");
                    //得到具体作者
                    itemTs.setAuthor(textOutn.substring(ia + 1, ib));


                    //得到具体章节x
                    int ca = element1.select("div").select(".d1").text().indexOf("节：");
                    //得到具体章节
                    itemTs.setUpdatese(textOutn.substring(ca));
                    //得到小说简介
                    itemTs.setBidn(element1.select("div").select(".d2").text());

                    //得到小说具体阅读地址
                    Element ash = element1.select("div").select(".btns").select("a").first();
                    itemTs.setContent("https://xiaoshuo.sogou.com" + ash.attr("href"));
                    //得到小说封面名称
                    //<img src="//yue07.sogoucdn.com/cdn/image/book/2405142055_1544305999255.jpg">
                    String imgUrl = element1.getElementsByClass("cover fl").select("img").attr("src");
                    String picName = httpClientUtlis.pareImage("https:" + imgUrl);
                    itemTs.setPir(picName);

                    //保存商品数据
                    this.itemTsService.save(itemTs);

                }

            }
        }
    }


}
