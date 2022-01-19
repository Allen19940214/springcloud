package com.yuan.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName
@Builder
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer uid;
    private String username;
    private String password;
    private String email;
    private Integer age;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @Version//乐观锁
    private Integer version;
    //@TableLogic
    private Integer deleted;
}
