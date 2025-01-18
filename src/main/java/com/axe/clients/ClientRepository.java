package com.axe.clients;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long>  //
, JpaSpecificationExecutor<Client> {

    @Query(value= """
            SELECT clients.id as clientId, name as clientName, phone, email, street, suburb, city, country
           FROM clients
           LEFT JOIN client_phones ON client_phones.client_id = clients.id AND client_phones.label = 'main'
           LEFT JOIN client_emails ON client_emails.client_id = clients.id AND client_emails.label = 'main'
           LEFT JOIN client_addresses ON client_addresses.client_id = clients.id AND client_addresses.label = 'main'
           """,nativeQuery = true)
    List<ClientDetails> getAllClientsWithDetails();

    interface Specs {
        /**
         * Filter Client by ID.
         * 
         * @param id The ID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<Client> withId(Number id) {
            return (root, query, builder) -> builder.equal(root.get("id"), id);
        }

        /**
         * Filter Client by name.
         * 
         * @param name The name to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<Client> withName(String name) {
            return (root, query, builder) -> builder.equal(root.get("name"), name);
        }

        /**
         * Returns a specification that orders the results by the 'name' attribute in ascending order null first.
         *
         * @param spec the original specification to which the ordering will be applied
         * @return a new specification with the ordering applied
         */
        static Specification<Client> orderByNameAsc(Specification<Client> spec) {
            return (root, query, builder) -> {
                if (query != null) {
                    query.orderBy(
                        builder.asc(builder.selectCase()
                                .when(root.get("name").isNull(), 0)
                                .otherwise(1)),
                        builder.asc(root.get("name")));
                    }
                    return spec.toPredicate(root, query, builder);
                };
            }
    }
}
