package ru.tipsauk.rental.dto;

import lombok.*;
import ru.tipsauk.rental.entity.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private long id;

    private String name;

    private String surname;

    private DocumentDto document;

}
