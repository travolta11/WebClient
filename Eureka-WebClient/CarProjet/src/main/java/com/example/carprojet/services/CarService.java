package com.example.carprojet.services;

import com.example.carprojet.entities.Car;
import com.example.carprojet.entities.Client;
import com.example.carprojet.models.CarResponse;
import com.example.carprojet.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    private final WebClient webClient;
    private final String CLIENT_SERVICE_URL = "http://localhost:8888/SERVICE-CLIENT";

    @Autowired
    public CarService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(CLIENT_SERVICE_URL).build();
    }

    public List<CarResponse> findAll() {
        // Fetch all cars from the repository
        List<Car> cars = carRepository.findAll();

        // Fetch all clients using WebClient
        Client[] clients = fetchClients();

        // Map each Car to CarResponse using the fetched clients
        return cars.stream()
                .map(car -> mapToCarResponse(car, clients))
                .collect(Collectors.toList());
    }

    private Client[] fetchClients() {
        try {
            Mono<Client[]> responseMono = webClient.get()
                    .uri("/api/client")
                    .retrieve()
                    .bodyToMono(Client[].class);

            return responseMono.block(); // Blocking call to get the response synchronously
        } catch (Exception e) {
            // Handle exceptions such as network errors
            // You can log the error and decide how to proceed (e.g., return empty array or rethrow)
            e.printStackTrace();
            return new Client[0];
        }
    }

    private CarResponse mapToCarResponse(Car car, Client[] clients) {
        // Assuming each Car has a clientId to associate with a Client
        Client client = Arrays.stream(clients)
                .filter(c -> c.getId().equals(car.getClient_id()))
                .findFirst()
                .orElse(null); // Handle the case where no matching client is found

        return new CarResponse.Builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .matricue(car.getMatricule())
                .client(client)
                .build();
    }
}