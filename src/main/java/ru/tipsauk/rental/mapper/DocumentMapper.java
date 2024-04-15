package ru.tipsauk.rental.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.tipsauk.rental.dto.DocumentDto;
import ru.tipsauk.rental.entity.Document;

@Mapper
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

//    @Mapping(source = "series", target = "series")
//    @Mapping(source = "number", target = "number")
    //@Mapping(target="validDate", ignore = true)
    DocumentDto documentToDocumentDto(Document client);

//    @Mapping(source = "series", target = "series")
//    @Mapping(source = "number", target = "number")
//    @Mapping(source = "validDate", target = "validDate")
    Document DocumentDtoToDocument(DocumentDto clientDto);

}
