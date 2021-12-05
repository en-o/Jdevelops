package cn.jdevelop.mybatis.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import cn.jdevelop.entity.basics.vo.SerializableVO;
import cn.jdevelop.mybatis.server.util.ObjectUtil;
import cn.jdevelop.mybatis.server.util.WrapperUtils;
import cn.jdevelop.result.page.ResourcePage;
import cn.jdevelop.result.response.PageVO;
import cn.jdevelop.result.response.SortVO;
import cn.jdevelop.result.result.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * mybatis 父级通用controller
 * @author lmz
 * @date 2020/12/8  15:25
 */
public class BaseController<M extends IService<T>, T extends SerializableVO<T>, S extends SerializableVO<S>,
        U extends SerializableVO<U>,
        R extends SerializableVO<R>> {
    @Autowired
    private M service;
    private static final int T_INDEX = 1;

    private static final int R_INDEX = 4;
    private Class<T> tClass;
    private Class<R> rClass;

    protected BaseController() {
        Type type = getClass().getGenericSuperclass();
        Type trueType1 = ((ParameterizedType) type).getActualTypeArguments()[T_INDEX];
        this.tClass = (Class<T>) trueType1;
        Type trueType2 = ((ParameterizedType) type).getActualTypeArguments()[R_INDEX];
        this.rClass = (Class<R>) trueType2;
    }

    /**
     * description: 单个保存或更新
     *
     * @return void
     * @author lmz
     * @company Peter
     * @date 2020/12/8  15:34
     */
    @PostMapping(value = "saveOrUpdate")
    @ApiOperation("单个保存或更新")
    public ResultVO<R> saveOrUpdate(@RequestBody U u) {
        T t = U.to(u, tClass);
        service.saveOrUpdate(t);
        return ResultVO.success(R.to(t, rClass), "修改成功");
    }

    /**
     * description: 批量保存或更新
     *
     * @param list list
     * @return void
     * @author lmz
     * @company Peter
     * @date 2020/12/8  15:36
     */
    @ApiOperation("批量保存或更新")
    @PostMapping(value = "/batchSaveOrUpdate")
    public ResultVO<String> batchSaveOrUpdate(@RequestBody List<U> list) {
        return ResultVO.resultMsg(service.saveOrUpdateBatch(U.to(list, tClass)), "批量操作");
    }

    /**
     * description: 根据id删除
     *
     * @param id id
     * @return void
     * @author lmz
     * @company Peter
     * @date 2020/12/8  15:39
     */
    @ApiOperation("根据id删除")
    @DeleteMapping("delete")
    public ResultVO<String> delete(@RequestParam("id") Long id) {
        return ResultVO.resultMsg(service.removeById(id), "删除");

    }

    /**
     * description: 根据id查询
     *
     * @param id id
     * @return T
     * @author lmz
     * @company Peter
     * @date 2020/12/8  15:39
     */
    @ApiOperation("根据id查询")
    @GetMapping(value = "/getById")
    public ResultVO<R> getById(@RequestParam("id") Long id) {
            service.getById(id);
            return ResultVO.resultDataMsgForT(true, R.to(service.getById(id), rClass), "查询");
    }

    /**
     * description: 根据uuid查询
     *
     * @param uuid uuid
     * @return com.databstech.apis.result.vo.ResultVO<T>
     * @author lmz
     * @company Peter
     * @date 2020/12/15  10:42
     */
    @ApiOperation("根据uuid查询")
    @GetMapping(value = "/uuid")
    public ResultVO<R> getById(@RequestParam("uuid") Object uuid) {
        T t = service.getOne(new QueryWrapper<T>().eq("uuid", uuid));
        return ResultVO.resultDataMsgForT(true, R.to(t, rClass), "查询");
    }

    /**
     * description: 查询全部
     *
     * @return java.util.List<T>
     * @author lmz
     * @company Peter
     * @date 2020/12/8  15:39
     */
    @ApiOperation("查询全部")
    @PostMapping(value = "/list")
    public ResultVO<List<R>> getList(@RequestBody S s) {
        T t = S.to(s, tClass);
        QueryWrapper<T> wrapper = WrapperUtils.createWrapper(t);
        //判断排序
        SortVO sortVO = (SortVO) ObjectUtil.getFieldValue(s, "sortVO");
        //添加排序
        if (sortVO != null) {
            wrapper = WrapperUtils.createOrderBy(wrapper, sortVO);
        }
        List<T> list = service.list(wrapper);
        List<R> list1 = R.to(list, rClass);
        return ResultVO.resultDataMsgForT(true, list1, "查询全部");
    }


    @ApiOperation("条件分页查询")
    @PostMapping(value = "/queryPage")
    public ResultVO<ResourcePage<List<R>>> queryPage(@RequestBody S s) {
        PageVO pageVO = (PageVO) ObjectUtil.getFieldValue(s, "pageVO");
        SortVO sortVO = (SortVO) ObjectUtil.getFieldValue(s, "sortVO");
        assert pageVO != null;
        Page<T> pageParam = new Page<>(pageVO.getPageIndex(), pageVO.getPageSize());

        T t = S.to(s, tClass);
        //创建查询条件
        QueryWrapper<T> wrapper = WrapperUtils.createWrapper(t);
        //添加排序
        if (sortVO != null) {
            wrapper = WrapperUtils.createOrderBy(wrapper, sortVO);
        }
        Page<T> page = service.page(pageParam, wrapper);
        long total = page.getTotal();
        List<T> records = page.getRecords();
        List<R> list = R.to(records, rClass);
        ResourcePage<List<R>> resultPage = ResourcePage.page(total, list);
        return ResultVO.success(resultPage, "查询成功");
    }
}
