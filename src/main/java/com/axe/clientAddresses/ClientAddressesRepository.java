package com.axe.clientAddresses;

import com.axe.clientAddresses.clientAdressDTOs.ClientAddressDTO;
import com.axe.clientAddresses.clientAdressDTOs.ClientAddressPostDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientAddressesRepository extends JpaRepository<ClientAddress, Long> {


    @Query( value ="SELECT * FROM axe.client_addresses WHERE client_id= :clientId ", nativeQuery = true)
    List<ClientAddressDTO> findAllAddressesForClient(Long clientId);
}
