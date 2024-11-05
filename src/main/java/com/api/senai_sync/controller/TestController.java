package com.api.senai_sync.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    // Endpoint público, acessível por qualquer pessoa
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Olá, público!";
    }

    // Endpoint privado, acessível apenas para usuários autenticados
    @GetMapping("/private/hello")
    public String privateHello() {
        return "Olá, usuário autenticado!";
    }

    // Endpoint exclusivo para MASTER
    @GetMapping("/master/hello")
    // @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public String masterHello() {
        return "Olá, MASTER! Você tem acesso total ao sistema.";
    }

    // Endpoint exclusivo para ADMIN
    @GetMapping("/admin/hello")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminHello() {
        return "Olá, ADMIN! Você tem acesso a funções administrativas.";
    }

    // Endpoint para ADMIN e MASTER
    @GetMapping("/admin/master/hello")
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MASTER')")
    public String adminMasterHello() {
        return "Olá, ADMIN ou MASTER! Vocês têm acesso compartilhado a este recurso.";
    }

    // Endpoint exclusivo para COLABORADOR
    @GetMapping("/colaborador/hello")
    // @PreAuthorize("hasAuthority('ROLE_COLABORADOR')")
    public String colaboradorHello() {
        return "Olá, COLABORADOR! Você tem acesso às funções de colaborador.";
    }

    // Endpoint exclusivo para USUARIO_SOMENTE_LEITURA
    @GetMapping("/reader/hello")
    // @PreAuthorize("hasAuthority('ROLE_USUARIO_SOMENTE_LEITURA')")
    public String readerHello() {
        return "Olá, usuário com permissão de leitura! Você pode visualizar dados, mas não modificá-los.";
    }

    // Endpoint acessível para COLABORADOR, ADMIN e MASTER
    @GetMapping("/colaborador/admin/hello")
    // @PreAuthorize("hasAnyAuthority('ROLE_COLABORADOR', 'ROLE_ADMIN', 'ROLE_MASTER')")
    public String colaboradorAdminHello() {
        return "Olá, COLABORADOR, ADMIN ou MASTER! Vocês têm permissões para este recurso.";
    }
    
    // Endpoint protegido que verifica se o usuário autenticado é um ADMIN ou MASTER antes de realizar uma ação crítica
    @GetMapping("/admin/critical")
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MASTER')")
    public String criticalAdminAction() {
        return "Ação crítica realizada! Somente ADMIN e MASTER podem realizar esta ação.";
    }
}
