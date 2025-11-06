import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    //get by id payment
    Optional<Payment> findById(Long id);
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findAll();
    void delete(Payment payment);
    void deleteAll();
    
}
