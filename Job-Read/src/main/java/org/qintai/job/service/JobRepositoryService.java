package org.qintai.job.service;

import org.qintai.job.pojo.JobInfoField;
import org.qintai.job.pojo.JobResult;

import java.util.List;

/**
 * @author wuangjing
 * @create 2020/10/18-20:32
 * @Description:
 */
public interface JobRepositoryService {

    /**
     * 保存一条数据
     * @param jobInfoField
     */
    void save(JobInfoField jobInfoField);

    /**
     * 批量保存数据
     * @param lists
     */
    void saveAll(List<JobInfoField> lists);

    /**
     *
     * @param salary
     * @param jobaddr
     * @param keyword
     * @param page
     * @return
     */
    JobResult search(String salary, String jobaddr, String keyword, Integer page);

}
