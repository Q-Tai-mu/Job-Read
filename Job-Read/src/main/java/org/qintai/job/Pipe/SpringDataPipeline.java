package org.qintai.job.Pipe;

import org.qintai.job.pojo.JobInfo;
import org.qintai.job.pojo.JobInfoField;
import org.qintai.job.service.JobInfoService;
import org.qintai.job.service.JobRepositoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuangjing
 * @create 2020/10/18-19:29
 * @Description:
 */
@Component
public class SpringDataPipeline implements Pipeline {

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private JobRepositoryService jobRepositoryService;


    /**
     * 保存数据到msql中
     *
     * @param resultItems
     * @param task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取需要保存到MySQL的数据
        JobInfo jobInfo = resultItems.get("jobInfo");
        //判断取得数据不为空
        if (jobInfo != null) {
            //如果有值则进行保存annot resolve symbol 'jobData' 执行在 更新el前面保证el能够正确执行
            this.jobInfoService.save(jobInfo);
            //更新el数据
            jobData(jobInfo);
        }

    }

    /**
     * 更新elasticSearch中的数据，如果存在则更新，不存在新增
     *
     * @param jobInfo
     */
    public void jobData(JobInfo jobInfo) {

        //从数据库中根据职业名称查询信息(此方法被执行执前得保证数据库中有相关数据)
        List<JobInfo> infos = this.jobInfoService.findJobInfo(jobInfo);
        List<JobInfoField> jr = new ArrayList<>();
        for (JobInfo info : infos) {
            //创建存放索引库数据的对象
            JobInfoField jobInfoField = new JobInfoField();
            //复制数据
            BeanUtils.copyProperties(info, jobInfoField);
            //将查询到的信息中的id赋予jobInfoField中
            jobInfoField.setId(info.getId());
            jr.add(jobInfoField);
        }
        //批量保存数据到索引库中
        this.jobRepositoryService.saveAll(jr);


    }
}
