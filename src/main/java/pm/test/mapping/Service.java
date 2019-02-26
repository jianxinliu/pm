package pm.test.mapping;

import com.minister.pm.define.Component;

/**
 *
 * @author ljx
 * @Date Feb 24, 2019 10:38:25 PM
 *
 */
@Component()
public class Service {
	public String hello() {
		return "{code:200,msg:'success',data:{a:3}}";
	}
}
