package ru.tipsauk.rental.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tipsauk.rental.dto.ClientDto;
import ru.tipsauk.rental.dto.DocumentDto;
import ru.tipsauk.rental.entity.Client;
import ru.tipsauk.rental.mapper.ClientMapper;
import ru.tipsauk.rental.repository.ClientRepository;
import ru.tipsauk.rental.service.ClientService;
import ru.tipsauk.rental.service.DocumentService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final DocumentService documentService;

    private final ClientMapper clientMapper;

    @Override
    public List<ClientDto> findAll() {
        log.info("ClientServiceImpl: findAll() (Start method)");
        return clientRepository.findAll().stream()
                .map(clientMapper::clientToClientDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto findById(long id) {
        log.info("ClientServiceImpl: findById(long id), id = " + id + " (Start method)");
        return clientMapper.clientToClientDto(clientRepository.findById(id));
    }

    @Override
    public Client create(ClientDto clientDto) {
        log.info("ClientServiceImpl: create(ClientDto clientDto), " + clientDto + " (Start method)");
        Client client = clientRepository.create(clientMapper.clientDtoToClient(clientDto));
        DocumentDto documentDto = clientDto.getDocument();
        documentDto.setClient_id(client.getId());
        documentService.create(documentDto);
        return client;
    }

    @Override
    public Client update(ClientDto clientDto) {
        log.info("ClientServiceImpl: update(ClientDto clientDto), " + clientDto + " (Start method)");
        Client client = clientRepository.update(clientMapper.clientDtoToClient(clientDto));
        DocumentDto documentDto = clientDto.getDocument();
        documentDto.setClient_id(client.getId());
        documentService.update(documentDto);
        return client;
    }

    @Override
    public void deleteById(long id) {
        log.info("ClientServiceImpl: delete(long id), id = " + id + " (Start method)");
        clientRepository.deleteById(id);
    }
}
