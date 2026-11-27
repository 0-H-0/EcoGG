package semi.project.product.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import semi.project.product.model.mapper.ProductMapper;
import semi.project.product.model.vo.Product;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductMapper mapper;

	public ArrayList<Product> selectAllProduct() {
		return mapper.selectAllProduct();
	}
	
	
}
