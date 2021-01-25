package Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {
    @RequestMapping("/index")
    @ResponseBody
    public Map<String,String> index(){
        Map map = new HashMap<String,String>();
        map.put("say:","小王宇");
        map.put("name:","徐凯");
        map.put("l&*:","田家兴");
        return map;
    }
}
