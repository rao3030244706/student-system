package com.example.demo.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 班级表
 * </p>
 *
 * @author nickle
 * @since 2020-10-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EClass对象", description="班级表")
public class EClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "乐观锁")
    @TableField("REVISION")
    private Integer revision;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATED_BY")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATED_TIME")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新人")
    @TableField("UPDATED_BY")
    private String updatedBy;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATED_TIME")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "逻辑删除 1:是；0:否")
    @TableField("IS_DELETE")
    private String isDelete;

    @ApiModelProperty(value = "主键")
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班主任id")
    private String masterTeacherId;

    @ApiModelProperty(value = "所属年级id")
    private String gardeId;

    @ApiModelProperty(value = "备注")
    private String remark;


}
