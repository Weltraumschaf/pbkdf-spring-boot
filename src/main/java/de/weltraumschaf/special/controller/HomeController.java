package de.weltraumschaf.special.controller;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.share.DiskShare;
import de.weltraumschaf.special.BuildInfo;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;

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
    public ModelAndView connect(ConnectRequest formData) throws IOException {
        final var authCtx = new AuthenticationContext(
            formData.getUsername(),
            formData.getPassword().toCharArray(),
            formData.getDomain());

        final var fileEntries = new ArrayList<String>();

        try (final var client = new SMBClient();
             final var connection = client.connect(formData.getServer())) {
            final var session = connection.authenticate(authCtx);

            // Connect to Share
            try (final var share = (DiskShare) session.connectShare(formData.getShare())) {
                for (FileIdBothDirectoryInformation f : share.list("sxs")) {
                    fileEntries.add(f.getFileName());
                }
            }
        }

        return createModelAndView("home/connect")
            .addObject("fileEntries", fileEntries);
    }

    private ModelAndView createModelAndView(String viewName) {
        return new ModelAndView(viewName)
            .addObject("buildInfo", buildInfo);
    }
}
