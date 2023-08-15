package shop.woowasap.order.domain.in.response;

import java.util.List;

public record OrdersResponse(List<OrderResponse> orders, int page, int totalPage) {

}
