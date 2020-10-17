package org.QinTai.Read.service;

import org.QinTai.Read.pojo.ItemTs;

import java.util.List;

/**
 * @author wuangjing
 * @create 2020/10/16-14:34
 * @Description:
 */
public interface ItemTsService {
    //根据条件查询数据
    public List<ItemTs> findAll(ItemTs item);

    //保存数据
    void save(ItemTs itemTs);
}
