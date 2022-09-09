package sia.tacocloud.Web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import sia.tacocloud.TacoOrder;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")

public class OrderController {
    @GetMapping("/current") //combined with RequestMapping, this means that the orderForm() method will handle HTTP GET requests for /orders/current
    public String showOrderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(
            @Valid TacoOrder order,
            Errors errors,
            SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        log.info("Order submitted: {}", order);
        sessionStatus.setComplete(); //TacoOrder was initially created and placed into the session when the user created their first taco. by
        // calling setComplete(), we are ensuring that the session is cleaned up and ready for a new order the next time the user creates a taco

        return "redirect:/";
    }
}

//premise here is that after creating a taco
//user will be redirected to an order form to place an order
//to eventually have their tacos delivered
