package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {

        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 주의!
     *
     * 요청 시 파라미터 이름만 사용
     * /request-param?username=
     * 파라미터 이름만 있고 값이 없는 경우 -> 빈문자("")로 되어
     * required=true에 안걸리고 통과 됨
     *
     * 기본형(primitive)에 null 입력(요청 시 전송 안한 경우)
     * /request-param?username=hello (age 제외)
     * 이 경우 required=false로 해도 에러(500)
     * 이유는 primitive 타입의 경우는 null값이 입력되지 않음
     * 따라서 이 경우는 Integer 타입을 사용해야함(Integer의 경우 객체이기 때문에 null 입력 가능)
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * defaultValue를 사용하는 경우 required는 사실상 불필요
     * 요청시 없는 경우 defaultValue값이 들어가기 때문
     * 요청시 빈문자로 들어오는 경우에도 defaultValue로 적용됨
     * /request-param-default?username=
     * => username=guest, age=-1
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamDefault(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
//        HelloData helloData = new HelloData();
//        helloData.setUsername(username);
//        helloData.setAge(age);

//        log.info("username={}, age={}", username, age);
        log.info("helloData={}", helloData);

        return "ok";
    }

    /**
     * 스프링은 해당 생략시 아래 규칙이 적용됨
     * String, int, Integer 같은 단순 타입의 경우 @RequestParam 적용
     * 그 외 나머지의 경우는 @ModelAttribute 적용 (단, argument resolver로 지정된 타입은 제외)
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("helloData={}", helloData);

        return "ok";
    }
}
