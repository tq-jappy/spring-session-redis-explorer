package session.redis.explorer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import session.redis.explorer.repository.ExpirationSessionRepository;
import session.redis.explorer.repository.MapSessionRepository;

/**
 *
 */
@Controller
@RequestMapping("")
public class RootController {

    private MapSessionRepository mapSessionRepository;

    private ExpirationSessionRepository expirationSessionRepository;

    @Autowired
    public RootController(MapSessionRepository mapSessionRepository,
                          ExpirationSessionRepository expirationSessionRepository) {
        this.mapSessionRepository = mapSessionRepository;
        this.expirationSessionRepository = expirationSessionRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getIndex(Model model) {
        model.addAttribute("mapSessionList", mapSessionRepository.getAll());
        model.addAttribute("expirationSessionList", expirationSessionRepository.getAll());

        return "index";
    }
}