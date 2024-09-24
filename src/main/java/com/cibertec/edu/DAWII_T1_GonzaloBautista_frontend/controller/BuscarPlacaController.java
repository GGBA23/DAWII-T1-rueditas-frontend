package com.cibertec.edu.DAWII_T1_GonzaloBautista_frontend.controller;

import com.cibertec.edu.DAWII_T1_GonzaloBautista_frontend.dto.BuscarRequestDTO;
import com.cibertec.edu.DAWII_T1_GonzaloBautista_frontend.dto.BuscarResponseDTO;
import com.cibertec.edu.DAWII_T1_GonzaloBautista_frontend.viewmodel.BuscarPlacaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/buscar")
public class BuscarPlacaController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        BuscarPlacaModel buscarPlacaModel = new BuscarPlacaModel("00","","","","","","");
        model.addAttribute("buscarPlacaModel",buscarPlacaModel);
        return "inicio";
    }

    @PostMapping("/buscarplaca")
    public String buscarplaca(@RequestParam("codigoPlaca") String codigoPlaca, Model model){

        // Validar campos de entrada
        if (codigoPlaca == null || codigoPlaca.trim().length() == 0 ||
                codigoPlaca.trim().length() > 7 || codigoPlaca.trim().length() < 7 || !codigoPlaca.matches("^[a-zA-Z0-9-]+$")){

            BuscarPlacaModel buscarPlacaModel = new BuscarPlacaModel("01","Error: Debe ingresar una placa correcta","", "","", "", "");
            model.addAttribute("buscarPlacaModel",buscarPlacaModel);
            return "inicio";
        }

        try {
            //Invocar api de validacion de usuario
            String endpoint = "http://localhost:8081/buscador/buscar";
            BuscarRequestDTO buscarRequestDTO = new BuscarRequestDTO(codigoPlaca);
            BuscarResponseDTO buscarResponseDTO = restTemplate.postForObject(endpoint,buscarRequestDTO, BuscarResponseDTO.class);

            //Validar respuesta
            if(buscarResponseDTO.codigo().equals("00")){
                BuscarPlacaModel buscarPlacaModel = new BuscarPlacaModel("00","",buscarResponseDTO.marca(), buscarResponseDTO.modelo(),buscarResponseDTO.numeAsientos(), buscarResponseDTO.precio(), buscarResponseDTO.color());
                model.addAttribute("buscarPlacaModel",buscarPlacaModel);
                return "principal";
            }else{
                BuscarPlacaModel buscarPlacaModel = new BuscarPlacaModel("01","Error: No se encontró un vehículo para la placa ingresada","", "","", "", "");
                model.addAttribute("buscarPlacaModel",buscarPlacaModel);
                return "inicio";
            }
        } catch (Exception e){

            BuscarPlacaModel buscarPlacaModel = new BuscarPlacaModel("99","Error: Ocurrio un error","", "","", "", "");
            model.addAttribute("buscarPlacaModel",buscarPlacaModel);
            System.out.println(e.getMessage());
            return "inicio";
        }
    }

}
