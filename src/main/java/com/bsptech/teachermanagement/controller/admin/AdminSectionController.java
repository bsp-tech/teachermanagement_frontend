/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsptech.teachermanagement.controller.admin;

import com.bsptech.teachermanagement.dao.SectionDataInter;
import com.bsptech.teachermanagement.entity.Section;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author murad_isgandar
 */
@Controller
@RequestMapping("/admin/sections")
public class AdminSectionController {

    @Autowired
    private SectionDataInter sectionDataInter;

    @GetMapping
    public ModelAndView page(ModelAndView modelAndView) {
        Iterable<Section> sections = sectionDataInter.findAll();
        modelAndView.addObject("sections", sections);
        modelAndView.setViewName("admin/sections");
        return modelAndView;
    }

    @GetMapping(value = "/search")
    public ModelAndView search(@RequestParam(value = "playlistUrl") String playlisturl,
            @RequestParam(value = "threadFilesPath") String threadFilesPath,
            @RequestParam(value = "thumbnailPath") String thumbnailPath,
            ModelAndView modelAndView) {

        if ((playlisturl != null && !playlisturl.isEmpty()) || (threadFilesPath != null && !threadFilesPath.isEmpty()) || (thumbnailPath != null && !thumbnailPath.isEmpty())) {
            List<Section> result = sectionDataInter.getSectionsBySearching(playlisturl, threadFilesPath, thumbnailPath);
            modelAndView.addObject("sections", result);
            modelAndView.setViewName("admin/sections");
        } else {
            return new ModelAndView("redirect:/admin/sections");
        }

        return modelAndView;
    }

    @PostMapping(value = "/add")
    public ModelAndView add(@ModelAttribute("section") Section section) {

        section.setInsertDateTime(new java.sql.Date(new Date().getTime()));
        sectionDataInter.save(section);
        return new ModelAndView("redirect:/admin/sections");
    }

    @PostMapping(value = "/update")
    public ModelAndView update(@RequestParam(name = "id") Integer id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "about", required = false) String about,
            @RequestParam(name = "playlistUrl", required = false) String playlistUrl,
            @RequestParam(name = "threadFilesPath", required = false) String threadFilesPath,
            @RequestParam(name = "thumbnailPath", required = false) String thumbnailPath,
            @RequestParam(name = "price", required = false) BigDecimal price
    ) {

        if ((name != null && !name.isEmpty()) || (about != null && !about.isEmpty())
                || (playlistUrl != null && !playlistUrl.isEmpty()) || (threadFilesPath != null && !threadFilesPath.isEmpty())
                || (thumbnailPath != null && !thumbnailPath.isEmpty()) || (price != null && !price.equals(BigDecimal.ZERO))) {

            Optional<Section> result = sectionDataInter.findById(id);
            Section section = result.get();

            if (name != null && !name.isEmpty()) {
                section.setName(name);
            }
            if (about != null && !about.isEmpty()) {
                section.setAbout(about);
            }
            if (playlistUrl != null && !playlistUrl.isEmpty()) {
                section.setPlaylistUrl(playlistUrl);
            }
            if (threadFilesPath != null && !threadFilesPath.isEmpty()) {
                section.setThreadFilesPath(threadFilesPath);
            }
            if (thumbnailPath != null && !thumbnailPath.isEmpty()) {
                section.setThumbnailPath(thumbnailPath);
            }
            if (price != null && !price.equals(BigDecimal.ZERO)) {
                section.setPrice(price);
            }

            section.setLastUpdateTime(new java.sql.Date(new Date().getTime()));
            sectionDataInter.save(section);

        }

        return new ModelAndView("redirect:/admin/sections");
    }

    @PostMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(name = "id") Integer id) {

        sectionDataInter.deleteById(id);
        return new ModelAndView("redirect:/admin/sections");
    }

}
