package org.qintai.job.mapper;

import org.qintai.job.pojo.JobInfoField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author wuangjing
 * @create 2020/10/18-20:31
 * @Description:
 */
@Component
public interface JobRepository extends ElasticsearchRepository<JobInfoField,Long> {
//findBySalaryMinBetweenAndSalaryMaxBetweenAndJobAddrAndJobNameAndJobInfo
    Page<JobInfoField> findBySalaryMinBetweenAndSalaryMaxBetweenOrJobAddrOrJobNameAndJobInfo(Integer salaryMin1, Integer salaryMin2, Integer salaryMax1, Integer salaryMax2, String jobAddr, String jobName, String jobInfo, Pageable pageable);}
