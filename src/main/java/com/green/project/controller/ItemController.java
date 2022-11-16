package com.green.project.controller;

import com.green.project.dto.ItemFormDto;
import com.green.project.dto.SearchDto;
import com.green.project.entity.Item;
import com.green.project.service.ItemService;
import com.green.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    // 새 상품 등록
    @GetMapping(value = "/business/item/new")
    public String itemForm(Model model, Principal principal) {
        String brandName = memberService.getBrandByEmail(principal.getName()); // 브랜드 이름을 받아서 자동 입력
        model.addAttribute("brandName", brandName);
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    // 새 상품 등록
    @PostMapping(value = "/business/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, Principal principal,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                          @RequestParam("itemDetailImgFile") List<MultipartFile> itemDetailImgFileList) {
        String brandName = memberService.getBrandByEmail(principal.getName()); // 브랜드 이름을 받아서 자동 입력
        model.addAttribute("brandName", brandName);

        if (bindingResult.hasErrors()) { // 상품 등록 중 필수 값이 없는 경우
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) { // 첫 번째 이미지가 없는 경우
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        if (itemDetailImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) { // 첫 번째 이미지가 없는 경우
            model.addAttribute("errorMessage", "첫 번째 상품 상세 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            itemFormDto.setItemBrand(brandName);
            itemService.saveItem(itemFormDto, itemImgFileList, itemDetailImgFileList); // 상품 저장 로직 호출
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/business/items"; // 처리 완료 시 상품 관리 페이지로 이동
    }

    // 상품 수정 또는 삭제 페이지
    @GetMapping(value = "/business/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model, Principal principal) {
        String brandName = memberService.getBrandByEmail(principal.getName());
        model.addAttribute("brandName", brandName);

        try {
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
            model.addAttribute("itemFormDto", itemFormDto); // 조회한 상품 데이터를 모델에 담아서 뷰로 전달하기
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());

            return "item/itemForm";
        }
        return "item/itemForm";
    }

    // 상품 수정 또는 삭제 페이지
    @PostMapping(value = "/business/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, Principal principal,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                          @RequestParam("itemDetailImgFile") List<MultipartFile> itemDetailImgFileList) {
        String brandName = memberService.getBrandByEmail(principal.getName());
        model.addAttribute("brandName", brandName);

        if (bindingResult.hasErrors()) { // 상품 등록 중 필수 값이 없는 경우
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) { // 첫 번째 이미지가 없는 경우
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        if (itemDetailImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) { // 첫 번째 이미지가 없는 경우
            model.addAttribute("errorMessage", "첫 번째 상품 상세 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            itemFormDto.setItemBrand(brandName);
            itemService.updateItem(itemFormDto, itemImgFileList, itemDetailImgFileList); // 상품 저장 로직 호출
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/business/items"; // 처리 완료 시 상품 관리 페이지로 이동
    }

    // 상품 삭제
    @PostMapping(value="/business/item/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") Long itemId) throws Exception {
        itemService.deleteItem(itemId);
        return "redirect:/business/items";
    }


    // 상품 관리 페이지
    @GetMapping(value = {"/business/items", "/business/items/{page}"})
    public String itemManage(SearchDto searchDto,
                             @PathVariable("page") Optional<Integer> page, Model model, Principal principal) {
        String brandName = memberService.getBrandByEmail(principal.getName());
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Item> items = itemService.getItemManagementPage(searchDto, pageable, brandName);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", searchDto);
        model.addAttribute("maxPage", 5);
        return "store/management";
    }

    // 상품 상세 페이지
    @GetMapping(value = "/store/itemDetail/{itemId}")
    public String itemDetail(Model model, @PathVariable("itemId") Long itemId, Principal principal) {
        String brandName = memberService.getBrandByEmail(principal.getName());
        ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
        model.addAttribute("item", itemFormDto);
        model.addAttribute("brandName", brandName);
        return "item/itemDetail"; }

}
