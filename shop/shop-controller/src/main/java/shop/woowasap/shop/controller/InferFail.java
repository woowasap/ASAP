package shop.woowasap.shop.controller;

import org.springframework.stereotype.Controller;

@Controller
public class InferFail {

    public void hello() {
        String hello = null; // 다음 코드는 Infer ci에서 실패해야함.
        hello.toString();
    }

}
