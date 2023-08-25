package shop.woowasap.shop.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class NoOffsetRequest extends PageRequest {

    protected NoOffsetRequest(final int page, final int size, final Sort sort) {
        super(page, size, sort);
    }

}
