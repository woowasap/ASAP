package shop.woowasap.order.domain.out.response;

import java.util.List;
import shop.woowasap.order.domain.Order;

public record OrdersPaginationResponse(List<Order> orders, int page, int totalPage) {

}
