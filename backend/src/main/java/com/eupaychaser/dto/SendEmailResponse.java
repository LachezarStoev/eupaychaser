package com.eupaychaser.dto;

public record SendEmailResponse(boolean sent, String provider, String message) {
}
