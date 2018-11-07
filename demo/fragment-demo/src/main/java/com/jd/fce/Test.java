package com.jd.fce;

import com.jd.fce.fragment.core.annotation.ExportMethod;
import com.jd.fce.fragment.core.annotation.ExportService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by hanyang1 on 2017/10/19.
 */
@ExportService(desc = "测试desc", appGroup = "测试appGroup", serGroup = "测试serGroup")
@Service
public class Test {

    @ExportMethod(desc = "测试方法desc")
    public int testFragment(Date date, List<Person> personList, long tid){
        System.out.println(date);
        return 1;
    }

    @ExportMethod
    public Double testHello(String hello){

        return null;
    }

    public static void main(String[] args) {
        /*FragmentServlet servlet = new FragmentServlet();
        Map<String, String> map = new HashMap<String, String>();
        map.put("age", "11");
        map.put("name", "yhan");
        System.out.println(FragmentJsonUtil.toJSON(map));
        Person person = new Person();
        person.setAge(11);
        person.setName("yhan");
        List<Person> list = new ArrayList<Person>();
        list.add(person);
        System.out.println(FragmentJsonUtil.toJSON(list));
        Map<String, String> strings = FragmentJsonUtil.parseObject("{\"name\":\"yhan\",\"age\":\"11\"}", Map.class);
        System.out.println();*/
        boolean b = Long.class.isAssignableFrom(long.class.getClass());
        boolean bb = long.class.isPrimitive();
        boolean bbb = long.class.getClass().isPrimitive();
        System.out.print(b + "===" + bb + "===" + bbb);
    }
}
