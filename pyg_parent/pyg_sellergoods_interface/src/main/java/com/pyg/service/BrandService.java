package com.pyg.service;

import com.pyg.pojo.TbBrand;
import entity.PageResult;

import java.util.List;

public interface BrandService {
    /**
     * 列出所有数据
     * @return
     */
    public List<TbBrand> findAll();

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult findPage(int pageNum,int pageSize);

    /**
     * 添加
     * @param brand
     */
    public void add(TbBrand brand);

    /**
     * 修改
     * @param brand
     */
    public void update(TbBrand brand);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);

    /**
     * 批量删除
     * @param ids
     */
    public void delete(Long [] ids);

    /**
     * 模糊查询
     * @param brand
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult findPage(TbBrand brand, int pageNum,int pageSize);
}
