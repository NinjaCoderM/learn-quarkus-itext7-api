package at.codecrafters.repository;


import at.codecrafters.entity.ClientContact;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientContactRepository implements PanacheRepository<ClientContact> {

}


