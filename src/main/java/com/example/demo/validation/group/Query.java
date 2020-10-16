package com.example.demo.validation.group;


/**
 * query
 *
 * @Validated(value = Add.class)
 * @Valid与@Validated()缺省是Default.class <p></p>
 * @TableId("ID")
 * @Null(groups = Add.class)
 * @NotNull(groups = {Update.class, Delete.class})
 * private String id;
 * <p></p>
 * @NotBlank() or@NotBlank都是数与Default.class
 * @ApiModelProperty(value = "中文名字")
 * private String cnName;
 */
public interface Query {

}
