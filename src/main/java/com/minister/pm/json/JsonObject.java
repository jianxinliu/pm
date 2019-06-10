package com.minister.pm.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

import com.minister.pm.json.status.TypeEnums;

/**
 * 对象转 JSON 字符串
 * @author ljx
 * @Date Mar 30, 2019 2:30:42 AM
 *
 */
public class JsonObject {

	public static String toJSONString(Object obj) {
		String ret = "";
		
		return ret;
	}
	
	private static void reflect(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field f : fields) {
			f.setAccessible(true);
			
		}
	}
	
	/**
	 * 将数组和列表转换成 JSON 字符串
	 * @param arr
	 * @return
	 */
	private static String arrayToJSONString(Array arr) {
		String ret = "";
		
		return ret;
	}
	
	/**
	 * 将对象转换成 JSON 字符串
	 * @param obj
	 * @return
	 */
	private static String objectToJSONString(Object obj) {
		String ret = "";
		
		return ret;
	}
	
	private static int typeDetect(Object obj) {
		int ret = 0;
		if(obj instanceof Array || obj instanceof List) {
			ret = TypeEnums.ARRAY.getIndex();
		}else {
			ret = TypeEnums.OBJECT.getIndex();
		}
		return ret;
	}
	
	public static void main(String[] args) {
		Stu stu = new Stu();
		
		stu.age = 3;
		stu.names = new String[] {"mary","hello"};
		typeDetect(stu);
	}
}


class Stu{
	int age;
	
	String[] names;
	
	Teacher teacher = new Teacher();
}

class Teacher{
	String name = "Jack Ma";
}