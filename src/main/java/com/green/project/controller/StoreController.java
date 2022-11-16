package com.green.project.controller;

import com.green.project.dto.SearchDto;
import com.green.project.dto.StoreDto;
import com.green.project.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/store")
public class StoreController {

    private final ItemService itemService;

    @GetMapping(value = {"/plant", "/plant/{page}"})
    public String plant(SearchDto searchDto, StoreDto storeDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9);
        Page<StoreDto> items = itemService.getStorePlantItemPage(searchDto, storeDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("storeDto", storeDto);
        model.addAttribute("maxPage", 3);
        return "store/plant";
    }

    @GetMapping(value = {"/tool", "/tool/{page}"})
    public String tool(SearchDto searchDto, StoreDto storeDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9);
        Page<StoreDto> items = itemService.getStoreToolItemPage(searchDto, storeDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("storeDto", storeDto);
        model.addAttribute("maxPage", 3);
        return "store/tool";
    }

    @GetMapping(value = {"/pot", "/pot/{page}"})
    public String pot(SearchDto searchDto, StoreDto storeDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9);
        Page<StoreDto> items = itemService.getStorePotItemPage(searchDto, storeDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("storeDto", storeDto);
        model.addAttribute("maxPage", 3);
        return "store/pot";
    }

    @GetMapping(value = {"/other", "/other/{page}"})
    public String other(SearchDto searchDto, StoreDto storeDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9);
        Page<StoreDto> items = itemService.getStoreOtherItemPage(searchDto, storeDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("storeDto", storeDto);
        model.addAttribute("maxPage", 3);
        return "store/other";
    }

}
