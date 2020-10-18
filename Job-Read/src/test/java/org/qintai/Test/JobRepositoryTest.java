package org.qintai.Test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.qintai.ApplicationJob;
import org.qintai.job.pojo.JobInfo;
import org.qintai.job.pojo.JobInfoField;
import org.qintai.job.service.JobInfoService;
import org.qintai.job.service.JobRepositoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuangjing
 * @create 2020/10/18-20:37
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationJob.class)
public class JobRepositoryTest {

    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private JobRepositoryService jobRepositoryService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 创建索引
     */
    @Test
    public void createIndex(){
        this.elasticsearchTemplate.createIndex(JobInfoField.class);
        this.elasticsearchTemplate.putMapping(JobInfoField.class);
    }
    @Test
    public void jobData() {
        //声明当前页码数
        int count = 1;
        //声明查询数据条数
        int pageSize = 0;

        //循环查询
        do {
            //从MySQL数据库中分页查询数据
            Page<JobInfo> page = this.jobInfoService.findAllPage(count, 500);

            //声明存放索引库数据的容器
            List<JobInfoField> list = new ArrayList<>();

            //遍历查询结果
            for (JobInfo jobInfo : page.getContent()) {
                //创建存放索引库数据的对象
                JobInfoField jobInfoField = new JobInfoField();
                //复制数据
                BeanUtils.copyProperties(jobInfo, jobInfoField);
                //把复制好的数据放到容器中
                list.add(jobInfoField);
            }

            //批量保存数据到索引库中
            this.jobRepositoryService.saveAll(list);

            //页面数加一
            count++;
            //获取查询数据条数
            pageSize = page.getContent().size();

        } while (pageSize == 500);
    }

}
