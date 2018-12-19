package com.lyn.lost_and_found.web.vo;

import com.lyn.lost_and_found.domain.LfGoods;
import lombok.Data;

@Data
public class LfGoodsVO extends LfGoods {
    /**
     * 物品类别名称
     */
    private String categoryName;
}
