package com.mytest.other_test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JacksonTest {
    // 可重复使用的对象
    ObjectMapper mapper = new ObjectMapper();

    @Test
    void toString_and_toObject() throws IOException {

        // 不序列化为null的字段
        // mapper.configure(WRITE_NULL_MAP_VALUES, false); // 无效?
        // mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 设置日期格式
        // mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        // 是否自动关闭, 比如`mapper.writeValue(流, 对象)`, 会自动关闭流
        // mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        // 忽略未知的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // object -> string时, 先要获得ObjectWriter实例 (不对, 直接: mapper.writeValueAsString)
        String toString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(zhangsan);
        System.out.println(toString);

        String toString_withnot_pretty = mapper.writeValueAsString(zhangsan);
        System.out.println(toString_withnot_pretty);

        // string -> object
        Student_JacksonTest toObject = mapper.readValue(toString, Student_JacksonTest.class);
        System.out.println("String -> Object:\n " + toObject);

        // map -> object
        Student_JacksonTest mapConvertToObject = mapper.convertValue(new HashMap<String, String>() {{
            put("name", "lisi");
            put("age", "11");
            put("birthday", "2000-01-01");
            put("unknownProperty&(&(", "sdfsf"); //未知的属性
        }}, Student_JacksonTest.class);
        // 使用mapper.writeValue(流, object)方法后, 会自动关闭流,
        // 比如下面这里会造成: System.out.close(); 这句话会造成执行中断
        // mapper.writeValue(System.out, mapToObject.toString());
        System.out.println("mapConvertToObject:\n " + mapConvertToObject);

        // string -> map
        // Map map = JSONObject.parseObject(zhangsan.toString(), Map.class);
        // 异常 `对象->对象`应该用convertValue, `字符串/流->对象`应该用readValue
        // Map stringToMap = mapper.convertValue(zhangsan.toString(), HashMap.class);
        @SuppressWarnings("unchecked")
        Map<String, String> stringToMap = mapper.readValue(zhangsan.toString(), Map.class);
        System.out.println("stringToMap:\n " + stringToMap);
    }

    @Test
    void tree_model() throws IOException {
        JsonNode rootNode = mapper.readTree(mapper.writeValueAsString(zhangsan));
        JsonNode schoolNode = rootNode.path("school");
        // JsonNode schoolNode2 = rootNode.get("school");
        schoolNode.fields().forEachRemaining(stringJsonNodeEntry -> {
            //
            System.out.println(String.format("name: %s, value: %s",
                    stringJsonNodeEntry.getKey(), stringJsonNodeEntry.getValue()));
        });
    }

    @Test
    void streaming_api() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();

        // 对应的, 还有解析parse: jsonFactory.createParser()
        JsonGenerator generator = jsonFactory.createGenerator(System.out);

        // generator.writeString("你好啊");

        // 异常: No ObjectCodec defined for the generator
        // generator.writeObject(Arrays.asList("one", "two", "three", "four"));
        // generator.writeObject(zhangsan);

        // 实际是这样用的 ⬇⬇⬇ (手动控制的)
        // {
        generator.writeStartObject(); // 输出对象序列化后的开始标识 {

        generator.writeFieldName("name"); // 输出字段名, 先输出字段, 后输出字段值, 顺序也要对
        generator.writeString(zhangsan.getName()); // 输出字段值

        generator.writeFieldName("age");
        generator.writeNumber(zhangsan.getAge());

        // }
        generator.writeEndObject(); // 输出对象序列化后的结束标识 }

        generator.close();
    }

    School_JacksonTest school = new School_JacksonTest("代码专修技校", "长江西路0133号");

    Student_JacksonTest zhangsan =
            new Student_JacksonTest("zhangsan", 22, 0, "安徽合肥蜀山", school, new Date(),
                    new HashMap<>() {{
                        put("备注", "给学生的备注");
                    }});
}

/**
 * Jackson在string->object时, 必须是静态的内部类,
 * 因此直接将`Student & School`内部类转到外部去了
 */
@JsonIgnoreProperties({"ignoreStringTwo"})
// @JsonInclude(JsonInclude.Include.NON_NULL) // 类级别的不输出null字段

class Student_JacksonTest {
    @Override
    public String toString() {
        // 使用FastJson, 区分开
        return com.alibaba.fastjson.JSONObject.toJSONString(this);
    }

    public Student_JacksonTest() {
    }

    public Student_JacksonTest(String name, Integer age, Integer gender, String address, School_JacksonTest school,
                               Date birthday, Map<String, String> mark) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.school = school;
        this.birthday = birthday;
        this.mark = mark;
    }

    private String name;
    private Integer age;
    private Integer gender;
    private String address;
    private School_JacksonTest school;
    @JsonProperty("nullObject")
    private School_JacksonTest oldNullObject;
    private Date birthday;
    private Map<String, String> mark;
    @JsonIgnore
    private String ignoreString;
    private String ignoreStringTwo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public School_JacksonTest getSchool() {
        return school;
    }

    public void setSchool(School_JacksonTest school) {
        this.school = school;
    }

    public School_JacksonTest getOldNullObject() {
        return oldNullObject;
    }

    public void setOldNullObject(School_JacksonTest oldNullObject) {
        this.oldNullObject = oldNullObject;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Map<String, String> getMark() {
        return mark;
    }

    public void setMark(Map<String, String> mark) {
        this.mark = mark;
    }

    public String getIgnoreString() {
        return ignoreString;
    }

    public void setIgnoreString(String ignoreString) {
        this.ignoreString = ignoreString;
    }

    public String getIgnoreStringTwo() {
        return ignoreStringTwo;
    }

    public void setIgnoreStringTwo(String ignoreStringTwo) {
        this.ignoreStringTwo = ignoreStringTwo;
    }

}

class School_JacksonTest {
    public School_JacksonTest() {
    }

    public School_JacksonTest(String name, String address) {
        this.name = name;
        this.address = address;
    }

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
