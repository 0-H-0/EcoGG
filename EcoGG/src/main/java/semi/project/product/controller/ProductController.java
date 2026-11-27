package semi.project.product.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import semi.project.product.model.vo.Product;
import semi.project.product.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {
	private final ProductService pService;

	private static final int PAGE_SIZE = 9;
	private final List<String> categories = List.of("생활용품", "주방용품", "패션", "세제·청소");

	@GetMapping({ "/product", "/product/list", "/product/detail" })
	public String productList(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "category", defaultValue = "all") String category,
			@RequestParam(name = "keyword", defaultValue = "") String keyword, Model model) {
		ArrayList<Product> pList = pService.selectAllProduct();
		
		String selectedCategory = categories.contains(category) ? category : "all";
		String normalizedKeyword = keyword.trim().toLowerCase(Locale.ROOT);
		
		System.out.println(pList);

		int totalPages = (pList.size() + PAGE_SIZE - 1) / PAGE_SIZE;
		int currentPage = Math.max(1, Math.min(page, Math.max(totalPages, 1)));

		int fromIndex = (currentPage - 1) * PAGE_SIZE;
		int toIndex = Math.min(fromIndex + PAGE_SIZE, pList.size());

		List<Product> pageList = pList.subList(fromIndex, toIndex);

		model.addAttribute("products", pageList);
		model.addAttribute("categories", categories);
		model.addAttribute("selectedCategory", selectedCategory);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageNumbers",
		        IntStream.rangeClosed(1, totalPages).boxed().toList());
		
		return "views/product/list";
	}

}