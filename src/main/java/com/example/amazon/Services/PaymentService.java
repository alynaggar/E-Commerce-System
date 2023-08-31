package com.example.amazon.Services;

import com.example.amazon.Entities.AmazonResponseCode;
import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Entities.Payment;
import com.example.amazon.Entities.User.User;
import com.example.amazon.Repositories.PaymentRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private PaymentRepo paymentRepo;
    private UserService userService;

    public PaymentService(PaymentRepo paymentRepo, UserService userService) {
        this.paymentRepo = paymentRepo;
        this.userService = userService;
    }

    public void createPayment(Payment payment){
        paymentRepo.save(payment);
    }

    public void deletePayment(long id){
        paymentRepo.deleteById(id);
    }

    public AmazonResponseEntity<?> getById(long id){
        Optional<Payment> payment = paymentRepo.findById(id);
        if(payment.isPresent()){
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, payment);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.PAYMENT_NOT_FOUND);
    }

    public AmazonResponseEntity<?> getByUser(String username){
        Optional<User> user = userService.getByUsername(username);
        List<Payment> payments = user.get().getPayments();
        return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, payments);
    }

    public AmazonResponseEntity<?> getAll(){
        List<Payment> payments = paymentRepo.findAll();
        if(payments.isEmpty()){
            return new AmazonResponseEntity<>(AmazonResponseCode.PAYMENT_NOT_FOUND);
        }
        return  new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, payments);
    }
}
