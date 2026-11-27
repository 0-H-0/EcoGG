package semi.project.product.model.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import semi.project.product.model.vo.Product;

@Mapper
public interface ProductMapper {

	ArrayList<Product> selectAllProduct();

}
