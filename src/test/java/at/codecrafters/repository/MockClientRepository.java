//package at.codecrafters.repository;
//
//import at.codecrafters.entity.Client;
//import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
//
//import java.util.List;
//
//public class MockClientRepository implements PanacheRepositoryBase<Client, Long> {
//    public List<Client> listAll() {
//        Client client = new Client();
//        client.setId(1L);
//        client.setFirstname("John");
//        client.setLastname("Doe");
//        client.setCompany("Test Company");
//        return List.of(client);
//    }
//    public Client findById(Long id) {
//        Client client = new Client();
//        client.setId(1L);
//        client.setFirstname("John");
//        client.setLastname("Doe");
//        client.setCompany("Test Company");
//        return client;
//    }
//
//    public void persist(Client client) {
//    }
//
//    public void delete(Client client) {
//    }
//}