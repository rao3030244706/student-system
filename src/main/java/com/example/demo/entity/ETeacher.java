package com.example.demo.entity;

import java.math.BigDecimal;
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
 * 老师信息表
 * </p>
 *
 * @author nickle
 * @since 2020-10-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ETeacher对象", description="老师信息表")
public class ETeacher implements Serializable {

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

    @ApiModelProperty(value = "教师名称")
    private String teachName;

    @ApiModelProperty(value = "入职时间")
    private LocalDateTime entryTime;

    @ApiModelProperty(value = "老师薪资")
    private BigDecimal salary;

    @ApiModelProperty(value = "教师所教科目id")
    private String subjectId;

    @ApiModelProperty(value = "教师出生年月日")
    private LocalDateTime birthDate;

    @ApiModelProperty(value = "教师身份证")
    private String idCard;

    @ApiModelProperty(value = "教师手机号")
    private String telNo;

    @ApiModelProperty(value = "工龄")
    private BigDecimal workYear;

    @ApiModelProperty(value = "是否退休")
    private String isRetire;

    @ApiModelProperty(value = "是否单身")
    private String isSingle;

    @ApiModelProperty(value = "备注")
    private String remark;


}
