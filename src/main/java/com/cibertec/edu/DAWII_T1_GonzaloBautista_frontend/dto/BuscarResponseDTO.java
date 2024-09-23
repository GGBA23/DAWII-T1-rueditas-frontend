package com.cibertec.edu.DAWII_T1_GonzaloBautista_frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public record BuscarResponseDTO(String codigo, String mensaje, String marca, String modelo, String numeAsientos, String precio, String color) {
}
