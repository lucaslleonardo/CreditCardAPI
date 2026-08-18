package br.com.lucaslleonardo.CreditCardAPI.database.entity;


import br.com.lucaslleonardo.CreditCardAPI.database.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name="Usuario")
@Getter
@Setter
@Builder @ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Roles cargo;


}
