package com.example.demo5.controller;

import com.example.demo5.domain.Comment;
import com.example.demo5.domain.Product;
import com.example.demo5.domain.User;
import com.example.demo5.service.CommentService;
import com.example.demo5.service.ProductService;
import com.example.demo5.util.SecurityUtils;
import org.hibernate.annotations.Any;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Controller
@EnableAutoConfiguration
public class IndexController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CommentService commentService;
//    使用默认
    private final Logger log= LoggerFactory.getLogger(IndexController.class);

//    主页控制
    @GetMapping({"/","/index"})
    String index(Model model,@PageableDefault(size = 16, sort = {"createTime"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("user", SecurityUtils.getUser());
        model.addAttribute("page",productService.listProduct(pageable));
        return "index";
    }

//搜索
    @PostMapping("search")
    String search(@RequestParam String query,Model model,
                  @PageableDefault(size = 8, direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("page", productService.listProduct("%"+query+"%", pageable));
        return "search";
    }

    //主页中不同的物品分类
    @GetMapping(value = "index{type}")
    String indexType(@PathVariable String type,Model model,
                  @PageableDefault(size = 8, direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("type",type);
        model.addAttribute("page", productService.listProductType(type, pageable));
        return "index";
    }



}
