package org.qintai.job.mapper;

import org.qintai.job.pojo.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author wuangjing
 * @create 2020/10/18-16:02
 * @Description:
 */
public interface JobInfoMapper extends JpaRepository<JobInfo,Long>, JpaSpecificationExecutor<JobInfo> {

    //@Query 使用jpql的方式查询。?1代表参数的占位符，其中1对应方法中的参数索引
    @Query(value="from JobInfo where jobName = ?1")
    public JobInfo findJobInfo(String name);
}
