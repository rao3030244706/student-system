package com.example.demo;

import com.example.demo.entity.EStudent;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 创建Entity的properties配置文件
 */
@Slf4j
public class CreateEntityProperties {
    /*项目路径*/
    private static String projectPath;

    static {
        projectPath = System.getProperty("user.dir");
    }

    public static void main(String[] args) {
        //给一个路劲就行了且前面一定要加文件分隔符，生成的文件名是默认的
        String descPath = "\\src\\main\\resources\\entityProperties";
        try {
            builderEntityPropertiesByApiModanno(EStudent.class, descPath);
            createEntityFiledType(EStudent.class, descPath);
            createEntityPositionY(EStudent.class, descPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * todo 构建entity的在数据库表的中文翻译的配置文件+选项配置文件
     * entitySaveDescPath==>类似于这样的前面 todo \src\main\resources\entityProperties
     *
     * @param aClass
     * @throws IOException
     */
    private static void builderEntityPropertiesByApiModanno(Class<?> aClass, String entitySaveDescPath) throws IOException {
        Map<String, List<String>> stringListMap = collectApiModAnnoValue(aClass);
        File file = new File(projectPath + entitySaveDescPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        //表中字段的中文翻译 职位
        Properties columnProperty = new Properties();
        //表中字段的中文选项-->5：数学课代表, 6：英语课代表
        Properties optionsProperty = new Properties();
        stringListMap.entrySet().forEach(entry -> {
            List<String> value = entry.getValue();
            columnProperty.put(entry.getKey(), value.get(0));
            if (value.size() != 1) {
                List<String> collect = value.stream().skip(1).collect(Collectors.toList());
                String[] strings = collect.toArray(new String[0]);
                String s = Arrays.toString(strings);
                optionsProperty.put(entry.getKey(), s);
            }
        });
        columnProperty.store(new FileWriter(new File(projectPath + entitySaveDescPath, aClass.getSimpleName() + "-column.properties"), false), "数据库中表字段的中文翻译");
        optionsProperty.store(new FileWriter(new File(projectPath + entitySaveDescPath, aClass.getSimpleName() + "-options.properties"), false), "表字段的选项");

    }

    /**
     * 收集entity包下的ApiModelProperty注解的value
     * Map<String, List<String>>key为Field的name，value的index(0)的是否为XXX，index(>0)就是选项
     *
     * @param aClass
     * @return
     */
    private static Map<String, List<String>> collectApiModAnnoValue(Class<?> aClass) {
        Map<String, List<String>> stringMap = Arrays.stream(aClass.getDeclaredFields()).
                skip(7).collect(Collectors.toMap(Field::getName, field -> {
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            String value = annotation.value();
            if (!value.contains("；")) {
                return Arrays.asList(value);
            }
            //逻辑删除 1:是；0:否
            List<String> list = new ArrayList<>();
            String s = value.substring(0, value.trim().indexOf(" "));
            list.add(s);
            String substring = value.substring(value.trim().indexOf(" ") + 1);
            //todo 这个弄了反而有点不好
            StringTokenizer tokenizer = new StringTokenizer(substring, "；");
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                list.add(token);
            }
            return list;
        }));
        return stringMap;
    }

    /**
     * todo 用于创建Entity的字段类型配置文件，便于后期的parse excel文件，去反射赋值
     *
     * @throws IOException
     */
    private static void createEntityFiledType(Class aClass, String entitySaveDescPath) throws IOException {
        File file = new File(projectPath + entitySaveDescPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        Field[] declaredFields = aClass.getDeclaredFields();
        Properties properties = new Properties();
        Arrays.stream(declaredFields).skip(7).forEach((field) -> {
            String name = field.getName();
            String className = field.getType().getName();
            properties.put(name, className);
        });
        //System.out.println(properties);
        properties.store(new FileWriter(new File(projectPath + entitySaveDescPath, aClass.getSimpleName() + "-fieldType.properties")), EStudent.class.getSimpleName() + "字段的类型");
    }

    /*用于记录 Y轴*/
    static int positionY = 0;

    private static void createEntityPositionY(Class aClass, String entitySaveDescPath) throws IOException {
        File file = new File(projectPath + entitySaveDescPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        Field[] declaredFields = aClass.getDeclaredFields();
        Properties properties = new Properties();
        Arrays.stream(declaredFields).skip(8).forEach((field) -> {
            String name = field.getName();
            properties.put(positionY + "_" + aClass.getSimpleName(), name);
            positionY++;
        });
        //使用完了重置
        positionY = 0;
        //System.out.println(properties);
        properties.store(new FileWriter(new File(projectPath + entitySaveDescPath, aClass.getSimpleName() + "-positionY.properties")), EStudent.class.getSimpleName() + "Y轴--绑定");
    }
}
