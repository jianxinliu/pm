package pm.test.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import com.minister.pm.define.Autowired;
import com.minister.pm.define.URLMapping;

/**
 *
 * @author ljx
 * @Date Feb 24, 2019 10:46:12 PM
 *
 */
public class Run {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {

		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String url = sc.nextLine().trim();
			if (!"".equals(url)) {
				try {
					String path = mapping(Controller.class, url);
					System.out.println(path);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String mapping(Class clz, String v)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		String ret = "";
		Object newInstance = clz.newInstance();
		// auto wire
//		if(clz.isAnnotationPresent(Component.class)) {
		Field[] fields = clz.getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			Field f = fields[j];
			if (f.isAnnotationPresent(Autowired.class)) {
				Class<?> type = f.getType();
				f.setAccessible(true);
				if (f.get(newInstance) == null) {
					f.set(newInstance, type.newInstance());
				}
			}
		}
//		}

		// url mapping
		Method[] methods = clz.getMethods();
		int i;
		for (i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (m.isAnnotationPresent(URLMapping.class)) {
				URLMapping mapper = m.getAnnotation(URLMapping.class);
				String value = mapper.value();
				if (v.equals(value)) {
					Object invoke = m.invoke(newInstance, new Object[] {});
					ret = (String) invoke;
				}
			}
		}

		if (i == methods.length && "".equals(ret)) {
			ret = "no such url";// should throw error
		}
		return ret;
	}
}
