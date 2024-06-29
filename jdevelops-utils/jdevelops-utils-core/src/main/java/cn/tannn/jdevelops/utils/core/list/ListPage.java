package cn.tannn.jdevelops.utils.core.list;

import java.util.ArrayList;
import java.util.List;

/**
 *  分页相关
 * @author tn
 * @version 1
 * @date 2020/8/12 19:11
 */
public class ListPage<T>  {

    /**
     * 要分页的数组
     */
    private List<T> list;
    /**
     * 总个数
     */
    private Integer total;
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 总页数
     */
    private Integer totalPages;
    /**
     * 每页个数大小
     */
    private Integer pageSize;
    /**
     * 前一页
     */
    private Integer previousPage;
    /**
     * 后一页
     */
    private Integer nextPage;

    /**
     * list分页 - 构造方法
     * @param list list数据
     * @param currentPage 当前页 (开始： 1还是0 我忘了，应该是1吧）
     * @param pageSize 每页数量
     */
    public ListPage(List<T> list, Integer currentPage, Integer pageSize) {
        this.list = list;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        setTotal();
        setTotalPages();
        setNextPage();
        setPreviousPage();
        getResult();
    }

    public ListPage() {
    }

    /**
     * list分页 - 静态方法
     *    Page page =ListUtil.page(stringArrayList, 3, 4);
     * @param list  需要分页的集合
     * @param currentPage 当前页
     * @param pageSize 每页数量
     * @param <T> 泛型
     * @return {Page} 包含分页的所有信息
     */
    public static<T> ListPage<T> page(List<T> list, Integer currentPage, Integer pageSize){
        return new ListPage<>(list,  currentPage,  pageSize);
    }

    /**
     * 获取结果
     */
    private void getResult() {
        // 每页小于总数目
        if (pageSize < total && pageSize > 0) {
            int start ;
            int end ;
            if (currentPage <= totalPages - 1 && currentPage >= 0) {
                start = (currentPage-1)* pageSize;
                end = currentPage* pageSize-1;
                setData(start, end);
            }else if(currentPage.equals(totalPages)){
                start = (currentPage-1)* pageSize;
                end = total - 1;
                setData(start, end);
            }else{
                setData(-1,-1);
            }
        }
    }


    /**
     * 获取数组指定位置的元素
     * @param start 起始位置
     * @param end   截止位置
     */
    private void setData(int start, int end) {
        if(start==-1&&end==-1) {
            setList(new ArrayList<>());
        }else if (start >= 0 && end < total) {
            List<T> list1 = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                list1.add(list.get(i));
            }
            this.list.clear();
            setList(list1);
        }
    }

    public Integer getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage() {
        this.previousPage = currentPage > 0 ? currentPage - 1 : currentPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage() {

        this.nextPage = currentPage < totalPages ? currentPage + 1 : currentPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal() {
        this.total = list.size();
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages() {
        this.totalPages = (int) Math.ceil(1.0 * total / pageSize);
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "Page{" +
                "list=" + list +
                ", total=" + total +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", pageSize=" + pageSize +
                ", previousPage=" + previousPage +
                ", nextPage=" + nextPage +
                '}';
    }
}
