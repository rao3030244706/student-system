package com.example.demo.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.example.demo.annotation.ExcelExport;
import com.example.demo.validation.group.Add;
import com.example.demo.validation.group.Delete;
import com.example.demo.validation.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 学生信息表
 * </p>
 *
 * @author nickle
 * @since 2020-10-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "EStudent对象", description = "学生信息表")
public class EStudent implements Serializable {

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
    //todo id就不要NotNull了，系统内部给一个
    @TableId("ID")
    @Null(groups = Add.class)
    @NotNull(groups = {Update.class, Delete.class})
    private String id;

    @ExcelExport
    @NotBlank(groups = {Add.class})
    @ApiModelProperty(value = "中文名字")
    private String cnName;

    @ExcelExport
    @ApiModelProperty(value = "英文名称")
    private String enName;

    @ExcelExport
    @NotNull(groups = Add.class)
    @ApiModelProperty(value = "出生日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime birthDate;


    @NotBlank(groups = Add.class)
    @Pattern(regexp = "^362502.*")
    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ExcelExport
    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "省份")
    private String provinceName;


    /*provinceName-->上海*/
    public interface JiangSu {
    }

    /*provinceName-->四川*/
    public interface SiChuan {
    }

    //todo 当provinceName为四川时，cityName必须是成都 \u6210\u90fd  使用jdk的bin的native2ascii.exe
    //todo 敲cmd 再敲native2ascii.exe 鼠标标记+右点
    //todo  当provinceName为江苏时，cityName必须是南京 \u5357\u4eac
    @ExcelExport
    @NotNull(groups = Add.class)
    @Pattern(regexp = "\\u6210\\u90fd", groups = SiChuan.class)
    @Pattern(regexp = "\\u5357\\u4eac", groups = JiangSu.class)
    @ApiModelProperty(value = "市")
    private String cityName;

    @ExcelExport
    @ApiModelProperty(value = "性别 0：未知，1：男；2：女")
    private String sex;

    @ExcelExport
    @ApiModelProperty(value = "职位 0：无；1：班长；2：副班长；3：学习委员；4：语文课代表；5：数学课代表；6：英语课代表；7：物理课代表；8：化学课代表；9：体育课代表")
    private String position;

    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "是否党员 1:是；0：否")
    private String isPartyMember;

    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "是否团员 1:是；0：否")
    private String isLeagueMember;

    @ExcelExport
    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "所属年级")
    private String grade;

    @ExcelExport
    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "所属班级")
    private String clazz;


    @ApiModelProperty(value = "是否留级 1:是；0：否")
    private String isRepeater;


    @ApiModelProperty(value = "是否毕业")
    private String isGraduation;


    @ApiModelProperty(value = "是否择校生 1:是；0：否")
    private String isSchoolChioceStudent;


    @ApiModelProperty(value = "是否受处分 1:是；0：否")
    private String isPunishment;

    @ExcelExport
    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "手机号")
    private String telNo;

    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "微信号")
    private String wechatNo;

    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "QQ号")
    private String qqNo;

    @ExcelExport
    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "父亲名称")
    private String fatherName;

    @ExcelExport
    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "母亲名称")
    private String motherName;

    @ApiModelProperty(value = "父亲职业")
    private String fatherOccupation;

    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "父亲电话号码")
    private String fatherTelNo;

    @NotBlank(groups = Add.class)
    @ApiModelProperty(value = "母亲电话号码")
    private String motherTelNo;

    @ApiModelProperty(value = "备注")
    private String remark;


}
