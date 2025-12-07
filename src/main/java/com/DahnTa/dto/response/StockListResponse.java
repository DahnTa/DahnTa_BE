package com.DahnTa.dto.response;

import com.DahnTa.dto.DashBoard;
import java.util.List;
import lombok.Builder;

@Builder
public record StockListResponse(List<DashBoard> dashBoard) {

    public static StockListResponse create(List<DashBoard> dashBoard) {

        return new StockListResponse(dashBoard);
    }
}
