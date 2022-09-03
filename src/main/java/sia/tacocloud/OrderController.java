package sia.tacocloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")

public class OrderController {
    @GetMapping("/current") //combined with RequestMapping, this means that the orderForm() method will handle HTTP GET requests for /orders/current
    public String showOrderForm() {
        return "orderForm";
    }
}
