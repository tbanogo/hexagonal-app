package eu.happycoders.shop.adapter.out.persistence.jpa;

import eu.happycoders.shop.adapter.out.persistence.AbstractCartRepositoryTest;
import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.application.port.out.persistence.ProductRepository;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class JpaCartRepositoryTest extends AbstractCartRepositoryTest {


    private static MySQLContainer<?> mysql;
    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    static void startDatatabase() {
        mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.1"));
        mysql.start();

        entityManagerFactory =
                EntityManagerFactoryFactory.createMySqlEntityManagerFactory(
                        mysql.getJdbcUrl(), "root", "test");
    }

    @Override
    protected CartRepository createCartRepository() {
        return new JpaCartRepository(entityManagerFactory);
    }

    @Override
    protected ProductRepository createProductRepository() {
        return new JpaProductRepository(entityManagerFactory);
    }

    @AfterAll
    static void stopDatatabase() {
        entityManagerFactory.close();
        mysql.stop();
    }
}
