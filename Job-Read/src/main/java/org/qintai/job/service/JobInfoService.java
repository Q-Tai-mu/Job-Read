package org.qintai.job.service;

import org.qintai.job.pojo.JobInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author wuangjing
 * @create 2020/10/18-16:03
 * @Description:
 */
public interface JobInfoService {
    /**
     * 保存数据
     * @param jobInfo
     */
     void save(JobInfo jobInfo);

    /**
     * 根据条件查询数据
     * @param jobInfo
     * @return
     */
     List<JobInfo> findJobInfo(JobInfo jobInfo);

    /**
     * 分页从数据库中查询
     * @param count
     * @param rows
     * @return
     */
    Page<JobInfo> findAllPage(int count, int rows);

    JobInfo findBy(JobInfo jobInfo);
}
