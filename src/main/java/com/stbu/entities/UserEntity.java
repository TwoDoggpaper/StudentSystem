//实体类
package com.stbu.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class UserEntity {
    @TableId(type = IdType.AUTO)
    private Integer uid;
    private String username;
    private String password;
    private Integer age;
    private Integer type;

}
