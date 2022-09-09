package sia.tacocloud.Web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;

import sia.tacocloud.Ingredient;
import sia.tacocloud.Ingredient.Type;
import sia.tacocloud.Taco;
import sia.tacocloud.TacoOrder;

import javax.validation.Valid;

@Slf4j
//automatically generates a SLF4J that allows end user to plug in the desired logging framework at deployment time
@Controller
//identifies class as controller and to mark it as candidate for component scanning - automatically create a
// DesignTacoController as a bean in Spring application context
@RequestMapping("/design")
//applied in this case to class level - specifies the kind of requests that this controller handles - in this case,
// it will handle requests from the path that begins with "/design"
@SessionAttributes("tacoOrder")
//Indicates that the TacoOrder object should be maintained in session. This is important because the creation of a
// taco is also the first step in creating an order, and the order we create will need to be carried in the session
// so that it can span multiple requests
public class DesignTacoController {

    @ModelAttribute
    public void addIngredientsToModel(Model model) { // Method is invoked when request handled and will construct list of ingredient objects to put
        // into the model. later this will be pulled from a database.
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), //filtered list is then added as attribute to Model object that will be passed into
                    // showDesignForm()
                    filterByType(ingredients, type)); //filters the list by ingredient type using filterByType
        }
    }

    @ModelAttribute(name = "tacoOrder") //Creates a new tacoOrder into the model
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco") //Creates a new taco into the model
    public Taco taco() {
        return new Taco();
    }

    // Paired with @RequestMapping specifies that when an HTTP Get request is received by /design, Spring MVC will
    // call showDesignForm() to handle the request
    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping //Handles POST requests
    public String processTaco(
            @Valid Taco taco, Errors errors, //@Valid is for validation on the submitted taco - tells Spring MVC to perform validation on the
            // submitted Taco object after it's bound to the submitted form data and before the processTaco() method is called
            @ModelAttribute TacoOrder tacoOrder) { //ModelAttribute applied to the TacoOrder parameter indicates that it should
        // use the TacoOrder object that was placed into the model
        if (errors.hasErrors()) {
            return "design";
        }
        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

}
