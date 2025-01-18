package com.axe.common.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
 
import com.axe.users.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

@Lazy
@Repository
public class UsersDaoImpl implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(QuotesDaoImpl.class);

    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    public UsersDaoImpl(final EntityManager entityManager$, final TransactionTemplate transactionTemplate$) {
        entityManager = entityManager$;
        transactionTemplate = transactionTemplate$;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public User fetchUserByEmail(final String userEmail) {
        logger.debug("Fetching user by email: {}", userEmail);
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            ParameterExpression<String> emailParameter = cb.parameter(String.class, "emailParam");

            CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            criteriaQuery.select(root).where(cb.equal(root.get("email"), emailParameter)).orderBy(cb.desc(root.get("id")));

            return entityManager.createQuery(criteriaQuery)
                    .setParameter(emailParameter, userEmail)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (final DataAccessException e) {
            logger.error("Data access error while fetching user by email: {}",
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Data access error while fetching user by email: ", e.getMostSpecificCause());
        } catch (final Exception e) {
            logger.error("Unknown error while fetching user by email: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("Initializing UsersDaoImpl");
    }

    @Override
    public void destroy() throws Exception {
        logger.debug("Destroying UsersDaoImpl");
    }
}
