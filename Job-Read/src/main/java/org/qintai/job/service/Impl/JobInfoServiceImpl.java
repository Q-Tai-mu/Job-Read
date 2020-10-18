package org.qintai.job.service.Impl;


import org.qintai.job.mapper.JobInfoMapper;
import org.qintai.job.pojo.JobInfo;
import org.qintai.job.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wuangjing
 * @create 2020/10/18-16:06
 * @Description:
 */
@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Override
    public void save(JobInfo jobInfo) {
        //先从数据库查询数据,根据发布日期查询和url查询
        JobInfo info = new JobInfo();
        info.setUrl(jobInfo.getUrl());
        info.setTime(jobInfo.getTime());
        List<JobInfo> jobInfos = this.findJobInfo(info);
        if (jobInfos.size() == 0) {
            //没有查询到数据则新增或者修改数据
            this.jobInfoMapper.saveAndFlush(jobInfo);
        }
    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        Example example = Example.of(jobInfo);
        List<JobInfo> list = this.jobInfoMapper.findAll(example);
        return list;
    }

    @Override
    public Page<JobInfo> findAllPage(int count, int rows) {
        Page<JobInfo> infos = this.jobInfoMapper.findAll(PageRequest.of(count - 1, rows));
        return infos;
    }

    /**
     * 查询单个
     *
     * @param jobInfo
     * @return
     */
    @Override
    public JobInfo findBy(JobInfo jobInfo) {
        JobInfo info = this.jobInfoMapper.findJobInfo(jobInfo.getJobName());
        return info;
    }
}
