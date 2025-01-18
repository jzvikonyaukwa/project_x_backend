package com.axe.common.agGrid.request;

import lombok.Data;

@Data
public class ColumnVO {
    private String id;
    private String field;
    private String displayName;
    private String aggFunc;
}
