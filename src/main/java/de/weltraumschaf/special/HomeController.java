package de.weltraumschaf.special;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
final class HomeController {
    private final BuildInfo buildInfo;

    @Autowired
    HomeController(@NonNull BuildProperties buildInfo) {
        super();
        this.buildInfo = new BuildInfo(buildInfo);
    }

    @GetMapping(value = "/")
    public ModelAndView home() {
        return createModelAndView("home/index");
    }

    @PostMapping(value = "/", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView connect(ConnectRequest formData) {
        return createModelAndView("home/connect")
            .addObject("formData", formData);
    }

    private ModelAndView createModelAndView(String viewName) {
        return new ModelAndView(viewName)
            .addObject("buildInfo", buildInfo);
    }
}
