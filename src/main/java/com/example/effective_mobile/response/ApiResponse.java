package com.example.effective_mobile.response;

import lombok.Builder;

@Builder
public record ApiResponse(String message, boolean success, String token)
{

}
