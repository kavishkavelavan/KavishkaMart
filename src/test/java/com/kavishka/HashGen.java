package com.kavishka;
import org.mindrot.jbcrypt.BCrypt;
public class HashGen {
    public static void main(String[] args) {
        System.out.println("HASH_ADMIN=" + BCrypt.hashpw("Admin123!", BCrypt.gensalt(10)));
        System.out.println("HASH_SELLER=" + BCrypt.hashpw("Seller123!", BCrypt.gensalt(10)));
        System.out.println("HASH_BUYER=" + BCrypt.hashpw("Buyer123!", BCrypt.gensalt(10)));
    }
}
