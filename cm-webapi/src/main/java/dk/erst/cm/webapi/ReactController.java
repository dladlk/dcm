package dk.erst.cm.webapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** 
 * Added to forward all non-api requests to react index.html
 */
@Controller
public class ReactController {

    @RequestMapping("/{path:[^\\.]+}/**")
    public String forward() {
        return  "forward:/";
    }
	
}
