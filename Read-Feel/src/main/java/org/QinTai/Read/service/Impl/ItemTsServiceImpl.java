package org.QinTai.Read.service.Impl;

import org.QinTai.Read.mapper.ItemTsMapper;
import org.QinTai.Read.pojo.ItemTs;
import org.QinTai.Read.service.ItemTsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wuangjing
 * @create 2020/10/16-14:36
 * @Description:
 */
@Service
public class ItemTsServiceImpl implements ItemTsService {
    @Autowired
    private ItemTsMapper itemTsMapper;

    @Override
    public List<ItemTs> findAll(ItemTs item) {
        Example example = Example.of(item);
        List list = this.itemTsMapper.findAll(example);
        return list;
    }

    @Override
    public void save(ItemTs itemTs) {
        this.itemTsMapper.save(itemTs);
    }
}
