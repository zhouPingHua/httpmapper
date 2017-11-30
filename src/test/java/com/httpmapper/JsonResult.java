package com.httpmapper;

import lombok.Data;

/**
 * @author zph  on 2017/11/29
 */
@Data
public class JsonResult<T> {

    private String code;

    private String reason;

    private T payload;

}
