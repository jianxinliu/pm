package pm.test.mapping;

import com.minister.pm.define.Autowired;
import com.minister.pm.define.RestController;
import com.minister.pm.define.URLMapping;

/**
 *
 * @author ljx
 * @Date Feb 24, 2019 10:37:39 PM
 *
 */
@URLMapping("/jx")
@RestController()
public class Controller {

	@Autowired
	private Service service;

	@URLMapping("/index")
	public String getIndex() {
		return "/html/index.html";
	}

	@URLMapping("/user")
	public String getUser() {
		return "/html/user.html";
	}

	@URLMapping("/hello")
	public String hello() {
		return service.hello();
	}
}
