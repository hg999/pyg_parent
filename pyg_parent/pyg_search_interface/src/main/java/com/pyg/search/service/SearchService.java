package com.pyg.search.service;

import java.util.List;
import java.util.Map;

/**
 * Created by crowndint on 2018/10/22.
 */
public interface SearchService {

    public Map<String, Object> search(Map<String, Object> searchMap);

    /**
     * 导入数据
     * @param list
     */
    public void importList(List list);

    /**
     * 删除数据
     * @param goodsIdList
     */
    public void deleteByGoodsIds(List goodsIdList);

}
