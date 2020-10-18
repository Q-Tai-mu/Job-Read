package org.qintai.job.tasks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.qintai.job.Pipe.SpringDataPipeline;
import org.qintai.job.pojo.JobInfo;
import org.qintai.job.utlis.MathSalary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @author wuangjing
 * @create 2020/10/18-16:15
 * @Description: setScheduler 使用 QueueScheduler 内存队列保存待抓取URL
 * setDuplicateRemover 使用 BloomFilterDuplicateRemover 过滤器对URL的去重复
 * addPipeline 自定义下载保存
 */
@Component
public class JobProcessor implements PageProcessor {

    @Autowired
    private SpringDataPipeline springDataPipeline;

    /**
     * 定时执行 每次启动延迟1秒 每次执行完成后间隔100秒
     */
    @Scheduled(initialDelay = 4000, fixedDelay = 1000 * 100)
    private void process() {
        String url = "https://jobs.51job.com/hefei/javakaifa/";
        Spider.create(new JobProcessor())
                .addUrl(url)
                .addPipeline(this.springDataPipeline)
                .setScheduler(new QueueScheduler()
                        .setDuplicateRemover(new BloomFilterDuplicateRemover(10000000)))
                .thread(15)
                .run();
    }

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().css("div.detlist div.e").nodes();

        //如果nodes为空则为详情页面
        if (nodes.isEmpty()) {
            //如果为空，表示这是招聘信息详情页保存信息详情
            this.saveJobInfo(page);
        } else {
            //如果有值，表示这是招聘信息列表页
            for (Selectable node : nodes) {
                //获取招聘信息详情页url
                String jobUrl = node.links().toString();
                //添加到url任务列表中，等待下载
                page.addTargetRequest(jobUrl);

                //获取翻页按钮的超链接
                List<String> listUrl = page.getHtml().$("div.p_in li.bk").links().all();
                //添加到任务列表中
                page.addTargetRequests(listUrl);

            }
        }
    }

    private Site site = Site.me();

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 具体解析页面详情
     *
     * @param page
     */
    private void saveJobInfo(Page page) {
        //创建招聘信息
        JobInfo jobInfo = new JobInfo();
        //得到dom
        Html html = page.getHtml();
        //公司名称
        jobInfo.setCompanyName(html.css("div.tHeader p.cname a", "text").toString());
        //公司地址div.tBorderTop_box:nth-child(3) p.fp
        jobInfo.setCompanyAddr(html.css("div.tBorderTop_box:nth-child(2) p.fp", "text").toString());
        //公司信息
        jobInfo.setCompanyInfo(html.$("div.tmsg", "text").toString());
        //职位名称
        jobInfo.setJobName(html.css("div.tHeader > div.in > div.cn > h1", "text").toString());
        //工作地点
        jobInfo.setJobAddr(html.css("div.tHeader > div.in > div.cn > span.sp4", "text").toString());
        //职位信息
        jobInfo.setJobInfo(Jsoup.parse(html.$("div.tBorderTop_box:nth-child(1)").toString()).text());
        //工资范围
        String salaryStr = html.$("div.tHeader > div.in > div.cn > strong", "text").toString();
        jobInfo.setSalaryMin(MathSalary.getSalary(salaryStr)[0]);
        jobInfo.setSalaryMax(MathSalary.getSalary(salaryStr)[1]);
        //职位详情url
        jobInfo.setUrl(page.getUrl().toString());
        //职位发布时间
        String time = html.$("div.cn > p.ltype", "text").regex(".*发布").toString();
        jobInfo.setTime(time.substring(time.lastIndexOf("人") + 2, time.length() - 2));

        //保存数据
        page.putField("jobInfo", jobInfo);
    }
}
