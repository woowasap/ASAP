package shop.woowasap.mock.dto;

import java.util.List;

public record OrdersResponse(List<OrderResponse> orders, int page, int totalPage) {

}
